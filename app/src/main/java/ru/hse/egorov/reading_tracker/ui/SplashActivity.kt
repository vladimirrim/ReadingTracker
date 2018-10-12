package ru.hse.egorov.reading_tracker.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.login.SignInActivity


class SplashActivity : AppCompatActivity() {
    private val db = DatabaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashScreenTheme)
        super.onCreate(savedInstanceState)

        chooseActivity()
        finish()
    }

    private fun chooseActivity() {

        if (db.isAuth()) {
            val intent = Intent(this,
                    MainActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this,
                    SignInActivity::class.java)
            startActivity(intent)
        }
    }
}
