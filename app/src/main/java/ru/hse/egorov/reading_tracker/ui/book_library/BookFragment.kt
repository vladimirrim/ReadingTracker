package ru.hse.egorov.reading_tracker.ui.book_library

import android.support.v4.app.Fragment
import android.view.View
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.fragment_adding_book.*
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.bitmap.BitmapEncoder
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment.Book
import ru.hse.egorov.reading_tracker.ui.book_library.BookFragment.Companion.MediaType.Companion.getMediaById
import java.util.*

abstract class BookFragment : Fragment(), BitmapEncoder {
    protected val dbManager = DatabaseManager()

    protected fun showProgressBar() {
        cover.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    protected fun gatherBookInfo(): HashMap<String, Any?> {
        val book = HashMap<String, Any?>()
        book["author"] = author.text.toString()
        book["title"] = title.text.toString()
        book["media"] = getMediaById(mediaSpinner.selectedItemId)
        if (pageCount.text.toString().isNotEmpty()) book["pageCount"] = pageCount.text.toString().toInt()
        book["last updated"] = Timestamp(Calendar.getInstance().time)
        return book
    }

    protected fun setUpNewBook(lastUpdated: Timestamp, id: String): Book {
        return Book(author.text.toString(), title.text.toString(), id,
                getMediaById(mediaSpinner.selectedItemId), null,
                lastUpdated.toDate(), pageCount.text?.toString()?.toIntOrNull())
    }

    protected fun hideProgressBar() {
        progressBar.visibility = View.GONE
        cover.visibility = View.VISIBLE
    }

    companion object {
        protected enum class MediaType(private val id: Long) {
            TABLET(2), EBOOK(3), PAPER(0), SMARTPHONE(1);

            companion object {
                fun getMediaById(id: Long): String {
                    return values().firstOrNull { it.id == id }.toString().toLowerCase()
                }

                fun getIdByName(name: String): Int {
                    return values().firstOrNull { it.toString().toLowerCase() == name.toLowerCase() }?.id!!.toInt()
                }
            }
        }
    }
}