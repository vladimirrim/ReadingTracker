package ru.hse.egorov.reading_tracker.ui.book_library

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_adding_book.*
import kotlinx.android.synthetic.main.fragment_adding_book.view.*
import kotlinx.android.synthetic.main.fragment_library.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.adapter.LibraryAdapter
import ru.hse.egorov.reading_tracker.ui.bitmap.BitmapEncoder
import java.io.ByteArrayOutputStream

class EditBookFragment : Fragment(), BitmapEncoder {
    private val dbManager = DatabaseManager()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_adding_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        author.setText(arguments!!["author"] as String)
        title.setText(arguments!!["title"] as String)
        cover.background = BitmapDrawable(resources, Bitmap.createScaledBitmap(arguments!!["cover"] as Bitmap, dipToPixels(context!!, 155f),
                dipToPixels(context!!, 222f), false))
        val spinner: Spinner = view.mediaSpinner

        ArrayAdapter.createFromResource(
                context!!,
                R.array.media_spinner_array,
                R.layout.media_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.media_spinner_item)
            spinner.adapter = adapter
            mediaSpinner.setSelection(arguments!!["media"] as Int, true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.action_bar_enabled, menu)
        menu?.getItem(0)?.setOnMenuItemClickListener {
            val book = HashMap<String, Any?>()
            book["author"] = author.text.toString()
            book["title"] = title.text.toString()
            book["media"] = 0 // TODO get position from spinner
            val baos = ByteArrayOutputStream()
            getBitmap(cover.background).compress(Bitmap.CompressFormat.PNG, 100, baos)
            showProgressBar()
            dbManager.updateBook(book, arguments!!["bookId"] as String).addOnSuccessListener { uploadedBook ->
                dbManager.addBookCover(baos.toByteArray(), arguments!!["bookId"] as String).addOnSuccessListener {
                    (activity!!.library.adapter as LibraryAdapter).replaceItem(arguments!!["bookPosition"] as Int,
                            LibraryFragment.Book(author.text.toString(), title.text.toString(), arguments!!["bookId"] as String,
                                    0, getBitmap(cover.background)
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

    private fun showProgressBar() {
        cover.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance() = EditBookFragment()
    }
}