package ru.hse.egorov.reading_tracker.ui.session

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_reading_session.*
import ru.hse.egorov.reading_tracker.R

class ReadingSessionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading_session)

        chronometer.start()

        doneButton.setOnClickListener {
            chronometer.stop()
            val intent = Intent(this, EndOfSessionFragment::class.java)
            intent.putExtra(SESSION_TIME, chronometer.text.toString())
            startActivity(intent)
        }
    }

    companion object {
        const val SESSION_TIME = "Session time"
    }
}
