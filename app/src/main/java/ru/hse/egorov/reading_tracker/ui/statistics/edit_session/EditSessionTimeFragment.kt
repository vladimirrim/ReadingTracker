package ru.hse.egorov.reading_tracker.ui.statistics.edit_session

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.book.*
import kotlinx.android.synthetic.main.fragment_manual_session_time_change.*
import kotlinx.android.synthetic.main.fragment_manual_session_time_change.view.*
import kotlinx.android.synthetic.main.session_time.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.MainActivity
import ru.hse.egorov.reading_tracker.ui.MainActivity.Companion.PROFILE_FRAGMENT_POSITION
import ru.hse.egorov.reading_tracker.ui.action_bar.ActionBarSetter
import ru.hse.egorov.reading_tracker.ui.adapter.SessionAdapter
import ru.hse.egorov.reading_tracker.ui.date.DateTranslator
import ru.hse.egorov.reading_tracker.ui.dialog.SessionDateDialog
import ru.hse.egorov.reading_tracker.ui.dialog.SessionTimeDialog
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.statistics.OverallStatisticsFragment
import java.util.*
import kotlin.collections.HashMap

class EditSessionTimeFragment : Fragment(), FragmentLauncher, DateTranslator {
    private val dbManager = DatabaseManager()
    private var startTimeMinutes = 0
    private var endTimeMinutes = 0
    private var date = Calendar.getInstance().timeInMillis

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_manual_session_time_change, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        val currentDate = Calendar.getInstance()
        val dateText = currentDate.get(Calendar.DAY_OF_MONTH).toString() + " " + translateMonth(currentDate.get(Calendar.MONTH), resources,
                DateTranslator.MONTH_GENITIVE)
        view.sessionDate.text = dateText

        setEndSessionButton()

        view.sessionDate.setOnClickListener {
            val dispatchedFragment = SessionDateDialog()
            dispatchedFragment.setTargetFragment(this, DATE_RC)
            dispatchedFragment.show(fragmentManager, "Session Date")
        }

        view.sessionTimeContainer.setOnClickListener {
            val dispatchedFragment = SessionTimeDialog()
            dispatchedFragment.setTargetFragment(this, TIME_RC)
            val bundle = Bundle()
            bundle.putInt("startTime", startTimeMinutes)
            bundle.putInt("endTime", endTimeMinutes)
            dispatchedFragment.arguments = bundle
            dispatchedFragment.show(fragmentManager, "Session Time")
        }

        view.toSession.visibility = View.GONE
        setBook()
    }

    private fun setBook() {
        author.text = arguments!!["author"] as String
        title.text = arguments!!["title"] as String
        dbManager.getBookCover(arguments!!["bookId"] as String, context!!).into(cover)
        book.visibility = View.VISIBLE
    }

    private fun setEndSessionButton() {
        endSessionButton.setOnClickListener {
            it.visibility = View.INVISIBLE
            progressBarSave.visibility = View.VISIBLE
            dbManager.updateSession(gatherSessionInfo(), arguments!!["sessionId"] as String).addOnSuccessListener {
                editSession()
                openPagerFragment(activity as AppCompatActivity, PROFILE_FRAGMENT_POSITION)
            }
        }
    }

    private fun gatherSessionInfo(): HashMap<String, Any?> {
        val map = HashMap<String, Any?>()
        var duration = endTimeMinutes - startTimeMinutes
        if (duration < 0) duration += 24 * 60
        map["duration"] = duration * 60
        map["start time"] = Date(date + startTimeMinutes * 60 * 1000)
        map["end time"] = Date(date + startTimeMinutes * 60 * 1000 + duration * 60 * 1000)
        return map
    }

    private fun editSession() {
        val sessionId = arguments!!.getString("sessionId")
        val allSessions = OverallStatisticsFragment.getAllSessions()
        val sessionsForPeriod = OverallStatisticsFragment.getSessionsForPeriod()
        setSession(allSessions.find { it.sessionId == sessionId }!!)
        setSession(sessionsForPeriod.find { it.sessionId == sessionId }!!)
        allSessions.sortByDescending { it.startTime }
        sessionsForPeriod.sortByDescending { it.startTime }
        (activity?.supportFragmentManager?.findFragmentByTag("android:switcher:" + R.id.fragmentPager + ":"
                + PROFILE_FRAGMENT_POSITION) as OverallStatisticsFragment).updateStatistics()
    }

    private fun setSession(session: SessionAdapter.Companion.Session) {
        var duration = endTimeMinutes - startTimeMinutes
        if (duration < 0) duration += 24 * 60
        val startTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()
        startTime.timeInMillis = date + startTimeMinutes * 60 * 1000
        endTime.timeInMillis = date + startTimeMinutes * 60 * 1000 + duration * 60 * 1000
        session.duration = duration * 60
        session.startTime = startTime
        session.endTime = endTime
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            DATE_RC -> {
                val selectedDate = Calendar.getInstance()
                date = data!!.getLongExtra("date", 0)
                selectedDate.timeInMillis = date
                val dateText = selectedDate.get(Calendar.DAY_OF_MONTH).toString() + " " + translateMonth(selectedDate.get(Calendar.MONTH),
                        resources, DateTranslator.MONTH_GENITIVE)
                sessionDate.text = dateText
            }

            TIME_RC -> {
                startTimeMinutes = data!!.getIntExtra("startTime", 0)
                endTimeMinutes = data.getIntExtra("endTime", 0)
                var duration = endTimeMinutes - startTimeMinutes
                if (duration < 0) duration += 24 * 60
                sessionTimeHours.text = (duration / 60).toString()
                sessionTimeMinutes.text = (duration % 60).toString()
                startTime.hours.text = (startTimeMinutes / 60).toString()
                startTime.minutes.text = (startTimeMinutes % 60).toString()
                endTime.hours.text = (endTimeMinutes / 60).toString()
                endTime.minutes.text = (endTimeMinutes % 60).toString()
            }
        }
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

    companion object {
        const val DATE_RC = 1
        const val TIME_RC = 2
        fun newInstance() = EditSessionTimeFragment()
    }
}