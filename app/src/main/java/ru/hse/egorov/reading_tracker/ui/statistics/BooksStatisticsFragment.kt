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

class BooksStatisticsFragment : Fragment() {
    private val statsManager = StatisticsManager()
    private val bookStatisticsAdapter = BookStatisticsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_books_statistics, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBooks(view)
    }

    private fun setUpBooks(view: View) {
        view.books.isNestedScrollingEnabled = false
        view.books.layoutManager = LinearLayoutManager(context)
        bookStatisticsAdapter.set(statsManager.getBookStatistics())
        view.books.adapter = bookStatisticsAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         * */
        fun newInstance() = BooksStatisticsFragment()
    }
}