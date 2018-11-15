package ru.hse.egorov.reading_tracker.ui.book_library

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.fragment_adding_book.*
import kotlinx.android.synthetic.main.fragment_adding_book.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.bitmap.BitmapEncoder
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class AddingBookFragment : Fragment(), BitmapEncoder {
    private val dbManager = DatabaseManager()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_adding_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateActionBar(activity as AppCompatActivity)

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
        inflater?.inflate(R.menu.action_bar_enabled, menu)
        menu?.getItem(0)?.setOnMenuItemClickListener {
            val book = HashMap<String, Any?>()
            book["author"] = author.text.toString()
            book["title"] = title.text.toString()
            val baos = ByteArrayOutputStream()
            getBitmap(cover.background as VectorDrawable).compress(Bitmap.CompressFormat.PNG, 100, baos)
            dbManager.addBookToLibrary(book).addOnSuccessListener {
                (activity as AppCompatActivity).supportFragmentManager.popBackStack()
            }
            true
        }
    }

    companion object {
        const val ACTION_BAR_TITLE = "Новая книга"
        fun newInstance(): AddingBookFragment = AddingBookFragment()
    }
}
