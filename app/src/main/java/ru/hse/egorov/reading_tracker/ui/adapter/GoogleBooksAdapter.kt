package ru.hse.egorov.reading_tracker.ui.adapter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.book_library.AddingBookFragment
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher

class GoogleBooksAdapter : BookAdapter(), FragmentLauncher {
    override fun bindContainer(container: View, book: Bundle) {
        container.setOnClickListener {
            val dispatchFragment = AddingBookFragment.newInstance()
            dispatchFragment.arguments = book
            openTemporaryFragment(container.context as AppCompatActivity, dispatchFragment, R.id.temporaryFragment)
        }
    }
}