package ru.hse.egorov.reading_tracker.ui.statistics


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_sessions_statistics.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.action_bar.ActionBarSetter
import ru.hse.egorov.reading_tracker.ui.adapter.SessionAdapter

class SessionsStatisticsFragment : Fragment(), ActionBarSetter {
    override fun setActionBar(activity: AppCompatActivity) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_sessions_statistics, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSessions(view)
    }

    private fun setUpSessions(view: View) {
        view.sessions.isNestedScrollingEnabled = false
        view.sessions.layoutManager = LinearLayoutManager(context)
        view.sessions.adapter = sessionAdapter
    }

    companion object {
        private val sessionAdapter = SessionAdapter()
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         * */
        fun newInstance() = SessionsStatisticsFragment()

        fun getAdapter() = sessionAdapter
    }
}
