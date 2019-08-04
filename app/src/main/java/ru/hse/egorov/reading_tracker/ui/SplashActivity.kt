package ru.hse.egorov.reading_tracker.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ReadingTrackerApplication
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.statistics.StatisticsManager
import ru.hse.egorov.reading_tracker.ui.adapter.LibraryAdapter
import ru.hse.egorov.reading_tracker.ui.bitmap.BitmapEncoder
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment
import ru.hse.egorov.reading_tracker.ui.login.SignInActivity
import ru.hse.egorov.reading_tracker.ui.statistics.BooksStatisticsFragment
import ru.hse.egorov.reading_tracker.ui.statistics.OverallStatisticsFragment
import ru.hse.egorov.reading_tracker.ui.statistics.SessionsStatisticsFragment
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap


class SplashActivity : AppCompatActivity(), BitmapEncoder {
    private val db = DatabaseManager()
    private val statsManager = StatisticsManager()
    @Inject
    lateinit var library: LibraryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashScreenTheme)
        super.onCreate(savedInstanceState)

        (application as ReadingTrackerApplication).appComponent.inject(this)
        clearData()
        chooseActivity()
    }

    private fun chooseActivity() {
        if (db.isAuth()) {
            db.getLibrary().addOnSuccessListener {
                val bookMap = HashMap<String, Pair<String, String>>()
                for (book in it.documents) {
                    if (book["is deleted"] == null) {
                        library.add(LibraryFragment.Book(book["author"] as String?, book["title"] as String, book.id,
                                book["media"] as String, null, book["last updated"] as Date, (book["page count"] as Long?)?.toInt()))
                    }
                    bookMap[book.id] = Pair(book["author"] as String, book["title"] as String)
                }
                statsManager.setUpSessions(bookMap) {
                    val intent = Intent(this,
                            MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                library.sortByLastUpdated()
            }
        } else {
            val intent = Intent(this,
                    SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun clearData() {
        OverallStatisticsFragment.getAllSessions().clear()
        library.clear()
        OverallStatisticsFragment.getSessionsForPeriod().clear()
        SessionsStatisticsFragment.getAdapter().clear()
        BooksStatisticsFragment.getAdapter().clear()
    }
}
