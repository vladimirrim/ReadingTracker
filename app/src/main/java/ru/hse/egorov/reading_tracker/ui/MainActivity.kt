package ru.hse.egorov.reading_tracker.ui

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.book_library.AddingBookFragment
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryWelcomeFragment
import ru.hse.egorov.reading_tracker.ui.session.StartOfSessionFragment


class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null
        when (item.itemId) {
            R.id.navigation_session -> {
                selectedFragment = StartOfSessionFragment.newInstance()
                item.isChecked = true
                fab.hide()
            }
            R.id.navigation_profile -> {
                selectedFragment = LibraryWelcomeFragment.newInstance()
                item.isChecked = true
                fab.show()
            }
            R.id.navigation_library -> {
                selectedFragment = LibraryFragment.newInstance()
                item.isChecked = true
                fab.show()
            }
        }
        openFragment(selectedFragment!!)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ACTION_BAR_TITLE
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.menu.getItem(1).isChecked = true

        fab.setOnClickListener {
            openFragment(AddingBookFragment.newInstance())
            fab.hide()
        }

        openFragment(StartOfSessionFragment.newInstance())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar, menu)
        return true
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit()
    }

    companion object {
        private const val ACTION_BAR_TITLE = "Новая запись о чтении"
    }
}
