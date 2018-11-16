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

    fun add(book: LibraryFragment.Book) {
        library.add(book)
        notifyDataSetChanged()
    }

    fun get(position: Int): LibraryFragment.Book {
        return library[position]
    }

    fun removeItem(position: Int) {
        library.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, library.size)
    }

    fun restoreItem(book: LibraryFragment.Book, position: Int) {
        library.add(position, book)
        notifyItemInserted(position)
    }

    override fun getItemCount(): Int {
        return library.size
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        holder.bind(library[position])
    }

    inner class LibraryViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var author: TextView? = itemView?.findViewById(R.id.author)
        var name: TextView? = itemView?.findViewById(R.id.title)
        var cover: ImageView? = itemView?.findViewById(R.id.cover)

        fun bind(book: LibraryFragment.Book) {
            author?.text = book.author
            name?.text = book.name
            cover?.setImageBitmap(book.cover)
        }
    }
}