package ru.hse.egorov.reading_tracker.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.adapter.SignUpInfoAdapter
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.MainActivity

/**
 * A login screen that offers login via email/password.
 */
class SignUpActivity : AppCompatActivity() {
    private val dbManager = DatabaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signUpButton.setOnClickListener { _ ->
            if (checkAllFieldsFilled()) {
                dbManager.auth(email.text.toString(), password.text.toString())?.addOnCompleteListener(this) { it ->
                    if (it.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val intent = Intent(this, SignUpPersonalInfoActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", it.exception)
                        Toast.makeText(this, "Authentication failed.Reason:${it.exception?.localizedMessage}",
                                Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please check your email and password.",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkAllFieldsFilled(): Boolean {

        return email.text != null &&
                password.text != null && password.text.toString() == confirmPassword.text.toString()


    }


    companion object {
        private const val TAG = "SIGN UP"
    }
}
