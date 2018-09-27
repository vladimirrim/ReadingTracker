package ru.hse.egorov.reading_tracker

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() {

    private val authManager: FirebaseAuth? = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashScreenTheme)
        super.onCreate(savedInstanceState)

        chooseActivity()
        finish()
    }

    private fun chooseActivity() {
        val currentUser = authManager?.currentUser

        if (currentUser == null) {
            val intent = Intent(this,
                    SignInActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this,
                    MainActivity::class.java)
            startActivity(intent)
        }
    }
}
