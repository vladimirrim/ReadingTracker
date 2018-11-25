package ru.hse.egorov.reading_tracker.ui.book_library

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_adding_book.*
import kotlinx.android.synthetic.main.fragment_adding_book.view.*
import kotlinx.android.synthetic.main.fragment_library.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.MainActivity.Companion.SESSION_FRAGMENT_POSITION
import ru.hse.egorov.reading_tracker.ui.adapter.LibraryAdapter
import ru.hse.egorov.reading_tracker.ui.bitmap.BitmapEncoder
import ru.hse.egorov.reading_tracker.ui.session.StartOfSessionFragment
import java.io.ByteArrayOutputStream
import java.util.*


class AddingBookFragment : Fragment(), BitmapEncoder {
    private val dbManager = DatabaseManager()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_adding_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateActionBar(activity as AppCompatActivity)

        cover.setOnClickListener {
            dispatchTakePictureIntent()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageUri = data?.data
            cover.background = BitmapDrawable(resources, Bitmap.createScaledBitmap(
                    MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri), dipToPixels(context!!, 155f),
                    dipToPixels(context!!, 222f), false))
        }
    }

    private fun updateActionBar(activity: AppCompatActivity) {
        activity.supportActionBar?.title = ACTION_BAR_TITLE
    }

    private fun showProgressBar() {
        cover.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun dispatchTakePictureIntent() {
        Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.action_bar, menu)
        menu?.getItem(0)?.setOnMenuItemClickListener {
            val book = HashMap<String, Any?>()
            book["author"] = author.text.toString()
            book["title"] = title.text.toString()
            book["media"] = 0 // TODO get position from spinner
            book["last updated"] = Timestamp(Calendar.getInstance().time)
            val baos = ByteArrayOutputStream()
            getBitmap(cover.background).compress(Bitmap.CompressFormat.PNG, 100, baos)
            showProgressBar()
            dbManager.addBookToLibrary(book).addOnSuccessListener { uploadedBook ->
                dbManager.addBookCover(baos.toByteArray(), uploadedBook.id).addOnSuccessListener {
                    setBookToSession()
                    (activity!!.library.adapter as LibraryAdapter).add(LibraryFragment.Book(author.text.toString(), title.text.toString(), uploadedBook.id,
                            0, getBitmap(cover.background), (book["last updated"] as Timestamp).toDate()
                    ))
                    activity?.fragmentPager?.visibility = View.VISIBLE
                    activity?.temporaryFragment?.visibility = View.GONE
                }.addOnFailureListener {
                    //TODO
                }
            }
            true
        }
    }

    private fun setBookToSession() {
        arguments?.let { bundle ->
            if (bundle.getBoolean("set book")) {
                (activity?.supportFragmentManager?.findFragmentByTag("android:switcher:" + R.id.fragmentPager + ":"
                        + SESSION_FRAGMENT_POSITION) as StartOfSessionFragment).setBook(author.text.toString(), title.text.toString(),
                        cover.background)
            }
        }
    }

    companion object {
        const val ACTION_BAR_TITLE = "Новая книга"
        const val REQUEST_IMAGE_CAPTURE = 1
        fun newInstance(): AddingBookFragment = AddingBookFragment()
    }
}
