package ru.hse.egorov.reading_tracker.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.session.ReadingSessionActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startSessionButton.setOnClickListener {
            val intent = Intent(this, ReadingSessionActivity::class.java)
            startActivity(intent)
        }
    }
}
