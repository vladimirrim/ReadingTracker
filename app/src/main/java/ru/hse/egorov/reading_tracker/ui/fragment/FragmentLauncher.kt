package ru.hse.egorov.reading_tracker.ui.fragment

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import ru.hse.egorov.reading_tracker.R

interface FragmentLauncher {
    fun openFragment(fragment: Fragment, activity: AppCompatActivity) {
        activity.supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit()
    }
}