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

class AutoSessionTimeChangeFragment : Fragment(), FragmentLauncher {
    private var isChronometerRunning = false
    private lateinit var doneButton: MenuItem

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

        setChronometerListener(view)
    }

    override fun onResume() {
        super.onResume()

        val libraryAdapter = LibraryFragment.getAdapter()
        if (libraryAdapter.itemCount == 0) {
            startSession.isEnabled = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val dispatchFragment = EndOfSessionFragment.newInstance()
        val bundle = Bundle()
        bundle.putInt("startPage", startPage.text.toString().toIntOrNull() ?: -1)
        bundle.putInt("endPage", endPage.text.toString().toIntOrNull() ?: -1)
        bundle.putInt("duration", minutes.text.toString().toInt() * 60 + seconds.text.toString().toInt())
        bundle.putString("bookId", LibraryFragment.getAdapter().get(0).id)
        bundle.putLong("startTime", chronometer.base)
        dispatchFragment.arguments = bundle
        openTemporaryFragment(activity as AppCompatActivity, dispatchFragment, R.id.temporaryFragment)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.action_bar, menu)
        doneButton = menu!!.getItem(0)
        doneButton.isEnabled = startSession.visibility == View.INVISIBLE
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