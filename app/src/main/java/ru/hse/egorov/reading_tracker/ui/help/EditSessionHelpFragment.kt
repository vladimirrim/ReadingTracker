package ru.hse.egorov.reading_tracker.ui.help

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_help_image.*
import ru.hse.egorov.reading_tracker.R

class EditSessionHelpFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_help_image, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tip.setText(R.string.edit_session_help)
        image.setImageResource(R.drawable.help_edit_session)
    }

    companion object {
        fun newInstance() = EditSessionHelpFragment()
    }
}