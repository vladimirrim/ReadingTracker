package ru.hse.egorov.reading_tracker.ui.book_library

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_adding_book.*
import ru.hse.egorov.reading_tracker.R

class EditBookFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_adding_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        author.setText(arguments!!["author"] as String)
        title.setText(arguments!!["title"] as String)
        mediaSpinner.setSelection(arguments!!["media"] as Int, true)
        cover.background = BitmapDrawable(resources, arguments!!["cover"] as Bitmap)
    }

    companion object {
        fun newInstance() = EditBookFragment()
    }
}