package ru.hse.egorov.reading_tracker.ui.session.session_inner_fragments

import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_auto_session_time_change.*
import kotlinx.android.synthetic.main.fragment_auto_session_time_change.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.session.EndOfSessionFragment

class AutoSessionTimeChangeFragment : Fragment(), FragmentLauncher {
    private lateinit var doneButton: MenuItem
    private var isChronometerRunning = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_auto_session_time_change, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.chronometer.stop()
        view.startSession.setOnClickListener {
            view.startSession.visibility = View.INVISIBLE
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.start()
            isChronometerRunning = true
            if(!doneButton.isEnabled) {
                doneButton.isEnabled = true
                doneButton.setIcon(R.drawable.ic_done_enabled)
                doneButton.setOnMenuItemClickListener {
                    openFragment(EndOfSessionFragment.newInstance(), activity as AppCompatActivity, R.id.fragment)
                    true
                }
            }
        }

        view.toManualTimeChange.setOnClickListener {
            openInnerFragment(ManualSessionTimeChangeFragment.newInstance(), parentFragment!!, R.id.sessionFragment)
        }

        setChronometerListener(view)
    }

    private fun setChronometerListener(view: View) {
        view.chronometer.stop()

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
            val time = SystemClock.elapsedRealtime() - it.base
            val m = time / 60000
            val s = (time - m * 60000) / 1000
            view.minutes.text = m.toString()
            view.seconds.text = s.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        doneButton = menu!!.getItem(0)
        doneButton.isEnabled = false
    }

    companion object {
        fun newInstance() = AutoSessionTimeChangeFragment()
    }

}