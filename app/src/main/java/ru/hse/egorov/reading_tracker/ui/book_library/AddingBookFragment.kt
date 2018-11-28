package ru.hse.egorov.reading_tracker.ui.book_library

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.media.ExifInterface
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage
import com.google.firebase.Timestamp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_adding_book.*
import kotlinx.android.synthetic.main.fragment_adding_book.view.*
import kotlinx.android.synthetic.main.fragment_library.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.MainActivity.Companion.SESSION_FRAGMENT_POSITION
import ru.hse.egorov.reading_tracker.ui.adapter.LibraryAdapter
import ru.hse.egorov.reading_tracker.ui.dialog.AddCoverDialog
import ru.hse.egorov.reading_tracker.ui.session.StartOfSessionFragment
import java.io.ByteArrayOutputStream


class AddingBookFragment : BookFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_adding_book, container, false)
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
    }

    private fun updateActionBar(activity: AppCompatActivity) {
        activity.supportActionBar?.title = ACTION_BAR_TITLE
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
                    (activity!!.library.adapter as LibraryAdapter).add(setUpNewBook(book["last updated"] as Timestamp, uploadedBook.id))
                    activity?.fragmentPager?.visibility = View.VISIBLE
                    activity?.temporaryFragment?.visibility = View.GONE
                }.addOnFailureListener {
                    //TODO
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
                            dbManager.getBookByISBN(isbn).addOnSuccessListener {
                                author.setText(it["author"] as String)
                                title.setText(it["name"] as String)
                                val coverURL = it["coverURL"] as String
                                dbManager.getBookCoverFromURL(coverURL, context!!).into(cover)
                                hideProgressBar()
                            }
                            return@addOnSuccessListener
                        }
                    }
                    hideProgressBar()
                    Snackbar.make(activity!!.placeSnackBar,
                            ISBN_SCAN_FAILURE,
                            Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    hideProgressBar()
                    Snackbar.make(activity!!.placeSnackBar,
                            ISBN_SCAN_FAILURE,
                            Toast.LENGTH_SHORT).show()
                }
    }

    private fun setOriginalImageOrientation(bitmap: Bitmap, imageUri: Uri): Bitmap {
        val ei = ExifInterface(activity!!.contentResolver.openInputStream(imageUri))
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

    private fun setBookToSession(id: String) {
        arguments?.let { bundle ->
            if (bundle.getBoolean("set book")) {
                (activity?.supportFragmentManager?.findFragmentByTag("android:switcher:" + R.id.fragmentPager + ":"
                        + SESSION_FRAGMENT_POSITION) as StartOfSessionFragment).setBook(author.text.toString(), title.text.toString(), id,
                        getBitmap(cover.drawable))
            }
        }
    }

    companion object {
        const val ACTION_BAR_TITLE = "Новая книга"
        const val ISBN_SCAN_FAILURE = "Не удалось распознать ISBN код."
        const val REQUEST_IMAGE_GALLERY = 1
        const val REQUEST_IMAGE_CAMERA = 2
        const val REQUEST_SCAN_ISBN = 3
        fun newInstance(): AddingBookFragment = AddingBookFragment()
    }
}
