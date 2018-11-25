package ru.hse.egorov.reading_tracker.ui.session

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.fragment_end_of_session.*
import kotlinx.android.synthetic.main.fragment_end_of_session.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.MainActivity.Companion.PROFILE_FRAGMENT_POSITION
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher

class EndOfSessionFragment : Fragment(), FragmentLauncher {
    private val dbManager = DatabaseManager()
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
                    setBackgroundsForEmotions(R.drawable.ic_emotion_happy_enabled,
                            R.drawable.ic_emotion_neutral,
                            R.drawable.ic_emotion_sad)
                }

                R.id.emotionNeutral -> {
                    setBackgroundsForEmotions(R.drawable.ic_emotion_happy,
                            R.drawable.ic_emotion_neutral_enabled,
                            R.drawable.ic_emotion_sad)
                }

                R.id.emotionSad -> {
                    setBackgroundsForEmotions(R.drawable.ic_emotion_happy,
                            R.drawable.ic_emotion_neutral,
                            R.drawable.ic_emotion_sad_enabled)
                }
            }
        }

        view.locations.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.locationHome -> {
                    setBackgroundsForLocations(R.drawable.ic_location_home_enabled,
                            R.drawable.ic_location_transport,
                            R.drawable.ic_location_work)
                }

                R.id.locationTransport -> {
                    setBackgroundsForLocations(R.drawable.ic_location_home,
                            R.drawable.ic_location_transport_enabled,
                            R.drawable.ic_location_work)
                }

                R.id.locationWork -> {
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
            val session = HashMap<String, Any?>()
            session["start page"] = arguments!!["startPage"]
            session["end page"] = arguments!!["endPage"]
            session["time"] = arguments!!["time"]
            session["place"] = place
            session["mood"] = mood
            progressBar.visibility = View.VISIBLE
            dbManager.addSession(session).addOnSuccessListener {
                openPagerFragment(activity as AppCompatActivity, PROFILE_FRAGMENT_POSITION)
            }
            true
        }
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
    }
}
