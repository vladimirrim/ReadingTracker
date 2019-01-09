package ru.hse.egorov.reading_tracker.ui.adapter

import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import ru.hse.egorov.reading_tracker.R
import java.util.*


class SearchCompletionAdapter(private val context: Context) : BaseAdapter(), Filterable {
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val filter = ItemFilter()
    private val books = java.util.ArrayList<Book>(MAX_BOOK_LIST_SIZE)

    override fun getCount(): Int {
        return books.size
    }

    override fun getItem(position: Int): Any {
        return books[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    class ViewHolder {
        internal var tvTitle: TextView? = null
    }

    override fun getFilter(): Filter = filter

    private inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            TODO("not implemented")
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            Collections.copy(books, results.values as List<Book>)
            notifyDataSetChanged()
        }
    }

    companion object {
        const val MAX_BOOK_LIST_SIZE = 10
    }

    data class Book(val bookName: String, val authorName: String)
}