package ru.hse.egorov.reading_tracker.ui.fragment

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import ru.hse.egorov.reading_tracker.R

interface FragmentLauncher {

    fun openPagerFragment(activity: AppCompatActivity, fragmentPosition: Int) {
        activity.supportFragmentManager.findFragmentById(R.id.temporaryFragment)?.apply {
            activity.supportFragmentManager.beginTransaction().remove(this).commit()
        }
        activity.fragmentPager.currentItem = fragmentPosition
        activity.navigation.menu.getItem(fragmentPosition).isChecked = true
        activity.fragmentPager.visibility = View.VISIBLE
        activity.temporaryFragment.visibility = View.GONE
    }

    fun openTemporaryFragment(activity: AppCompatActivity, fragment: Fragment, fragmentID: Int) {
        activity.supportFragmentManager.beginTransaction().replace(fragmentID, fragment).commit()
        activity.temporaryFragment.visibility = View.VISIBLE
        activity.fragmentPager.visibility = View.GONE
    }

    fun openInnerFragment(innerFragment: Fragment, outerFragment: Fragment, fragmentID: Int) {
        outerFragment.childFragmentManager.beginTransaction().replace(fragmentID, innerFragment).addToBackStack(null).commit()
    }
}