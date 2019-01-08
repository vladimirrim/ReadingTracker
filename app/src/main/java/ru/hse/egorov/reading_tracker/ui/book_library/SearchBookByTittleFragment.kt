package ru.hse.egorov.reading_tracker.ui.book_library

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.extensions.android.json.AndroidJsonFactory
import com.google.api.services.books.Books
import com.google.api.services.books.BooksRequestInitializer
import com.google.api.services.books.model.Volumes
import kotlinx.android.synthetic.main.fragment_search_book_by_title.view.*
import ru.hse.egorov.reading_tracker.BuildConfig
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.adapter.GoogleBooksAdapter


class SearchBookByTittleFragment : Fragment() {
    private val libraryAdapter = GoogleBooksAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_search_book_by_title, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter(view)

        view.findButton.setOnClickListener {
            val query = view.title.text.toString()
            if (query != "") {
                BookDownloader { _, books ->
                    libraryAdapter.clear()
                    libraryAdapter.set(convert(books))
                }.execute(query)
            }
        }
    }

    private fun convert(volumes: Volumes?): ArrayList<LibraryFragment.Book> {
        return if (volumes?.items != null) {
            val vols = volumes.items
            val books = ArrayList<LibraryFragment.Book>(vols.size)
            for (vol in vols) {
                books.add(LibraryFragment.Book(vol.volumeInfo))
            }
            books
        } else {
            ArrayList()
        }
    }

    private fun setUpAdapter(view: View) {
        view.results.layoutManager = LinearLayoutManager(context)
        view.results.adapter = libraryAdapter
    }

    private class BookDownloader(private val onDownloadFinish: (success: Boolean, Volumes) -> Unit) : AsyncTask<String, Unit, Volumes>() {

        override fun doInBackground(vararg p0: String?): Volumes {
            val books = Books.Builder(AndroidHttp.newCompatibleTransport(), AndroidJsonFactory.getDefaultInstance(), null)
                    .setApplicationName(BuildConfig.APPLICATION_ID)
                    .setGoogleClientRequestInitializer(BooksRequestInitializer(API_KEY))
                    .build()

            val list = books.volumes().list(p0[0])
            list.fields = "totalItems,items(volumeInfo(title,authors,pageCount,imageLinks/smallThumbnail),id)"
            return list.execute()
        }

        override fun onPostExecute(result: Volumes?) {
            onDownloadFinish(true, result!!)
        }
    }

    companion object {
        private const val API_KEY = "AIzaSyDyz5uPI7Bv-gZTosWKBPMHtVVXtWy_UEA"

        fun newInstance() = SearchBookByTittleFragment()
    }
}
