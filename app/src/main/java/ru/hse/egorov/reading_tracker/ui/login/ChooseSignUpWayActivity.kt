package ru.hse.egorov.reading_tracker.ui.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_choose_sign_up_way.*
import ru.hse.egorov.reading_tracker.R

class ChooseSignUpWayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_sign_up_way)

        signUpWithCode.setOnClickListener {
            val intent = Intent(this, SignUpCodeActivity::class.java)
            startActivity(intent)
        }

        signUpWithEmail.setOnClickListener {
            val intent = Intent(this, SignUpEmailActivity::class.java)
            startActivity(intent)
        }
    }
}
