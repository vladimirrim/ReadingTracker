package ru.hse.egorov.reading_tracker.ui.fragment

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

interface FragmentLauncher {

    fun openFragment(fragment: Fragment, activity: AppCompatActivity, fragmentID: Int) {
        activity.supportFragmentManager.beginTransaction().replace(fragmentID, fragment).addToBackStack(null).commit()
    }

    fun openInnerFragment(innerFragment: Fragment, outerFragment: Fragment, fragmentID: Int) {
        outerFragment.childFragmentManager.beginTransaction().replace(fragmentID, innerFragment).addToBackStack(null).commit()
    }
}