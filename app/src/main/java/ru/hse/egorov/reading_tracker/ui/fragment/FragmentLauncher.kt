package ru.hse.egorov.reading_tracker.ui.fragment

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

interface FragmentLauncher {

    fun openPagerFragment(activity: AppCompatActivity, fragmentPosition: Int) {
        activity.fragmentPager.currentItem = fragmentPosition
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