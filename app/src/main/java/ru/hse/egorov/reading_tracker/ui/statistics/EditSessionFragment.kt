package ru.hse.egorov.reading_tracker.ui.statistics


import android.app.ActionBar
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.action_bar.ActionBarSetter


class EditSessionFragment : Fragment(), ActionBarSetter {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_edit_session, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        setActionBar(view.context as AppCompatActivity)
    }

    override fun setActionBar(activity: AppCompatActivity) {
        setHasOptionsMenu(true)
        activity.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.title = ACTION_BAR_TITLE
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.edit_session_action_bar, menu)
    }

    companion object {
        private const val ACTION_BAR_TITLE = "Запись о чтении"

        fun newInstance() = EditSessionFragment()
    }
}
