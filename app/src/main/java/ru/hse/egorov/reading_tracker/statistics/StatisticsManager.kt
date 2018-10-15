package ru.hse.egorov.reading_tracker.statistics

import ru.hse.egorov.reading_tracker.ui.adapter.SessionAdapter

class StatisticsManager {

    fun getUserSessions():Collection<SessionAdapter.Session>{
        //TODO get sessions from database
        return arrayListOf(SessionAdapter.Session("пн, 8 окт","8:10",4.7f),
                SessionAdapter.Session("вт, 9 окт","3:40",3.6f) )
    }
}