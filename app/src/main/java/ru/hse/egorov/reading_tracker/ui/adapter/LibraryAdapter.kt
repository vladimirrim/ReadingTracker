package ru.hse.egorov.reading_tracker.ui.adapter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import ru.hse.egorov.reading_tracker.ui.MainActivity.Companion.SESSION_FRAGMENT_POSITION
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher


class LibraryAdapter : BookAdapter(), FragmentLauncher {
    override fun bindContainer(container: View, book: Bundle) {
        container.setOnClickListener {
            openPagerFragment(container.context as AppCompatActivity, SESSION_FRAGMENT_POSITION)
        }
    }
}