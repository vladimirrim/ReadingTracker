package ru.hse.egorov.reading_tracker.ui.adapter

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.book.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.bitmap.BitmapEncoder
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher

abstract class BookAdapter : RecyclerView.Adapter<BookAdapter.LibraryViewHolder>() {
    protected val library = ArrayList<LibraryFragment.Book>()
    protected val dbManager = DatabaseManager()


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

    fun clear() {
        library.clear()
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        library.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, library.size)
    }

    fun replaceItem(position: Int, newBook: LibraryFragment.Book) {
        library[position] = newBook
        notifyDataSetChanged()
    }

    fun restoreItem(book: LibraryFragment.Book, position: Int) {
        library.add(position, book)
        notifyItemInserted(position)
    }

    fun sortByLastUpdated() {
        library.sortByDescending {
            it.lastUpdated
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return library.size
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        holder.bind(library[position])
    }

    protected abstract fun bindContainer(container: View, book: LibraryFragment.Book)

    protected fun setBundle(book: LibraryFragment.Book): Bundle {
        val bundle = Bundle()
        bundle.putString("author", book.author)
        bundle.putString("title", book.name)
        bundle.putString("bookId", book.id)
        bundle.putString("coverURL", book.cover)
        bundle.putString("media", book.mediaType)
        book.pageCount?.apply { bundle.putInt("pageCount", this) }
        return bundle
    }

    inner class LibraryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), BitmapEncoder, FragmentLauncher, RequestListener<Drawable> {
        private val author: TextView? = itemView.author
        private val name: TextView? = itemView.title
        private val cover: ImageView? = itemView.cover
        private val progressBar: ProgressBar? = itemView.progressBar
        private val container = itemView.bookContainer

        fun bind(book: LibraryFragment.Book) {
            author?.text = book.author
            name?.text = book.name
            bindContainer(container, book)

            progressBar?.visibility = View.VISIBLE
            if (book.cover == null) {
                dbManager.getBookCover(book.id, cover!!.context).listener(this).into(cover)
            } else {
                dbManager.getBookCoverFromURL(book.cover, cover!!.context).listener(this).into(cover)
            }
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            progressBar?.visibility = View.GONE
            return false
        }

        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            progressBar?.visibility = View.GONE
            return false
        }
    }
}