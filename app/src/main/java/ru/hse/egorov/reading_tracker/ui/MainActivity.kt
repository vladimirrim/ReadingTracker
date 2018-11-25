package ru.hse.egorov.reading_tracker.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.adapter.ViewPagerAdapter
import ru.hse.egorov.reading_tracker.ui.book_library.AddingBookFragment
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryWelcomeFragment
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.session.StartOfSessionFragment


class MainActivity : AppCompatActivity(), FragmentLauncher {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_session -> {
                openPagerFragment(this, SESSION_FRAGMENT_POSITION)
                item.isChecked = true
                fab.hide()
            }
            R.id.navigation_profile -> {
                openPagerFragment(this, PROFILE_FRAGMENT_POSITION)
                item.isChecked = true
                fab.show()
            }
            R.id.navigation_library -> {
                openPagerFragment(this, LIBRARY_FRAGMENT_POSITION)
                item.isChecked = true
                fab.show()
            }
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.menu.getItem(1).isChecked = true

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(LibraryWelcomeFragment.newInstance(), "profile")
        adapter.addFragment(StartOfSessionFragment.newInstance(), "session")
        adapter.addFragment(LibraryFragment.newInstance(), "library")
        fragmentPager.adapter = adapter

        fab.setOnClickListener {
            openTemporaryFragment(this, AddingBookFragment.newInstance(), R.id.temporaryFragment)
            fab.hide()
        }

        fragmentPager.currentItem = SESSION_FRAGMENT_POSITION
    }

    companion object {
        const val PROFILE_FRAGMENT_POSITION = 0
        const val SESSION_FRAGMENT_POSITION = 1
        const val LIBRARY_FRAGMENT_POSITION = 2
    }
}
