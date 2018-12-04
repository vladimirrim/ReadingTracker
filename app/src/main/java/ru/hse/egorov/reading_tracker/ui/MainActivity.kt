package ru.hse.egorov.reading_tracker.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.action_bar.ActionBarSetter
import ru.hse.egorov.reading_tracker.ui.adapter.ViewPagerAdapter
import ru.hse.egorov.reading_tracker.ui.book_library.AddingBookFragment
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryWelcomeFragment
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.session.StartOfSessionFragment
import ru.hse.egorov.reading_tracker.ui.statistics.SessionsStatisticsFragment


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

    private val fragments = ArrayList<ActionBarSetter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.menu.getItem(1).isChecked = true

        val adapter = ViewPagerAdapter(supportFragmentManager)
        fragments.add(SessionsStatisticsFragment.newInstance())
        fragments.add(StartOfSessionFragment.newInstance())
        fragments.add(LibraryFragment.newInstance())
        adapter.addFragment(fragments[PROFILE_FRAGMENT_POSITION] as Fragment, "profile")
        adapter.addFragment(fragments[SESSION_FRAGMENT_POSITION] as Fragment, "session")
        adapter.addFragment(fragments[LIBRARY_FRAGMENT_POSITION] as Fragment, "library")
        fragmentPager.adapter = adapter
        fragmentPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(p0: Int) {
                val selectedFragment = fragments[p0]
                selectedFragment.setActionBar(this@MainActivity)
                invalidateOptionsMenu()
            }

        })

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
