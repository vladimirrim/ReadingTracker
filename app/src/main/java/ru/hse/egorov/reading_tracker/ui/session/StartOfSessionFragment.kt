package ru.hse.egorov.reading_tracker.ui.session

import android.app.ActionBar
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.fragment_start_of_session.*
import kotlinx.android.synthetic.main.fragment_start_of_session.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.MainActivity.Companion.LIBRARY_FRAGMENT_POSITION
import ru.hse.egorov.reading_tracker.ui.action_bar.ActionBarSetter
import ru.hse.egorov.reading_tracker.ui.bitmap.BitmapEncoder
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment
import ru.hse.egorov.reading_tracker.ui.dialog.AddBookDialog
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.session.session_inner_fragments.AutoSessionTimeChangeFragment


class StartOfSessionFragment : Fragment(), FragmentLauncher, BitmapEncoder, ActionBarSetter {
    private val dbManager = DatabaseManager()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_start_of_session, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val libraryAdapter = LibraryFragment.getAdapter()
        if (libraryAdapter.itemCount != 0) {
            val book = libraryAdapter.get(0)
            setBook(book.author, book.name, book.id)
        }

        view.addBook.setOnClickListener {
            AddBookDialog().show(fragmentManager, "Add Book")
        }

        view.bookContainer.setOnClickListener {
            openPagerFragment(activity as AppCompatActivity, LIBRARY_FRAGMENT_POSITION)
        }

        resetSession()
    }

    fun resetSession() {
        val dispatchFragment = AutoSessionTimeChangeFragment.newInstance()
        dispatchFragment.arguments = setBundle()
        openInnerFragment(dispatchFragment, this, R.id.sessionFragment)
    }

    private fun setBundle(): Bundle {
        val bundle = Bundle()
        bundle.putString("author", author.text.toString())
        bundle.putString("title", title.text.toString())
        return bundle
    }

    fun setBook(author: String?, title: String, id: String) {
        this.author.maxLines = 1
        this.author.text = author
        this.title.maxLines = 2
        this.title.text = title
        this.addBook.visibility = View.INVISIBLE
        dbManager.getBookCover(id, context!!).into(this.cover)
        this.cover.visibility = View.VISIBLE
        this.author.visibility = View.VISIBLE
        resetSession()
    }

    override fun setActionBar(activity: AppCompatActivity) {
        setHasOptionsMenu(true)
        activity.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        activity.supportActionBar?.setCustomView(R.layout.session_action_bar)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
    }

    companion object {
        fun newInstance() = StartOfSessionFragment()
    }
}
