package ru.hse.egorov.reading_tracker.ui.session

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_end_of_session.*
import kotlinx.android.synthetic.main.fragment_end_of_session.view.*
import ru.hse.egorov.reading_tracker.R

class EndOfSessionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_end_of_session, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.emotions.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.emotionHappy -> {
                    setBackgroundsForEmotions(R.drawable.ic_emotion_happy_enabled,
                            R.drawable.ic_emotion_indifferent,
                            R.drawable.ic_emotion_sad)
                }

                R.id.emotionIndifferent -> {
                    setBackgroundsForEmotions(R.drawable.ic_emotion_happy,
                            R.drawable.ic_emotion_indifferent_enabled,
                            R.drawable.ic_emotion_sad)
                }

                R.id.emotionSad -> {
                    setBackgroundsForEmotions(R.drawable.ic_emotion_happy,
                            R.drawable.ic_emotion_indifferent,
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

    private fun setBackgroundsForEmotions(backgroundHappy: Int, backgroundIndifferent: Int, backgroundSad: Int) {
        emotionHappy.setBackgroundResource(backgroundHappy)
        emotionIndifferent.setBackgroundResource(backgroundIndifferent)
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
