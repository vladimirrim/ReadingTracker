package ru.hse.egorov.reading_tracker.ui.session.session_inner_fragments

import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.view.*
import kotlinx.android.synthetic.main.fragment_auto_session_time_change.*
import kotlinx.android.synthetic.main.fragment_auto_session_time_change.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher

class AutoSessionTimeChangeFragment : Fragment(), FragmentLauncher {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_auto_session_time_change, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.startSession.setOnClickListener {
            view.startSession.visibility = View.INVISIBLE
            chronometer.start()
        }

        view.toManualTimeChange.setOnClickListener {
            openInnerFragment(ManualSessionTimeChangeFragment.newInstance(), parentFragment!!,R.id.sessionFragment)
        }

        var initialTime = 0

        if (arguments != null) {
            view.startSession.visibility = View.INVISIBLE
            initialTime = arguments!!["minutes"] as Int
            view.minutes.text = initialTime.toString()
        }

        setChronometerListener(view, initialTime)
    }

    private fun setChronometerListener(view: View, initialTime: Int) {
        var isChronometerRunning = false

        view.chronometer.setOnClickListener {
            if (isChronometerRunning) {
                isChronometerRunning = false
                chronometer.setBackgroundResource(R.drawable.ic_session_start)
                chronometer.stop()
            } else {
                isChronometerRunning = true
                chronometer.setBackgroundResource(R.drawable.ic_session_pause)
                chronometer.start()
            }
        }

        view.chronometer.setOnChronometerTickListener {
            val time = SystemClock.elapsedRealtime() - it.base + initialTime*1000*60
            val m = time / 60000
            val s = (time - m * 60000) / 1000
            view.minutes.text = m.toString()
            view.seconds.text = s.toString()
        }
    }

    companion object {
        fun newInstance() = AutoSessionTimeChangeFragment()
    }

}