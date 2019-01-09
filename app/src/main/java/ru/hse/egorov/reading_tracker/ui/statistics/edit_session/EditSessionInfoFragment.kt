package ru.hse.egorov.reading_tracker.ui.statistics.edit_session

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_end_of_session.*
import kotlinx.android.synthetic.main.fragment_end_of_session.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.MainActivity
import ru.hse.egorov.reading_tracker.ui.MainActivity.Companion.PROFILE_FRAGMENT_POSITION
import ru.hse.egorov.reading_tracker.ui.action_bar.ActionBarSetter
import ru.hse.egorov.reading_tracker.ui.adapter.SessionAdapter
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.session.EndOfSessionFragment
import ru.hse.egorov.reading_tracker.ui.statistics.OverallStatisticsFragment
import java.util.*

class EditSessionInfoFragment : Fragment(), FragmentLauncher {
    private val dbManager = DatabaseManager()
    private var place: String? = null
    private var mood: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_end_of_session, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        updateActionBar(activity as AppCompatActivity)

        view.emotions.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.emotionHappy -> mood = EndOfSessionFragment.Companion.Mood.HAPPY.toString().toLowerCase()
                R.id.emotionNeutral -> mood = EndOfSessionFragment.Companion.Mood.NEUTRAL.toString().toLowerCase()
                R.id.emotionSad -> mood = EndOfSessionFragment.Companion.Mood.SAD.toString().toLowerCase()
                R.id.emotionVeryHappy -> mood = EndOfSessionFragment.Companion.Mood.VERY_HAPPY.toString().replace("_", " ").toLowerCase()
                R.id.emotionVerySad -> mood = EndOfSessionFragment.Companion.Mood.VERY_SAD.toString().replace("_", " ").toLowerCase()
            }
        }

        view.locations.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.locationHome -> place = EndOfSessionFragment.Companion.Place.HOME.toString().toLowerCase()
                R.id.locationTransport -> place = EndOfSessionFragment.Companion.Place.TRANSPORT.toString().toLowerCase()
                R.id.locationWork -> place = EndOfSessionFragment.Companion.Place.WORK.toString().toLowerCase()
                R.id.locationThirdPlace -> place = EndOfSessionFragment.Companion.Place.THIRD_PLACE.toString().replace("_", " ").toLowerCase()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.action_bar, menu)
        menu?.getItem(0)?.setOnMenuItemClickListener {
            if (startPage.text!!.isNotEmpty() && endPage.text!!.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                dbManager.updateSession(gatherSessionInfo(), arguments!!["sessionId"] as String).addOnSuccessListener {
                    editSession()
                    openPagerFragment(activity as AppCompatActivity, PROFILE_FRAGMENT_POSITION)
                }
            } else {
                Snackbar.make(activity!!.placeSnackBar, FILL_PAGES_MESSAGE, Snackbar.LENGTH_SHORT).show()
            }
            true
        }
    }

    private fun gatherSessionInfo(): HashMap<String, Any?> {
        val session = HashMap<String, Any?>()
        session["start page"] = startPage.text.toString().toLong()
        session["end page"] = endPage.text.toString().toLong()
        session["place"] = place
        session["mood"] = mood
        session["comment"] = comment.text.toString()
        return session
    }

    private fun editSession() {
        val sessionId = arguments!!.getString("sessionId")
        setSession(OverallStatisticsFragment.getAllSessions().find { it.sessionId == sessionId }!!)
        setSession(OverallStatisticsFragment.getSessionsForPeriod().find { it.sessionId == sessionId }!!)
        (activity?.supportFragmentManager?.findFragmentByTag("android:switcher:" + R.id.fragmentPager + ":"
                + PROFILE_FRAGMENT_POSITION) as OverallStatisticsFragment).updateStatistics()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            setHasOptionsMenu(false)
            (activity?.supportFragmentManager?.findFragmentByTag("android:switcher:" + R.id.fragmentPager + ":"
                    + MainActivity.PROFILE_FRAGMENT_POSITION) as ActionBarSetter).setActionBar(activity as AppCompatActivity)
            openPagerFragment(activity as AppCompatActivity, PROFILE_FRAGMENT_POSITION)
        }
        return true
    }

    private fun updateActionBar(activity: AppCompatActivity) {
        activity.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.title = ACTION_BAR_TITLE
    }

    private fun setSession(session: SessionAdapter.Companion.Session) {
        session.startPage = startPage.text.toString().toInt()
        session.endPage = endPage.text.toString().toInt()
        session.place = EndOfSessionFragment.Companion.Place.getPlaceByName(place)
        session.emotion = EndOfSessionFragment.Companion.Mood.getMoodByName(mood)
        session.comment = comment.text.toString()
    }

    companion object {
        private const val FILL_PAGES_MESSAGE = "Заполните начальную и конечную страницы."
        private const val ACTION_BAR_TITLE = "Оценка чтения"

        fun newInstance() = EditSessionInfoFragment()
    }
}