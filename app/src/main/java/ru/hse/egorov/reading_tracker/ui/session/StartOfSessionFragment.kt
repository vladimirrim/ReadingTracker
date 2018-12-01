package ru.hse.egorov.reading_tracker.ui.session

import android.app.ActionBar
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.fragment_start_of_session.*
import kotlinx.android.synthetic.main.fragment_start_of_session.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.MainActivity.Companion.LIBRARY_FRAGMENT_POSITION
import ru.hse.egorov.reading_tracker.ui.bitmap.BitmapEncoder
import ru.hse.egorov.reading_tracker.ui.book_library.AddingBookFragment
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.session.session_inner_fragments.AutoSessionTimeChangeFragment


class StartOfSessionFragment : Fragment(), FragmentLauncher, BitmapEncoder {
    private lateinit var doneButton: MenuItem
    private val dbManager = DatabaseManager()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_start_of_session, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpActionBar()

        val libraryAdapter = LibraryFragment.getAdapter()
        if (libraryAdapter.itemCount != 0) {
            val book = libraryAdapter.get(0)
            setBook(book.author, book.name, book.id, book.cover)
        }

        view.addBook.setOnClickListener {
            val dispatchFragment = AddingBookFragment.newInstance()
            val bundle = Bundle()
            bundle.putBoolean("set book", true)
            dispatchFragment.arguments = bundle
            openTemporaryFragment(activity as AppCompatActivity, dispatchFragment, R.id.temporaryFragment)
        }

        view.bookContainer.setOnClickListener {
            openPagerFragment(activity as AppCompatActivity, LIBRARY_FRAGMENT_POSITION)
        }

        openInnerFragment(AutoSessionTimeChangeFragment.newInstance(), this, R.id.sessionFragment)
    }

    fun setBook(author: String, title: String, id: String, cover: Bitmap?) {
        this.comment.maxLines = 1
        this.comment.text = author
        this.title.maxLines = 2
        this.title.text = title
        this.addBook.visibility = View.INVISIBLE
        if (cover == null) {
            dbManager.getBookCover(id, context!!).into(this.cover)
        } else {
            this.cover.setImageBitmap(cover)
        }
        this.cover.visibility = View.VISIBLE
        this.comment.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.action_bar, menu)
        doneButton = menu!!.getItem(0)
        doneButton.isEnabled = false
    }

    private fun setUpActionBar() {
        val supportedActivity = activity as AppCompatActivity
        supportedActivity.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        supportedActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportedActivity.supportActionBar?.title = ACTION_BAR_TITLE
        supportedActivity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
        setHasOptionsMenu(true)
    }


    companion object {
        fun newInstance() = StartOfSessionFragment()
        private const val ACTION_BAR_TITLE = "Новая запись о чтении"
    }
}
