package ru.hse.egorov.reading_tracker.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment


class LibraryAdapter : RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {

    private val library = ArrayList<LibraryFragment.Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book, parent, false)
        return LibraryViewHolder(view)
    }

    fun set(books: Collection<LibraryFragment.Book>) {
        library.addAll(books)
        notifyDataSetChanged()
    }

    fun get(): ArrayList<LibraryFragment.Book> {
        return library
    }

    override fun getItemCount(): Int {
        return library.size
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        holder.bind(library[position])
    }

    inner class LibraryViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var author: TextView? = itemView?.findViewById(R.id.comment)
        var name: TextView? = itemView?.findViewById(R.id.name)
        var cover: ImageView? = itemView?.findViewById(R.id.cover)

        fun bind(book: LibraryFragment.Book) {
            author?.text = book.author
            name?.text = book.name
            cover?.setImageBitmap(book.cover)
        }
    }
}