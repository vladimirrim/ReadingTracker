package ru.hse.egorov.reading_tracker.ui.adapter

import android.content.Context
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import kotlinx.android.synthetic.main.book_info.view.*
import ru.hse.egorov.reading_tracker.R
import java.util.*


class SearchCompletionAdapter(private val context: Context) : BaseAdapter(), Filterable {

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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val curView = convertView
                ?: LayoutInflater.from(context).inflate(R.layout.book_info, parent, false)

        val book = books[position]
        curView.bookName.text = book.bookName
        curView.authorName.text = book.authorName
        return curView
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