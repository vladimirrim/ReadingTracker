package ru.hse.egorov.reading_tracker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashScreenTheme)
        super.onCreate(savedInstanceState)

        val intent = Intent(applicationContext,
                MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
