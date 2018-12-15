package ru.hse.egorov.reading_tracker.statistics

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import ru.hse.egorov.reading_tracker.ui.adapter.BookStatisticsAdapter
import ru.hse.egorov.reading_tracker.ui.adapter.SessionAdapter.Companion.Session
import ru.hse.egorov.reading_tracker.ui.session.EndOfSessionFragment.Companion.Place
import ru.hse.egorov.reading_tracker.ui.session.EndOfSessionFragment.Companion.Mood
import java.util.*

class StatisticsManager {
    private val db = FirebaseFirestore.getInstance(authManager.app)

    fun getSessions(): Task<QuerySnapshot> {
        return db.collection("statistics").document("sessions").collection(authManager.uid as String).get()
    }

    fun wrapSession(session: DocumentSnapshot, author: String, title: String): Session {
        val startTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()
        startTime.time = session["start time"] as Date
        endTime.time = session["end time"] as Date
        return Session(startTime, endTime, (session["duration"] as Long).toInt(), Mood.getMoodByName(session["mood"] as String?),
                Place.getPlaceByName(session["place"] as String?), author, session["comment"] as String?, title)
    }

    fun getBookStatistics(): ArrayList<BookStatisticsAdapter.Companion.BookStatistics> {
        val list = ArrayList<BookStatisticsAdapter.Companion.BookStatistics>()

        list.add(BookStatisticsAdapter.Companion.BookStatistics("kok", "kek", 1, 40, 20))
        list.add(BookStatisticsAdapter.Companion.BookStatistics("kok", "kek", 1, 41, 20))
        list.add(BookStatisticsAdapter.Companion.BookStatistics("kok", "kek", 1, 42, 20))
        list.add(BookStatisticsAdapter.Companion.BookStatistics("kok", "kek", 1, 42, 20))

        return list
    }

    companion object {
        private val authManager = FirebaseAuth.getInstance()
    }
}