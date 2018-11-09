package ru.hse.egorov.reading_tracker.ui.book_library

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_library.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.adapter.LibraryAdapter


class LibraryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_library, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.library.layoutManager = LinearLayoutManager(context)
        val libraryAdapter = LibraryAdapter()
        libraryAdapter.set(getLibrary())
        view.library.adapter = libraryAdapter
        libraryAdapter.notifyDataSetChanged()
    }

    private fun getLibrary(): Collection<Book> {
        val list = ArrayList<Book>()
        list.add(Book("Тур Хейердал", "Биоцентризм.Как жизнь создает вселенную",
                getBitmap(context!!, R.drawable.ic_stab_cover)))
        list.add(Book("Роберт Ланца", "Фату-ХиваЖвозврат к природе",
                getBitmap(context!!, R.drawable.ic_stab_cover)))
        return list
    }

    private fun getBitmap(vectorDrawable: VectorDrawable): Bitmap {
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    private fun getBitmap(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        return when (drawable) {
            is BitmapDrawable -> BitmapFactory.decodeResource(context.resources, drawableId)
            is VectorDrawable -> getBitmap((drawable as VectorDrawable?)!!)
            else -> throw IllegalArgumentException("unsupported drawable type")
        }
    }

    companion object {
        fun newInstance(): LibraryFragment = LibraryFragment()
    }

    data class Book(var author: String, var name: String, var cover: Bitmap)
}