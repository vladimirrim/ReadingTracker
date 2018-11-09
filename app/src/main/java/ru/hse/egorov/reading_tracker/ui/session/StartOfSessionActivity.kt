package ru.hse.egorov.reading_tracker.ui.session

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start_of_session.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment

class StartOfSessionActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null
        when (item.itemId) {
            R.id.navigation_session -> {
                selectedFragment = LibraryFragment.newInstance()
                item.isChecked = true
            }
            R.id.navigation_profile -> {
                selectedFragment = LibraryFragment.newInstance()
                item.isChecked = true
            }
            R.id.navigation_library -> {
                selectedFragment = LibraryFragment.newInstance()
                item.isChecked = true
            }
        }
        openFragment(selectedFragment!!)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_of_session)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.menu.getItem(1).isChecked = true

        val fragment = LibraryFragment.newInstance()

        openFragment(fragment)
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit()
    }
}
