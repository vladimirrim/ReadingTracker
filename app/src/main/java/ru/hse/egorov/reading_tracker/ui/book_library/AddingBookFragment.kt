package ru.hse.egorov.reading_tracker.ui.book_library

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.fragment_adding_book.view.*
import ru.hse.egorov.reading_tracker.R

class AddingBookFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_adding_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateActionBar(activity as AppCompatActivity)

        val spinner: Spinner = view.mediaSpinner


        ArrayAdapter.createFromResource(
                context!!,
                R.array.media_spinner_array,
                R.layout.media_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.media_spinner_item)
            spinner.adapter = adapter
        }
    }

    private fun updateActionBar(activity: AppCompatActivity){
        activity.supportActionBar?.title = ACTION_BAR_TITLE
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.action_bar_enabled, menu)
    }

    companion object {
        const val ACTION_BAR_TITLE = "Новая книга"
        fun newInstance(): AddingBookFragment = AddingBookFragment()
    }
}
