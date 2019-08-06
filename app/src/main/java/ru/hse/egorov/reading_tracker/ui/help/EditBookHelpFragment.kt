package ru.hse.egorov.reading_tracker.ui.help

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_help_image.*
import ru.hse.egorov.reading_tracker.R
import android.graphics.drawable.BitmapDrawable



class EditBookHelpFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_help_image, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tip.setText(R.string.edit_book_help)
        try {
            (image.drawable as BitmapDrawable?)?.bitmap?.recycle()
            image.setImageResource(R.drawable.help_edit_book)
        } catch (e: Exception) {
            Snackbar.make(activity!!.placeSnackBar,
                    "Error: " + e.localizedMessage,
                    Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance() = EditBookHelpFragment()
    }
}