package ru.hse.egorov.reading_tracker.ui.adapter

import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
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
import ru.hse.egorov.reading_tracker.ui.MainActivity.Companion.SESSION_FRAGMENT_POSITION
import ru.hse.egorov.reading_tracker.ui.bitmap.BitmapEncoder
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment.Book
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher


class LibraryAdapter : RecyclerView.Adapter<LibraryAdapter.LibraryViewHolder>() {
    private val library = ArrayList<Book>()
    private val dbManager = DatabaseManager()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book, parent, false)
        return LibraryViewHolder(view)
    }

    fun set(books: Collection<Book>) {
        library.addAll(books)
        notifyDataSetChanged()
    }

    fun add(book: Book) {
        library.add(book)
        notifyDataSetChanged()
    }

    fun get(position: Int): Book {
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

    fun replaceItem(position: Int, newBook: Book) {
        library[position] = newBook
        notifyDataSetChanged()
    }

    fun restoreItem(book: Book, position: Int) {
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

    inner class LibraryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), BitmapEncoder, FragmentLauncher {
        private val author: TextView? = itemView.author
        private val name: TextView? = itemView.title
        private val cover: ImageView? = itemView.cover
        private val progressBar: ProgressBar? = itemView.progressBar
        private val container = itemView.bookContainer

        fun bind(book: Book) {
            author?.text = book.author
            name?.text = book.name

            container.setOnClickListener {
                openPagerFragment(it.context as AppCompatActivity, SESSION_FRAGMENT_POSITION)
                //TODO transfer data to book
            }

            progressBar?.visibility = View.VISIBLE
            if (book.cover == null) {
                dbManager.getBookCover(book.id, cover!!.context).listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                                 dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        progressBar?.visibility = View.GONE
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        progressBar?.visibility = View.GONE
                        return false
                    }

                }).into(cover)
            } else {
                dbManager.getBookCoverFromURL(book.cover, cover!!.context).listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                                 dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        progressBar?.visibility = View.GONE
                        return false
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        progressBar?.visibility = View.GONE
                        return false
                    }

                }).into(cover)
            }
        }
    }
}