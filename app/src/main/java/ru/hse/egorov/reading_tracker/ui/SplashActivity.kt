package ru.hse.egorov.reading_tracker.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.statistics.StatisticsManager
import ru.hse.egorov.reading_tracker.ui.bitmap.BitmapEncoder
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment
import ru.hse.egorov.reading_tracker.ui.login.SignUpSignInActivity
import ru.hse.egorov.reading_tracker.ui.statistics.SessionsStatisticsFragment
import java.util.*
import kotlin.collections.HashMap


class SplashActivity : AppCompatActivity(), BitmapEncoder {
    private val db = DatabaseManager()
    private val statsManager = StatisticsManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashScreenTheme)
        super.onCreate(savedInstanceState)

        chooseActivity()
    }

    private fun chooseActivity() {
        if (db.isAuth()) {
            db.getLibrary().addOnSuccessListener {
                val bookMap = HashMap<String, Pair<String, String>>()
                val libraryAdapter = LibraryFragment.getAdapter()
                for (book in it.documents) {
                    libraryAdapter.add(LibraryFragment.Book(book["author"] as String, book["title"] as String, book.id,
                            book["media"] as String, null, book["last updated"] as Date))
                    bookMap[book.id] = Pair(book["author"] as String, book["title"] as String)
                }
                setUpSessions(bookMap)
                libraryAdapter.sortByLastUpdated()
            }
        } else {
            val intent = Intent(this,
                    SignUpSignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setUpSessions(bookMap: HashMap<String, Pair<String, String>>) {
        statsManager.getSessions().addOnSuccessListener {
            val sessionAdapter = SessionsStatisticsFragment.getAdapter()
            it.documents.forEach { session ->
                val id = session["book id"] as String
                sessionAdapter.add(statsManager.wrapSession(session, bookMap[id]!!.first, bookMap[id]!!.second))
            }
            sessionAdapter.sortByDate()
        }
        val intent = Intent(this,
                MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}
