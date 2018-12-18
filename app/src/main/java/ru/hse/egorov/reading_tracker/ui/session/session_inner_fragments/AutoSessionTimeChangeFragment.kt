package ru.hse.egorov.reading_tracker.ui.session.session_inner_fragments

import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.fragment_auto_session_time_change.*
import kotlinx.android.synthetic.main.fragment_auto_session_time_change.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.session.EndOfSessionFragment
import java.util.*

class AutoSessionTimeChangeFragment : Fragment(), FragmentLauncher {
    private var isChronometerRunning = false
    private lateinit var doneButton: MenuItem
    private var startTime: Long = System.currentTimeMillis()

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
        view.stopwatch.visibility = View.INVISIBLE
        view.startSession.setOnClickListener {
            startTime = Calendar.getInstance().time.time
            view.stopwatch.visibility = View.VISIBLE
            view.startSession.visibility = View.INVISIBLE
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.start()
            isChronometerRunning = true
            doneButton.isEnabled = true
        }
        val libraryAdapter = LibraryFragment.getAdapter()
        if (libraryAdapter.itemCount == 0) {
            view.startSession.isEnabled = false
        }
        view.toManualTimeChange.setOnClickListener {
            openInnerFragment(ManualSessionTimeChangeFragment.newInstance(), parentFragment!!, R.id.sessionFragment)
        }

        setHasOptionsMenu(true)
        setChronometerListener(view)
    }

    override fun onResume() {
        super.onResume()

        val libraryAdapter = LibraryFragment.getAdapter()
        if (libraryAdapter.itemCount == 0) {
            startSession.isEnabled = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.action_bar, menu)
        doneButton = menu!!.getItem(0)
        doneButton.setOnMenuItemClickListener {
            val dispatchFragment = EndOfSessionFragment.newInstance()
            val bundle = Bundle()
            bundle.putInt("duration", minutes.text.toString().toInt() * 60 + seconds.text.toString().toInt())
            bundle.putString("bookId", LibraryFragment.getAdapter().get(0).id)
            bundle.putLong("startTime", startTime)
            bundle.putLong("endTime", Calendar.getInstance().time.time)
            dispatchFragment.arguments = bundle
            openTemporaryFragment(activity as AppCompatActivity, dispatchFragment, R.id.temporaryFragment)
            true
        }
        doneButton.isEnabled = startSession.visibility == View.INVISIBLE
        setHasOptionsMenu(false)
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
            view.minutes.text = m.toString()
            view.seconds.text = s.toString()
        }
    }

    companion object {
        fun newInstance() = AutoSessionTimeChangeFragment()
    }

}