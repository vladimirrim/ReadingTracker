package ru.hse.egorov.reading_tracker.ui.statistics


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ru.hse.egorov.reading_tracker.R


class EditSessionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_edit_session, container, false)

    companion object {
        fun newInstance() = EditSessionFragment()
    }
}
