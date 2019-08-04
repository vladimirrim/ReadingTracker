package ru.hse.egorov.reading_tracker.ui.session.session_inner_fragments

import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_auto_session_time_change.*
import kotlinx.android.synthetic.main.fragment_auto_session_time_change.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ReadingTrackerApplication
import ru.hse.egorov.reading_tracker.ui.adapter.LibraryAdapter
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.session.EndOfSessionFragment
import java.util.*
import javax.inject.Inject

class AutoSessionTimeChangeFragment : Fragment(), FragmentLauncher {
    private var isChronometerRunning = false
    private var startTime: Long = System.currentTimeMillis()
    @Inject
    lateinit var library: LibraryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity!!.application as ReadingTrackerApplication).appComponent.inject(this)
        return inflater.inflate(R.layout.fragment_auto_session_time_change, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStopwatchStarter(view)
        setChronometerListener(view)
        setEndSessionButton()

        if (library.itemCount == 0) {
            view.startSession.isEnabled = false
        }
        view.toManualTimeChange.setOnClickListener {
            openInnerFragment(ManualSessionTimeChangeFragment.newInstance().apply { arguments = this@AutoSessionTimeChangeFragment.arguments },
                    parentFragment!!, R.id.sessionFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        if (library.itemCount == 0) {
            startSession.isEnabled = false
        }
    }

    private fun setEndSessionButton() {
        endSessionButton.setOnClickListener {
            val dispatchFragment = EndOfSessionFragment.newInstance()
            dispatchFragment.arguments = setBundle()
            openTemporaryFragment(activity as AppCompatActivity, dispatchFragment, R.id.temporaryFragment)
        }
        endSessionButton.isEnabled = startSession.visibility == View.INVISIBLE
    }

    private fun setBundle(): Bundle {
        val bundle = Bundle()
        bundle.putLong("duration", hours.text.toString().toInt() * 60L + minutes.text.toString().toInt())
        bundle.putString("bookId", library.get(0).id)
        bundle.putLong("startTime", startTime)
        bundle.putLong("endTime", Calendar.getInstance().time.time)
        bundle.putString("author", arguments!!["author"] as String?)
        bundle.putString("title", arguments!!["title"] as String?)
        return bundle
    }

    private fun setStopwatchStarter(view: View) {
        view.chronometer.stop()
        view.stopwatch.visibility = View.INVISIBLE
        view.startSession.setOnClickListener {
            startTime = Calendar.getInstance().time.time
            view.stopwatch.visibility = View.VISIBLE
            view.startSession.visibility = View.INVISIBLE
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.start()
            isChronometerRunning = true
            endSessionButton.isEnabled = true
        }
    }

    private fun setChronometerListener(view: View) {
        var timeWhenStopped: Long = 0
        view.chronometer.setOnClickListener {
            if (isChronometerRunning) {
                isChronometerRunning = false
                chronometer.setBackgroundResource(R.drawable.ic_session_start)
                timeWhenStopped = chronometer.base - SystemClock.elapsedRealtime()
                chronometer.stop()
            } else {
                isChronometerRunning = true
                chronometer.setBackgroundResource(R.drawable.ic_session_pause)
                chronometer.base = SystemClock.elapsedRealtime() + timeWhenStopped
                chronometer.start()
            }
        }

        view.chronometer.setOnChronometerTickListener {
            val time = SystemClock.elapsedRealtime() - it.base
            val m = time / 60000
            val s = (time - m * 60000) / 1000
            view.hours.text = m.toString()
            view.minutes.text = s.toString()
        }
    }

    companion object {
        fun newInstance() = AutoSessionTimeChangeFragment()
    }

}