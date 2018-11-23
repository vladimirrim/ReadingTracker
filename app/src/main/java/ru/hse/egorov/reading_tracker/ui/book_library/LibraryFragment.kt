package ru.hse.egorov.reading_tracker.ui.book_library

import android.graphics.*
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_library.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.adapter.LibraryAdapter
import ru.hse.egorov.reading_tracker.ui.bitmap.BitmapEncoder
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher


class LibraryFragment : Fragment(), BitmapEncoder, FragmentLauncher {
    private val dbManager = DatabaseManager()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_library, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpActionBar()
        setUpLibrary(view)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.getItem(0)?.isVisible = false
    }

    private fun setUpLibrary(view: View) {
        view.library.layoutManager = LinearLayoutManager(context)
        enableSwipe(view.library, libraryAdapter)
        view.library.adapter = libraryAdapter
    }

    private fun setUpActionBar() {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        (activity as AppCompatActivity).supportActionBar?.setCustomView(R.layout.action_bar)
    }

    private fun enableSwipe(library: RecyclerView, libraryAdapter: LibraryAdapter) {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val selectedBook = libraryAdapter.get(position)

                if (direction == ItemTouchHelper.RIGHT) {
                    libraryAdapter.removeItem(position)
                    val snackbar = Snackbar.make(activity!!.placeSnackBar,
                            "Нажмите, чтобы восстановить", Snackbar.LENGTH_LONG)
                    snackbar.setAction("UNDO") {
                        libraryAdapter.restoreItem(selectedBook, position)
                    }
                    snackbar.addCallback(object : Snackbar.Callback() {

                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            super.onDismissed(transientBottomBar, event)

                            if (event != DISMISS_EVENT_ACTION)
                                dbManager.deleteBookFromLibrary(selectedBook.id).addOnSuccessListener {
                                    Log.d(TAG, "Successful deletion of book ${selectedBook.name}")
                                }
                        }
                    })
                    snackbar.setActionTextColor(Color.YELLOW)
                    snackbar.show()
                } else {
                    val dispatchFragment = EditBookFragment.newInstance()
                    val bundle = Bundle()
                    setUpBundle(bundle, selectedBook, position)
                    dispatchFragment.arguments = bundle
                    openTemporaryFragment(activity as AppCompatActivity, dispatchFragment, R.id.temporaryFragment)
                    (activity as AppCompatActivity).fab.hide()
                }
            }

            private fun setUpBundle(bundle: Bundle, selectedBook: Book, position: Int) {
                bundle.putParcelable("cover", selectedBook.cover)
                bundle.putString("title", selectedBook.name)
                bundle.putString("author", selectedBook.author)
                bundle.putString("bookId", selectedBook.id)
                bundle.putInt("media", selectedBook.mediaType)
                bundle.putInt("bookPosition", position)
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                val icon: Bitmap
                val paint = Paint()
                val iconDestination: RectF
                val background: RectF

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val width = itemView.height.toFloat() / 3
                    paint.color = Color.parseColor("#FFFFFF")

                    if (dX > 0) {
                        background = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                        icon = getBitmap(resources.getDrawable(R.drawable.ic_delete, context?.theme))
                        iconDestination = RectF(itemView.left.toFloat() + width, itemView.top.toFloat() + width, itemView.left.toFloat() + 2 * width, itemView.bottom.toFloat() - width)
                    } else {
                        background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                        icon = getBitmap(resources.getDrawable(R.drawable.ic_edit, context?.theme))
                        iconDestination = RectF(itemView.right.toFloat() - 2 * width, itemView.top.toFloat() + width, itemView.right.toFloat() - width, itemView.bottom.toFloat() - width)
                    }
                    c.drawRect(background, paint)
                    c.drawBitmap(icon, null, iconDestination, paint)
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(library)
    }

    companion object {
        private const val TAG = "Library"
        fun newInstance() = LibraryFragment()
        private val libraryAdapter = LibraryAdapter()
        fun getAdapter() = libraryAdapter
    }

    data class Book(var author: String, var name: String, var id: String, var mediaType: Int, var cover: Bitmap?)
}