package ru.hse.egorov.reading_tracker.ui.statistics


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_sessions_statistics.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.statistics.StatisticsManager
import ru.hse.egorov.reading_tracker.ui.adapter.SessionAdapter

class SessionsStatisticsFragment : Fragment() {
    private val statsManager = StatisticsManager()
    private val sessionAdapter = SessionAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_sessions_statistics, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSessions(view)
    }

    private fun setUpSessions(view: View) {
        view.sessions.layoutManager = LinearLayoutManager(context)
        sessionAdapter.set(statsManager.getSessions())
        view.sessions.adapter = sessionAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         * */
        fun newInstance() = SessionsStatisticsFragment()
    }
}
