package ru.hse.egorov.reading_tracker.statistics

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import ru.hse.egorov.reading_tracker.ui.adapter.BookStatisticsAdapter.Companion.BookStatistics
import ru.hse.egorov.reading_tracker.ui.adapter.SessionAdapter.Companion.Session
import ru.hse.egorov.reading_tracker.ui.session.EndOfSessionFragment.Companion.Mood
import ru.hse.egorov.reading_tracker.ui.session.EndOfSessionFragment.Companion.Place
import ru.hse.egorov.reading_tracker.ui.statistics.BooksStatisticsFragment
import ru.hse.egorov.reading_tracker.ui.statistics.OverallStatisticsFragment
import ru.hse.egorov.reading_tracker.ui.statistics.SessionsStatisticsFragment
import java.util.*

class StatisticsManager {
    private val db = FirebaseFirestore.getInstance(authManager.app)

    fun wrapSession(session: Map<String, Any?>, sessionId: String, author: String?, title: String): Session {
        val startTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()
        startTime.time = session["start time"] as Date
        endTime.time = session["end time"] as Date
        return Session(startTime, endTime,
                (session["duration"] as Long).toInt(), (session["start page"] as Long).toInt(),
                (session["end page"] as Long).toInt(), Mood.getMoodByName(session["mood"] as String?),
                Place.getPlaceByName(session["place"] as String?), author, session["comment"] as String?,
                title, sessionId, session["book id"] as String)
    }

    fun setUpSessions(bookMap: HashMap<String, Pair<String, String>>, onFinish: () -> Unit) {
        getSessions().addOnSuccessListener {
            val sessionAdapter = SessionsStatisticsFragment.getAdapter()
            val bookStatisticsAdapter = BooksStatisticsFragment.getAdapter()
            val bookStatisticsMap = HashMap<String, BookStatistics>()
            it.documents.forEach { session ->
                val id = session["book id"] as String
                val sessionData = wrapSession(session.data!!, session.id, bookMap[id]!!.first, bookMap[id]!!.second)
                bookStatisticsMap[id] = updateBookStatistics(bookStatisticsMap.getOrDefault(id,
                        BookStatistics(sessionData.title, sessionData.author, 0, 0, 0)),
                        sessionData)
                sessionAdapter.add(sessionData)
            }
            sessionAdapter.sortByDate()
            OverallStatisticsFragment.getAllSessions().addAll(sessionAdapter.getCopy())
            OverallStatisticsFragment.getSessionsForPeriod().addAll(sessionAdapter.getCopy())
            bookStatisticsAdapter.set(bookStatisticsMap.values)
            onFinish()
        }
    }

    fun updateBookStatistics(bookStatistics: BookStatistics, session: Session): BookStatistics {
        bookStatistics.sessionsCount++
        bookStatistics.minutes += session.duration / 60
        bookStatistics.hours += bookStatistics.minutes / 60
        bookStatistics.minutes %= 60
        return bookStatistics
    }

    private fun getSessions(): Task<QuerySnapshot> {
        return db.collection("statistics").document("sessions").collection(authManager.uid as String).get()
    }

    companion object {
        private val authManager = FirebaseAuth.getInstance()
    }
}