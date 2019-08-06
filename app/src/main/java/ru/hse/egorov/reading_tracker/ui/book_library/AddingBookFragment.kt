package ru.hse.egorov.reading_tracker.ui.book_library

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.exifinterface.media.ExifInterface
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.extensions.android.json.AndroidJsonFactory
import com.google.api.services.books.Books
import com.google.api.services.books.BooksRequestInitializer
import com.google.api.services.books.model.Volumes
import com.google.firebase.Timestamp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_adding_book.*
import kotlinx.android.synthetic.main.fragment_adding_book.view.*
import ru.hse.egorov.reading_tracker.BuildConfig
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ReadingTrackerApplication
import ru.hse.egorov.reading_tracker.ui.MainActivity
import ru.hse.egorov.reading_tracker.ui.MainActivity.Companion.SESSION_FRAGMENT_POSITION
import ru.hse.egorov.reading_tracker.ui.action_bar.ActionBarSetter
import ru.hse.egorov.reading_tracker.ui.adapter.LibraryAdapter
import ru.hse.egorov.reading_tracker.ui.dialog.AddCoverDialog
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.session.StartOfSessionFragment
import java.io.ByteArrayOutputStream
import javax.inject.Inject


class AddingBookFragment : BookFragment(), FragmentLauncher {
    private lateinit var ISBN_SCAN_FAILURE: String
    private lateinit var ADD_BOOK_FAILURE: String
    @Inject
    lateinit var library: LibraryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        ISBN_SCAN_FAILURE = resources.getString(R.string.isbn_scan_failure)
        ADD_BOOK_FAILURE = resources.getString(R.string.add_book_failure)
        return inflater.inflate(R.layout.fragment_adding_book, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity!!.application as ReadingTrackerApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateActionBar(activity as AppCompatActivity)

        cover.setOnClickListener {
            AddCoverDialog().show(activity!!.supportFragmentManager, "AddCover")
        }

        val spinner: Spinner = view.mediaSpinner
        ArrayAdapter.createFromResource(
                context!!,
                R.array.media_spinner_array,
                R.layout.media_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.media_spinner_item)
            spinner.adapter = adapter
        }

        arguments?.apply {
            arguments!!["set book"]?.let {
                setBook(this)
            }
            arguments!!["set isbn"]?.let {
                dispatchScanISBNIntent(this@AddingBookFragment)
            }
        }
    }

    private fun updateActionBar(activity: AppCompatActivity) {
        activity.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        activity.supportActionBar?.title = resources.getString(R.string.new_book)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.action_bar, menu)
        menu?.getItem(0)?.setOnMenuItemClickListener {
            val book = gatherBookInfo()
            val baos = ByteArrayOutputStream()
            getBitmap(cover.drawable).compress(Bitmap.CompressFormat.PNG, 100, baos)
            showProgressBar()
            dbManager.addBookToLibrary(book).addOnSuccessListener { uploadedBook ->
                dbManager.addBookCover(baos.toByteArray(), uploadedBook.id).addOnSuccessListener {
                    setBookToSession(uploadedBook.id)
                    library.add(setUpNewBook(book["last updated"] as Timestamp, uploadedBook.id))
                    library.sortByLastUpdated()
                    (activity?.supportFragmentManager?.findFragmentByTag("android:switcher:" + R.id.fragmentPager + ":"
                            + MainActivity.LIBRARY_FRAGMENT_POSITION) as ActionBarSetter).setActionBar(activity as AppCompatActivity)
                    openPagerFragment(activity as AppCompatActivity, MainActivity.LIBRARY_FRAGMENT_POSITION)
                }.addOnFailureListener { e ->
                    Snackbar.make(activity!!.placeSnackBar,
                            ADD_BOOK_FAILURE,
                            Toast.LENGTH_SHORT).show()
                    Log.e(TAG, e.localizedMessage)
                }
            }
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            cover.setImageBitmap(Bitmap.createScaledBitmap(
                    MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri), dipToPixels(context!!, 155f),
                    dipToPixels(context!!, 222f), false))
        }

        if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            cover.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, dipToPixels(context!!, 155f),
                    dipToPixels(context!!, 222f), false))
        }

        if (requestCode == REQUEST_SCAN_ISBN && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            val imageBitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)
            scanISBN(setOriginalImageOrientation(imageBitmap, imageUri!!))
        }
    }

    private fun scanISBN(bitmap: Bitmap) {
        showProgressBar()
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val detector = FirebaseVision.getInstance()
                .visionBarcodeDetector
        detector.detectInImage(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        if (barcode.valueType == FirebaseVisionBarcode.TYPE_ISBN) {
                            val isbn = barcode.rawValue!!
                            BookDownloader { success, volumes ->
                                if (success) {
                                    volumes?.items?.get(0)?.volumeInfo?.let {
                                        setBook(LibraryFragment.Book(it))
                                    }
                                } else {
                                    onIsbnFailure()
                                }
                                hideProgressBar()
                            }.execute(isbn)
                        }
                        return@addOnSuccessListener
                    }
                    hideProgressBar()
                    onIsbnFailure()
                }
                .addOnFailureListener {
                    hideProgressBar()
                    onIsbnFailure()
                }
    }

    private fun onIsbnFailure() {
        Snackbar.make(activity!!.placeSnackBar,
                ISBN_SCAN_FAILURE,
                Toast.LENGTH_SHORT).show()
    }

    private fun setBook(bundle: Bundle) {
        author.setText(bundle["author"] as String? ?: "")
        title.setText(bundle["title"] as String)
        val coverURL = bundle["coverURL"] as String?
        coverURL?.apply {
            dbManager.getBookCoverFromURL(this, context!!).into(cover)
        }
        val pages = bundle["pageCount"] as Int?
        pages?.apply { pageCount.setText(this.toString()) }
    }

    private fun setBook(book: LibraryFragment.Book) {
        author.setText(book.author)
        book.pageCount?.let { pageCount.setText(it.toString()) }
        title.setText(book.name)
        val coverURL = book.cover
        coverURL?.let { dbManager.getBookCoverFromURL(it, context!!).into(cover) }
    }

    private fun setOriginalImageOrientation(bitmap: Bitmap, imageUri: Uri): Bitmap {
        val ei = ExifInterface(activity!!.contentResolver.openInputStream(imageUri)!!)
        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED)
        var rotatedBitmap: Bitmap = bitmap
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                rotatedBitmap = rotateImage(bitmap, 90)
            }

            ExifInterface.ORIENTATION_ROTATE_180 -> {
                rotatedBitmap = rotateImage(bitmap, 180)
            }

            ExifInterface.ORIENTATION_ROTATE_270 -> {
                rotatedBitmap = rotateImage(bitmap, 270)
            }
        }
        return rotatedBitmap
    }

    private fun dispatchScanISBNIntent(fragment: Fragment) {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                fragment.startActivityForResult(takePictureIntent, AddingBookFragment.REQUEST_SCAN_ISBN)
            }
        }
    }

    private fun setBookToSession(id: String) {
        arguments?.let {
            (activity?.supportFragmentManager?.findFragmentByTag("android:switcher:" + R.id.fragmentPager + ":"
                    + SESSION_FRAGMENT_POSITION) as StartOfSessionFragment).setBook(author.text.toString(), title.text.toString(), id)
        }
    }

    private class BookDownloader(private val onDownloadFinish: (success: Boolean, Volumes?) -> Unit) : AsyncTask<String, Unit, Volumes>() {

        override fun doInBackground(vararg isbn: String?): Volumes {
            val books = Books.Builder(AndroidHttp.newCompatibleTransport(), AndroidJsonFactory.getDefaultInstance(), null)
                    .setApplicationName(BuildConfig.APPLICATION_ID)
                    .setGoogleClientRequestInitializer(BooksRequestInitializer(API_KEY))
                    .build()

            val list = books.volumes().list(ISBN_PREFIX + isbn[0])
            list.fields = "totalItems,items(volumeInfo(title,authors,pageCount,imageLinks/smallThumbnail),id)"
            return list.execute()
        }

        override fun onPostExecute(result: Volumes?) {
            onDownloadFinish(result == null, result)
        }
    }

    companion object {
        private const val ISBN_PREFIX = "isbn:"
        private const val API_KEY = "AIzaSyDyz5uPI7Bv-gZTosWKBPMHtVVXtWy_UEA"
        private const val TAG = "Add Book"
        const val REQUEST_IMAGE_GALLERY = 1
        const val REQUEST_IMAGE_CAMERA = 2
        const val REQUEST_SCAN_ISBN = 3

        fun newInstance(): AddingBookFragment = AddingBookFragment()
    }
}
