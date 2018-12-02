package ru.hse.egorov.reading_tracker.ui.adapter

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.session.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.MainActivity.Companion.SESSION_FRAGMENT_POSITION
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
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

    inner class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), FragmentLauncher {
        private val date = itemView.date
        private val dayOfTheWeek = itemView.dayOfTheWeek
        private val minutes = itemView.minutes
        private val hours = itemView.hours
        private val author = itemView.author
        private val title = itemView.title
        private val place = itemView.placeFlag
        private val comment = itemView.commentFlag
        private val emotion = itemView.emotionFlag
        private val container = itemView.sessionContainer

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

            container.setOnClickListener {
                //TODO
            }
        }
    }

    companion object {
        data class Session(val date: Calendar, val time: Int, val emotion: Mood?, val place: Place?, val author: String, val comment: String?, val title: String)
    }
}