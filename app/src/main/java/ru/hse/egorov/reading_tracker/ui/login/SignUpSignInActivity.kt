package ru.hse.egorov.reading_tracker.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up_sign_in.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager

/**
 * A login screen that offers login via email/password.
 */
class SignUpSignInActivity : AppCompatActivity() {
    private val dbManager = DatabaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_sign_in)

        signUpButton.setOnClickListener {
            val intent = Intent(this, ChooseSignUpWayActivity::class.java)
            startActivity(intent)
        }

        signInButton.setOnClickListener {

            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

            /*  if (email.text.toString() != "" && password.text.toString() != "") {
                dbManager.signIn(email.text.toString(), password.text.toString())?.addOnCompleteListener(this
                ) {
                    if (it.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", it.exception)
                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
            }*/
        }
    }

    companion object {
        private const val TAG = "SIGN IN"
    }
}
