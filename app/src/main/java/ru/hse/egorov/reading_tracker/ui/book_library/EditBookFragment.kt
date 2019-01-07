package ru.hse.egorov.reading_tracker.ui.book_library

import android.graphics.Bitmap
import android.os.Bundle
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
import ru.hse.egorov.reading_tracker.ui.adapter.LibraryAdapter
import ru.hse.egorov.reading_tracker.ui.book_library.BookFragment.Companion.MediaType.Companion.getIdByName
import java.io.ByteArrayOutputStream

class EditBookFragment : BookFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_adding_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        author.setText(arguments!!["author"] as String)
        title.setText(arguments!!["title"] as String)

        dbManager.getBookCover(arguments!!["bookId"] as String, context!!).into(cover)

        val spinner = view.mediaSpinner
        ArrayAdapter.createFromResource(
                context!!,
                R.array.media_spinner_array,
                R.layout.media_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.media_spinner_item)
            spinner.adapter = adapter
            mediaSpinner.setSelection(getIdByName(arguments!!["media"] as String), true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.action_bar, menu)
        menu?.getItem(0)?.setOnMenuItemClickListener {
            val book = gatherBookInfo()
            val baos = ByteArrayOutputStream()
            getBitmap(cover.drawable).compress(Bitmap.CompressFormat.PNG, 100, baos)
            showProgressBar()
            dbManager.updateBook(book, arguments!!["bookId"] as String).addOnSuccessListener {
                dbManager.addBookCover(baos.toByteArray(), arguments!!["bookId"] as String).addOnSuccessListener {
                    (activity!!.library.adapter as LibraryAdapter).replaceItem(arguments!!["bookPosition"] as Int,
                            setUpNewBook(book["last updated"] as Timestamp, arguments!!["bookId"] as String))
                    activity?.fragmentPager?.visibility = View.VISIBLE
                    activity?.temporaryFragment?.visibility = View.GONE
                }.addOnFailureListener {
                    //TODO
                }
            }
            true
        }
    }

    companion object {
        fun newInstance() = EditBookFragment()
    }
}