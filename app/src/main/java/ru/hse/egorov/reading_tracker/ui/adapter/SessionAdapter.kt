package ru.hse.egorov.reading_tracker.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.session.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.session.EndOfSessionFragment.Companion.Mood
import ru.hse.egorov.reading_tracker.ui.session.EndOfSessionFragment.Companion.Place
import java.util.*

class SessionAdapter : RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {
    private val sessions = ArrayList<Session>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.session, parent, false)
        return SessionViewHolder(view)
    }

    fun set(sessions: Collection<Session>) {
        this.sessions.addAll(sessions)
        notifyDataSetChanged()
    }

    fun get(): ArrayList<Session> {
        return sessions
    }

    override fun getItemCount(): Int {
        return sessions.size
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.bind(sessions[position])
    }

    inner class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var date = itemView.date
        private var dayOfTheWeek = itemView.dayOfTheWeek
        private var minutes = itemView.minutes
        private var hours = itemView.hours
        private var author = itemView.author
        private var title = itemView.title
        private var place = itemView.placeFlag
        private var comment = itemView.commentFlag
        private var emotion = itemView.emotionFlag

        fun bind(session: Session) {
            date.text = session.date.get(Calendar.MONTH).toString()
            dayOfTheWeek.text = session.date.get(Calendar.DAY_OF_WEEK).toString()
            minutes.text = (session.time % 60).toString()
            hours.text = (session.time / 60).toString()
            author.text = session.author
            title.text = session.title
            if (session.comment == null) {
                comment.visibility = View.GONE
            }
            if (session.place == null) {
                place.visibility = View.GONE
            }
            if (session.emotion == null) {
                emotion.visibility = View.GONE
            }
        }
    }

    companion object {
        data class Session(val date: Calendar, val time: Int, val emotion: Mood?, val place: Place?, val author: String, val comment: String?, val title: String)
    }
}