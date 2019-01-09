package ru.hse.egorov.reading_tracker.ui.session

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.activity_main.*
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
import kotlin.collections.HashMap

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
                R.id.emotionHappy -> mood = Mood.HAPPY.toString().toLowerCase()
                R.id.emotionNeutral -> mood = Mood.NEUTRAL.toString().toLowerCase()
                R.id.emotionSad -> mood = Mood.SAD.toString().toLowerCase()
                R.id.emotionVeryHappy -> mood = Mood.VERY_HAPPY.toString().replace("_", " ").toLowerCase()
                R.id.emotionVerySad -> mood = Mood.VERY_SAD.toString().replace("_", " ").toLowerCase()
            }
        }

        view.locations.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.locationHome -> place = Place.HOME.toString().toLowerCase()
                R.id.locationTransport -> place = Place.TRANSPORT.toString().toLowerCase()
                R.id.locationWork -> place = Place.WORK.toString().toLowerCase()
                R.id.locationThirdPlace -> place = Place.THIRD_PLACE.toString().replace("_", " ").toLowerCase()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.action_bar, menu)
        menu?.getItem(0)?.setOnMenuItemClickListener {
            if (startPage.text!!.isNotEmpty() && endPage.text!!.isNotEmpty()) {
                val session = setSession()
                progressBar.visibility = View.VISIBLE
                dbManager.addSession(session).addOnSuccessListener { doc ->
                    (activity?.supportFragmentManager?.findFragmentByTag("android:switcher:" + R.id.fragmentPager + ":"
                            + SESSION_FRAGMENT_POSITION) as StartOfSessionFragment).resetSession()
                    updateStatistics(session, doc)
                    openPagerFragment(activity as AppCompatActivity, PROFILE_FRAGMENT_POSITION)
                    this@EndOfSessionFragment.setHasOptionsMenu(false)
                }
            } else {
                Snackbar.make(activity!!.placeSnackBar, FILL_PAGES_MESSAGE, Snackbar.LENGTH_SHORT).show()
            }
            true
        }
    }

    private fun updateStatistics(session: HashMap<String, Any?>, doc: DocumentReference) {
        OverallStatisticsFragment.getAllSessions().add(0, statsManager.wrapSession(session, doc.id, arguments!!["author"] as String?,
                arguments!!["title"] as String))
        OverallStatisticsFragment.getSessionsForPeriod().add(0, statsManager.wrapSession(session, doc.id, arguments!!["author"] as String?,
                arguments!!["title"] as String))
        (activity?.supportFragmentManager?.findFragmentByTag("android:switcher:" + R.id.fragmentPager + ":"
                + PROFILE_FRAGMENT_POSITION) as OverallStatisticsFragment).updateStatistics()
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

    companion object {
        private const val FILL_PAGES_MESSAGE = "Заполните начальную и конечную страницы."

        fun newInstance() = EndOfSessionFragment()

        enum class Mood {
            HAPPY, SAD, NEUTRAL, VERY_SAD, VERY_HAPPY;

            companion object {
                fun getMoodByName(name: String?): Mood? {
                    return values().firstOrNull {
                        it.name.replace("_", " ").toLowerCase() == name?.replace("_", " ")?.toLowerCase()
                    }
                }
            }
        }

        enum class Place {
            WORK, TRANSPORT, HOME, THIRD_PLACE;

            companion object {
                fun getPlaceByName(name: String?): Place? {
                    return values().firstOrNull {
                        it.name.replace("_", " ").toLowerCase() == name?.replace("_", " ")?.toLowerCase()
                    }
                }
            }
        }
    }
}
