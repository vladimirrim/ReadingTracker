package ru.hse.egorov.reading_tracker.ui.session.session_inner_fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_manual_session_time_change.*
import kotlinx.android.synthetic.main.fragment_manual_session_time_change.view.*
import kotlinx.android.synthetic.main.session_time.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment
import ru.hse.egorov.reading_tracker.ui.date.DateTranslator
import ru.hse.egorov.reading_tracker.ui.date.DateTranslator.Companion.MONTH_GENITIVE
import ru.hse.egorov.reading_tracker.ui.dialog.SessionDateDialog
import ru.hse.egorov.reading_tracker.ui.dialog.SessionTimeDialog
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.session.EndOfSessionFragment
import java.util.*


class ManualSessionTimeChangeFragment : Fragment(), FragmentLauncher, DateTranslator {
    private var startTimeMinutes = 0
    private var endTimeMinutes = 0
    private var date = Calendar.getInstance().timeInMillis

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_manual_session_time_change, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentDate = Calendar.getInstance()
        val dateText = currentDate.get(Calendar.DAY_OF_MONTH).toString() + " " + translateMonth(currentDate.get(Calendar.MONTH), resources,
                MONTH_GENITIVE)
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

        view.toSession.setOnClickListener {
            openInnerFragment(AutoSessionTimeChangeFragment.newInstance(), parentFragment!!, R.id.sessionFragment)
        }
    }

    private fun setEndSessionButton() {
        endSessionButton.setOnClickListener {
            val dispatchFragment = EndOfSessionFragment.newInstance()
            val bundle = Bundle()
            var duration = endTimeMinutes - startTimeMinutes
            if (duration < 0) duration += 24 * 60
            bundle.putInt("duration", duration * 60)
            bundle.putString("bookId", LibraryFragment.getAdapter().get(0).id)
            bundle.putLong("startTime", date + startTimeMinutes * 60 * 1000)
            bundle.putLong("endTime", date + startTimeMinutes * 60 * 1000 + duration * 60 * 1000)
            dispatchFragment.arguments = bundle
            openTemporaryFragment(activity as AppCompatActivity, dispatchFragment, R.id.temporaryFragment)
        }
        endSessionButton.isEnabled = LibraryFragment.getAdapter().itemCount != 0
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            DATE_RC -> {
                val selectedDate = Calendar.getInstance()
                date = data!!.getLongExtra("date", 0)
                selectedDate.timeInMillis = date
                val dateText = selectedDate.get(Calendar.DAY_OF_MONTH).toString() + " " + translateMonth(selectedDate.get(Calendar.MONTH), resources,
                        MONTH_GENITIVE)
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

    companion object {
        const val DATE_RC = 1
        const val TIME_RC = 2
        fun newInstance() = ManualSessionTimeChangeFragment()
    }
}
