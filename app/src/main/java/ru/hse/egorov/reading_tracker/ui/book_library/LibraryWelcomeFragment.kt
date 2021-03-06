package ru.hse.egorov.reading_tracker.ui.book_library

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.hse.egorov.reading_tracker.R

class LibraryWelcomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_library_welcome, container, false)

    companion object {
        fun newInstance() = LibraryWelcomeFragment()
    }
}
