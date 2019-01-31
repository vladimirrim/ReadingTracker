package ru.hse.egorov.reading_tracker.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.action_bar.ActionBarSetter
import ru.hse.egorov.reading_tracker.ui.adapter.ViewPagerAdapter
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryWelcomeFragment
import ru.hse.egorov.reading_tracker.ui.dialog.AddBookDialog
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.session.StartOfSessionFragment
import ru.hse.egorov.reading_tracker.ui.statistics.OverallStatisticsFragment


class MainActivity : AppCompatActivity(), FragmentLauncher {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_session -> {
                openPagerFragment(this, SESSION_FRAGMENT_POSITION)
                (adapter.getItem(SESSION_FRAGMENT_POSITION) as ActionBarSetter).setActionBar(this)
                invalidateOptionsMenu()
                fab.hide()
            }
            R.id.navigation_profile -> {
                openPagerFragment(this, PROFILE_FRAGMENT_POSITION)
                (adapter.getItem(PROFILE_FRAGMENT_POSITION) as ActionBarSetter).setActionBar(this)
                invalidateOptionsMenu()
                fab.hide()
            }
            R.id.navigation_library -> {
                if (LibraryFragment.getAdapter().itemCount == 0) {
                    openTemporaryFragment(this, LibraryWelcomeFragment.newInstance(), R.id.temporaryFragment)
                } else {
                    openPagerFragment(this, LIBRARY_FRAGMENT_POSITION)
                    (adapter.getItem(LIBRARY_FRAGMENT_POSITION) as ActionBarSetter).setActionBar(this)
                }
                invalidateOptionsMenu()
                fab.show()
            }
        }
        true
    }

    private val adapter = ViewPagerAdapter(supportFragmentManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.menu.getItem(1).isChecked = true

        setUpViewPager()
        fab.setOnClickListener {
            AddBookDialog().show(supportFragmentManager, "Add Book")
            fab.hide()
        }

        fragmentPager.currentItem = SESSION_FRAGMENT_POSITION
    }

    private fun setUpViewPager() {
        adapter.addFragment(LibraryFragment.newInstance(), "library")
        adapter.addFragment(StartOfSessionFragment.newInstance(), "session")
        adapter.addFragment(OverallStatisticsFragment.newInstance(), "profile")
        fragmentPager.adapter = adapter
        fragmentPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(p0: Int) {
                val selectedFragment = adapter.getItem(p0) as ActionBarSetter
                selectedFragment.setActionBar(this@MainActivity)
                invalidateFragmentMenus(p0)
            }

        })

    }

    private fun invalidateFragmentMenus(position: Int) {
        for (i in 0 until adapter.count) {
            adapter.getItem(i).setHasOptionsMenu(i == position)
        }
        invalidateOptionsMenu()
    }

    companion object {
        const val PROFILE_FRAGMENT_POSITION = 2
        const val SESSION_FRAGMENT_POSITION = 1
        const val LIBRARY_FRAGMENT_POSITION = 0
    }
}
