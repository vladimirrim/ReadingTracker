package ru.hse.egorov.reading_tracker.ui.session

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_session_library.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.statistics.StatisticsManager
import ru.hse.egorov.reading_tracker.ui.adapter.SessionAdapter

class SessionLibraryActivity : AppCompatActivity() {
    private val userSessionAdapter = SessionAdapter()
    private val statsMan = StatisticsManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_library)

        sessionList.adapter = userSessionAdapter
        sessionList.layoutManager = LinearLayoutManager(this)
        userSessionAdapter.set(statsMan.getUserSessions())
    }
}
