package ru.hse.egorov.reading_tracker.ui.adapter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.session.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.date.DateTranslator
import ru.hse.egorov.reading_tracker.ui.date.DateTranslator.Companion.MONTH_GENITIVE
import ru.hse.egorov.reading_tracker.ui.date.DateTranslator.Companion.WEEK_FULL
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.session.EndOfSessionFragment.Companion.Mood
import ru.hse.egorov.reading_tracker.ui.session.EndOfSessionFragment.Companion.Place
import ru.hse.egorov.reading_tracker.ui.statistics.edit_session.EditSessionFragment
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

    fun add(session: Session) {
        sessions.add(session)
        notifyDataSetChanged()
    }

    fun get(position: Int): Session {
        return sessions[position]
    }

    fun getCopy(): MutableList<Session> {
        return ArrayList(sessions)
    }

    fun clear() {
        sessions.clear()
        notifyDataSetChanged()
    }

    fun restoreItem(book: Session, position: Int) {
        sessions.add(position, book)
        notifyItemInserted(position)
    }

    fun removeItem(position: Int) {
        sessions.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, sessions.size)
    }

    fun sortByDate() {
        sessions.sortByDescending {
            it.startTime
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return sessions.size
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.bind(sessions[position])
    }

    class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), FragmentLauncher, DateTranslator {
        private val date = itemView.date
        private val dayOfTheWeek = itemView.dayOfTheWeek
        private val minutes = itemView.minutes
        private val hours = itemView.hours
        private val author = itemView.author
        private val title = itemView.title
        private val place = itemView.placeFlag
        private val comment = itemView.commentFlag
        private val emotion = itemView.emotionFlag
        private val container = itemView

        fun bind(session: Session) {
            setDate(session.startTime.get(Calendar.MONTH), session.startTime.get(Calendar.DAY_OF_MONTH))
            setDayOfTheWeek(session.startTime.get(Calendar.DAY_OF_WEEK))
            minutes.text = ((session.duration / 60) % 60).toString()
            if (session.duration < 60) minutes.text = "1"
            hours.text = (session.duration / 60 / 60).toString()
            if (hours.text == "0") {
                itemView.hoursStatic.visibility = View.INVISIBLE
                hours.text = ""
            } else {
                itemView.hoursStatic.visibility = View.VISIBLE
            }
            author.text = session.author
            title.text = session.title
            var commentText = ""
            var placeText = ""
            var emotionText = ""
            if (session.comment!!.isEmpty()) {
                comment.visibility = View.GONE
            } else {
                comment.visibility = View.VISIBLE
                commentText = session.comment!!
            }
            if (session.place == null) {
                place.visibility = View.GONE
            } else {
                place.visibility = View.VISIBLE
                setPlace(session.place!!)
                placeText = session.place.toString()
            }
            if (session.emotion == null) {
                emotion.visibility = View.GONE
            } else {
                emotion.visibility = View.VISIBLE
                setMood(session.emotion!!)
                emotionText = session.emotion.toString()
            }

            container.setOnClickListener {
                val dispatchedFragment = EditSessionFragment.newInstance()
                dispatchedFragment.arguments = setUpBundle(commentText, placeText, emotionText, session.startTime, session.endTime,
                        session.startPage, session.endPage, session.bookId, session.sessionId)
                openTemporaryFragment(it.context as AppCompatActivity, dispatchedFragment, R.id.temporaryFragment)
            }
        }

        private fun setMood(mood: Mood) {
            when (mood) {
                Mood.HAPPY -> emotion.setImageResource(R.drawable.ic_emotion_happy_enabled)
                Mood.SAD -> emotion.setImageResource(R.drawable.ic_emotion_sad_enabled)
                Mood.NEUTRAL -> emotion.setImageResource(R.drawable.ic_emotion_neutral_enabled)
                Mood.VERY_SAD -> emotion.setImageResource(R.drawable.ic_emotion_very_sad_enabled)
                Mood.VERY_HAPPY -> emotion.setImageResource(R.drawable.ic_emotion_very_happy_enabled)
            }
        }

        private fun setPlace(location: Place) {
            when (location) {
                Place.WORK -> place.setImageResource(R.drawable.ic_location_work_enabled)
                Place.TRANSPORT -> place.setImageResource(R.drawable.ic_location_transport_enabled)
                Place.HOME -> place.setImageResource(R.drawable.ic_location_home_enabled)
                Place.THIRD_PLACE -> place.setImageResource(R.drawable.ic_location_third_place_enabled)
            }
        }

        private fun setDate(month: Int, dayOfMonth: Int) {
            val dateText = dayOfMonth.toString() + " " + translateMonth(month, date.rootView.resources, MONTH_GENITIVE)
            date.text = dateText
        }

        private fun setDayOfTheWeek(day: Int) {
            dayOfTheWeek.text = translateDayOfTheWeek(day, dayOfTheWeek.rootView.resources, WEEK_FULL)
        }

        private fun setUpBundle(comment: String, place: String, emotion: String, startTime: Calendar, endTime: Calendar,
                                startPage: Int?, endPage: Int?, bookId: String, sessionId: String): Bundle {
            val bundle = Bundle()
            bundle.putString("date", date.text.toString())
            bundle.putString("dayOfTheWeek", dayOfTheWeek.text.toString())
            bundle.putString("startTimeMinutes", startTime.get(Calendar.MINUTE).toString())
            bundle.putString("startTimeHours", startTime.get(Calendar.HOUR_OF_DAY).toString())
            bundle.putString("endTimeMinutes", endTime.get(Calendar.MINUTE).toString())
            bundle.putString("endTimeHours", endTime.get(Calendar.HOUR_OF_DAY).toString())
            bundle.putString("minutes", minutes.text.toString())
            bundle.putString("hours", hours.text.toString())
            bundle.putString("author", author.text.toString())
            bundle.putString("title", title.text.toString())
            bundle.putString("comment", comment)
            bundle.putString("place", place)
            bundle.putString("emotion", emotion)
            bundle.putString("bookId", bookId)
            bundle.putString("sessionId", sessionId)
            startPage?.apply { bundle.putInt("startPage", this) }
            endPage?.apply { bundle.putInt("endPage", this) }

            return bundle
        }

    }

    companion object {
        data class Session(var startTime: Calendar, var endTime: Calendar, var duration: Int,
                           var startPage: Int, var endPage: Int, var emotion: Mood?,
                           var place: Place?, val author: String?, var comment: String?,
                           val title: String, val sessionId: String, val bookId: String)
    }
}