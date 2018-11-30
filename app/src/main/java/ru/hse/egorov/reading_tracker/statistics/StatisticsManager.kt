package ru.hse.egorov.reading_tracker.statistics

import ru.hse.egorov.reading_tracker.ui.adapter.SessionAdapter
import ru.hse.egorov.reading_tracker.ui.session.EndOfSessionFragment
import java.util.*

class StatisticsManager {
    fun getSessions(): ArrayList<SessionAdapter.Companion.Session> {
        val list = ArrayList<SessionAdapter.Companion.Session>()

        list.add(SessionAdapter.Companion.Session(Calendar.getInstance(), 61, EndOfSessionFragment.Companion.Mood.HAPPY, EndOfSessionFragment.Companion.Place.WORK,
                "kok", "kek", "le"))
        list.add(SessionAdapter.Companion.Session(Calendar.getInstance(), 61, null, EndOfSessionFragment.Companion.Place.TRANSPORT,
                "kok", null, "le"))

        list.add(SessionAdapter.Companion.Session(Calendar.getInstance(), 61, null, EndOfSessionFragment.Companion.Place.TRANSPORT,
                "kok", null, "le"))

        return list
    }
}