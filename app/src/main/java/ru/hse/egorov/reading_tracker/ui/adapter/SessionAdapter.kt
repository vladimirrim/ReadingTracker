package ru.hse.egorov.reading_tracker.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import ru.hse.egorov.reading_tracker.R

class SessionAdapter : RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {
    private var date: TextView? = null
    private var time: TextView? = null
    private var ratingBar:RatingBar? =null
    private val userSessions = ArrayList<Session>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_session, parent, false)
        return SessionViewHolder(view)
    }

    fun set(sessions: Collection<Session>) {
        userSessions.addAll(sessions)
    }

    fun get(): ArrayList<Session> {
        return userSessions
    }

    override fun getItemCount(): Int {
        return userSessions.size
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.bind(userSessions[position])
    }

    inner class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            date = itemView.findViewById(R.id.date)
            time = itemView.findViewById(R.id.time)
            ratingBar = itemView.findViewById(R.id.ratingBar)
        }

        fun bind(session:Session) {
            date?.text = session.date
            time?.text = session.time
            ratingBar?.rating = session.rating
        }
    }

    data class Session(val date:String, val time:String, val rating:Float)
}