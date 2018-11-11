package ru.hse.egorov.reading_tracker.ui.session

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_end_of_session.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.MainActivity

class EndOfSessionFragment : AppCompatActivity() {

    private val db = DatabaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_of_session)
        val time = intent.getStringExtra(SESSION_TIME)
        val text = "Session time:\n$time"
        sessionTime.text = text

        db.addSession(time).addOnSuccessListener {
            Toast.makeText(this, "OK!",
                    Toast.LENGTH_SHORT).show()
        }

        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        const val SESSION_TIME = "Session time"
    }
}
