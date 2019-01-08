package ru.hse.egorov.reading_tracker.ui.session

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.fragment_end_of_session.*
import kotlinx.android.synthetic.main.fragment_end_of_session.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.statistics.StatisticsManager
import ru.hse.egorov.reading_tracker.ui.MainActivity.Companion.PROFILE_FRAGMENT_POSITION
import ru.hse.egorov.reading_tracker.ui.MainActivity.Companion.SESSION_FRAGMENT_POSITION
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.statistics.OverallStatisticsFragment
import java.util.*

class EndOfSessionFragment : Fragment(), FragmentLauncher {
    private val dbManager = DatabaseManager()
    private val statsManager = StatisticsManager()
    private var place: String? = null
    private var mood: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_end_of_session, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        view.emotions.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.emotionHappy -> {
                    mood = Mood.HAPPY.toString().toLowerCase()
                    setBackgroundsForEmotions(R.drawable.ic_emotion_happy_enabled,
                            R.drawable.ic_emotion_neutral,
                            R.drawable.ic_emotion_sad)
                }

                R.id.emotionNeutral -> {
                    mood = Mood.NEUTRAL.toString().toLowerCase()
                    setBackgroundsForEmotions(R.drawable.ic_emotion_happy,
                            R.drawable.ic_emotion_neutral_enabled,
                            R.drawable.ic_emotion_sad)
                }

                R.id.emotionSad -> {
                    mood = Mood.SAD.toString().toLowerCase()
                    setBackgroundsForEmotions(R.drawable.ic_emotion_happy,
                            R.drawable.ic_emotion_neutral,
                            R.drawable.ic_emotion_sad_enabled)
                }
            }
        }

        view.locations.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.locationHome -> {
                    place = Place.HOME.toString().toLowerCase()
                    setBackgroundsForLocations(R.drawable.ic_location_home_enabled,
                            R.drawable.ic_location_transport,
                            R.drawable.ic_location_work)
                }

                R.id.locationTransport -> {
                    place = Place.TRANSPORT.toString().toLowerCase()
                    setBackgroundsForLocations(R.drawable.ic_location_home,
                            R.drawable.ic_location_transport_enabled,
                            R.drawable.ic_location_work)
                }

                R.id.locationWork -> {
                    place = Place.WORK.toString().toLowerCase()
                    setBackgroundsForLocations(R.drawable.ic_location_home,
                            R.drawable.ic_location_transport,
                            R.drawable.ic_location_work_enabled)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.action_bar, menu)
        menu?.getItem(0)?.setOnMenuItemClickListener {
            val session = setSession()
            progressBar.visibility = View.VISIBLE
            dbManager.addSession(session).addOnSuccessListener { doc ->
                (activity?.supportFragmentManager?.findFragmentByTag("android:switcher:" + R.id.fragmentPager + ":"
                        + SESSION_FRAGMENT_POSITION) as StartOfSessionFragment).resetSession()
                OverallStatisticsFragment.getAllSessions().add(statsManager.wrapSession(session, doc.id, arguments!!["author"] as String?,
                        arguments!!["title"] as String))
                openPagerFragment(activity as AppCompatActivity, PROFILE_FRAGMENT_POSITION)
                this@EndOfSessionFragment.setHasOptionsMenu(false)
            }
            true
        }
    }

    private fun setSession(): HashMap<String, Any?> {
        val session = HashMap<String, Any?>()
        session["start page"] = if (startPage.text.toString() == "") -1 else startPage.text.toString().toLong()
        session["end page"] = if (endPage.text.toString() == "") -1 else endPage.text.toString().toLong()
        session["duration"] = arguments!!["duration"]
        session["place"] = place
        session["mood"] = mood
        session["book id"] = arguments!!["bookId"]
        session["start time"] = Date((arguments!!["startTime"] as Long))
        session["end time"] = Date(arguments!!["endTime"] as Long)
        session["comment"] = comment.text.toString()
        return session
    }

    private fun setBackgroundsForEmotions(backgroundHappy: Int, backgroundIndifferent: Int, backgroundSad: Int) {
        emotionHappy.setBackgroundResource(backgroundHappy)
        emotionNeutral.setBackgroundResource(backgroundIndifferent)
        emotionSad.setBackgroundResource(backgroundSad)
    }

    private fun setBackgroundsForLocations(backgroundHome: Int, backgroundTransport: Int, backgroundWork: Int) {
        locationHome.setBackgroundResource(backgroundHome)
        locationTransport.setBackgroundResource(backgroundTransport)
        locationWork.setBackgroundResource(backgroundWork)
    }

    companion object {
        fun newInstance() = EndOfSessionFragment()

        enum class Mood {
            HAPPY, SAD, NEUTRAL;

            companion object {
                fun getMoodByName(name: String?): Mood? {
                    return values().firstOrNull { it.name.toLowerCase() == name }
                }
            }
        }

        enum class Place {
            WORK, TRANSPORT, HOME;

            companion object {
                fun getPlaceByName(name: String?): Place? {
                    return values().firstOrNull { it.name.toLowerCase() == name }
                }
            }
        }
    }
}
