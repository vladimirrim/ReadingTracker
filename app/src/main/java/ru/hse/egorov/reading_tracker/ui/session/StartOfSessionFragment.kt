package ru.hse.egorov.reading_tracker.ui.session

import android.app.ActionBar
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_start_of_session.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.book_library.AddingBookFragment
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.session.session_inner_fragments.AutoSessionTimeChangeFragment


class StartOfSessionFragment : Fragment(), FragmentLauncher {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_start_of_session, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpActionBar()

        view.addBook.setOnClickListener {
            openFragment(AddingBookFragment.newInstance(), activity as AppCompatActivity, R.id.fragment)
        }

        openInnerFragment(AutoSessionTimeChangeFragment.newInstance(), this, R.id.sessionFragment)
    }

    private fun setUpActionBar() {
        val supportedActivity = activity as AppCompatActivity
        supportedActivity.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        supportedActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportedActivity.supportActionBar?.title = ACTION_BAR_TITLE
        supportedActivity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)
    }


    companion object {
        fun newInstance() = StartOfSessionFragment()
        private const val ACTION_BAR_TITLE = "Новая запись о чтении"
    }
}
