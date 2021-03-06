package ru.hse.egorov.reading_tracker.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.book_statistics.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import java.util.*

class BookStatisticsAdapter : RecyclerView.Adapter<BookStatisticsAdapter.BookStatisticsHolder>() {
    private val books = ArrayList<BookStatistics>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookStatisticsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_statistics, parent, false)
        return BookStatisticsHolder(view)
    }

    fun set(sessions: Collection<BookStatistics>) {
        this.books.addAll(sessions)
        notifyDataSetChanged()
    }

    fun get(): ArrayList<BookStatistics> {
        return books
    }

    fun clear() {
        books.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: BookStatisticsHolder, position: Int) {
        holder.bind(books[position])
    }

    class BookStatisticsHolder(itemView: View) : RecyclerView.ViewHolder(itemView), FragmentLauncher {
        private val title = itemView.title
        private val author = itemView.author
        private val hours = itemView.hours
        private val minutes = itemView.minutes
        private val sessionsCount = itemView.sessionsCount
        private val sessionCountStatic = itemView.sessionsCountStatic

        fun bind(book: BookStatistics) {
            title.text = book.title
            author.text = book.author
            hours.text = book.hours.toString()
            minutes.text = book.minutes.toString()
            sessionsCount.text = book.sessionsCount.toString()
            sessionCountStatic.text = title.resources.getQuantityString(R.plurals.sessions_count_plurals, book.sessionsCount)
        }
    }

    companion object {
        data class BookStatistics(val title: String, val author: String?, var hours: Int, var minutes: Int,
                                  var sessionsCount: Int)
    }
}