package ru.hse.egorov.reading_tracker.ui.session

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.hse.egorov.reading_tracker.R

class StartOfSessionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_start_of_session, container, false)

    companion object {
        fun newInstance(): StartOfSessionFragment = StartOfSessionFragment()
    }
}
