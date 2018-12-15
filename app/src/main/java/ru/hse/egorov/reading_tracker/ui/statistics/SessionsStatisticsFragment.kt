package ru.hse.egorov.reading_tracker.ui.statistics


import android.graphics.*
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_sessions_statistics.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.adapter.SessionAdapter
import ru.hse.egorov.reading_tracker.ui.bitmap.BitmapEncoder

class SessionsStatisticsFragment : Fragment(), BitmapEncoder {
    private val dbManager = DatabaseManager()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_sessions_statistics, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSessions()
    }

    private fun setUpSessions() {
        sessions.isNestedScrollingEnabled = false
        sessions.layoutManager = LinearLayoutManager(context)
        sessions.adapter = sessionAdapter
        enableSwipe(sessions, sessionAdapter)
    }

    private fun enableSwipe(sessions: RecyclerView, sessionsAdapter: SessionAdapter) {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val selectedSession = sessionsAdapter.get(position)

                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
                    sessionsAdapter.removeItem(position)
                    val snackbar = Snackbar.make(placeSnackBar,
                            "Нажмите, чтобы восстановить", Snackbar.LENGTH_LONG)
                    snackbar.setAction("UNDO") {
                        sessionsAdapter.restoreItem(selectedSession, position)
                    }
                    snackbar.addCallback(object : Snackbar.Callback() {

                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            super.onDismissed(transientBottomBar, event)

                            if (event != DISMISS_EVENT_ACTION)
                                dbManager.deleteSession(selectedSession.sessionId).addOnSuccessListener {
                                    Log.d(TAG,
                                            "Successful deletion of book ${selectedSession.sessionId}")
                                }
                        }
                    })
                    snackbar.setActionTextColor(Color.YELLOW)
                    snackbar.show()
                }
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView,
                                     viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                                     actionState: Int, isCurrentlyActive: Boolean) {
                val icon = getBitmap(resources.getDrawable(R.drawable.ic_delete, context?.theme))
                val paint = Paint()
                val iconDestination: RectF
                val background: RectF

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder.itemView
                    val width = itemView.height.toFloat() / 3
                    paint.color = Color.parseColor("#FFFFFF")
                    if (dX > 0) {
                        background = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX,
                                itemView.bottom.toFloat())
                        iconDestination = RectF(itemView.left.toFloat() + width,
                                itemView.top.toFloat() + width,
                                itemView.left.toFloat() + 2 * width,
                                itemView.bottom.toFloat() - width)
                    } else {
                        background = RectF(itemView.right.toFloat() + dX,
                                itemView.top.toFloat(), itemView.right.toFloat(),
                                itemView.bottom.toFloat())
                        iconDestination = RectF(itemView.right.toFloat() - 2 * width,
                                itemView.top.toFloat() + width,
                                itemView.right.toFloat() - width,
                                itemView.bottom.toFloat() - width)
                    }
                    c.drawRect(background, paint)
                    c.drawBitmap(icon, null, iconDestination, paint)
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(sessions)
    }

    companion object {
        private val sessionAdapter = SessionAdapter()
        private const val TAG = "Sessions Statistics"
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         * */
        fun newInstance() = SessionsStatisticsFragment()

        fun getAdapter() = sessionAdapter
    }
}
