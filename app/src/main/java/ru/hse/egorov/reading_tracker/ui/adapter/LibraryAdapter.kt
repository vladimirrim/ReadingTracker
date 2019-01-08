package ru.hse.egorov.reading_tracker.ui.adapter

import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.MainActivity.Companion.SESSION_FRAGMENT_POSITION
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.session.StartOfSessionFragment
import java.util.*


class LibraryAdapter : BookAdapter(), FragmentLauncher {
    override fun bindContainer(container: View, book: LibraryFragment.Book) {
        container.setOnClickListener {
            ((it.context as AppCompatActivity).supportFragmentManager?.findFragmentByTag("android:switcher:" + R.id.fragmentPager + ":"
                    + SESSION_FRAGMENT_POSITION) as StartOfSessionFragment).setBook(book.author,
                    book.name, book.id)
            book.lastUpdated = Calendar.getInstance().time
            (it.context as AppCompatActivity).fab.hide()
            openPagerFragment(container.context as AppCompatActivity, SESSION_FRAGMENT_POSITION)
            LibraryFragment.getAdapter().sortByLastUpdated()
        }
    }
}