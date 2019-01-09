package ru.hse.egorov.reading_tracker.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up_email.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.MainActivity


/**
 * A login screen that offers login via email/password.
 */
class SignUpEmailActivity : AppCompatActivity() {
    private val dbManager = DatabaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_email)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ACTION_BAR_TITLE
        signUpEmail.setOnClickListener {
            if (checkAllFieldsFilled()) {
                showProgressBar()
                dbManager.auth(email.text.toString(), password.text.toString())?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        hideProgressBar()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        hideProgressBar()
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Snackbar.make(findViewById(android.R.id.content),
                                "Authentication failed.Reason:${task.exception?.localizedMessage}",
                                Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        "Please check your email and password.",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            super.onBackPressed()
        }
        return true
    }

    private fun showProgressBar() {
        signUpEmail.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
        signUpEmail.visibility = View.VISIBLE
    }

    private fun checkAllFieldsFilled(): Boolean {
        return email.text != null && email.text.toString() != "" && password.text.toString() != ""
                && password.text != null && password.text.toString() == confirmPassword.text.toString()


    }

    companion object {
        private const val TAG = "SIGN UP"
        private const val ACTION_BAR_TITLE = "Регистрация"
    }
}
