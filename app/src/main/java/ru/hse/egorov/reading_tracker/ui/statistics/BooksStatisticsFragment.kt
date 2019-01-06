package ru.hse.egorov.reading_tracker.ui.statistics

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_books_statistics.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.statistics.StatisticsManager
import ru.hse.egorov.reading_tracker.ui.adapter.BookStatisticsAdapter
import ru.hse.egorov.reading_tracker.ui.adapter.BookStatisticsAdapter.Companion.BookStatistics

class BooksStatisticsFragment : Fragment(), StatisticsUpdater {
    private val statsManager = StatisticsManager()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_books_statistics, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBooks(view)
    }

    override fun updateStatistics() {
        bookStatisticsAdapter.clear()
        val bookStatisticsMap = HashMap<String, BookStatistics>()
        for (session in OverallStatisticsFragment.getSessionsForPeriod()) {
            bookStatisticsMap[session.bookId] = statsManager.updateBookStatistics(bookStatisticsMap.getOrDefault(session.bookId,
                    BookStatistics(session.title, session.author, 0, 0, 0)),
                    session)
        }
        bookStatisticsAdapter.set(bookStatisticsMap.values)
    }

    private fun setUpBooks(view: View) {
        view.books.isNestedScrollingEnabled = false
        view.books.layoutManager = LinearLayoutManager(context)
        view.books.adapter = bookStatisticsAdapter
    }

    companion object {
        private val bookStatisticsAdapter = BookStatisticsAdapter()
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         * */
        fun newInstance() = BooksStatisticsFragment()

        fun getAdapter() = bookStatisticsAdapter
    }
}