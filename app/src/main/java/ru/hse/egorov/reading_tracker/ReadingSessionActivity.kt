package ru.hse.egorov.reading_tracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_reading_session.*

class ReadingSessionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading_session)

        chronometer.start()

        doneButton.setOnClickListener {
            chronometer.stop()
        }
    }
}
