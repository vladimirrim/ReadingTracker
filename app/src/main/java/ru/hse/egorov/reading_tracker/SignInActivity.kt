package ru.hse.egorov.reading_tracker

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

/**
 * A login screen that offers login via email/password.
 */
class SignInActivity : AppCompatActivity() {
    private val authManager: FirebaseAuth? = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signUpSwitchButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        signInButton.setOnClickListener { _ ->
            if (email.text != null && password.text != null) {
                authManager?.signInWithEmailAndPassword(email.text.toString(), password.text.toString())?.addOnCompleteListener(this
                ) {
                    if (it.isSuccessful) {
                        Log.d(TAG, "signInWithEmail:success")
                        val user = authManager.currentUser
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", it.exception)
                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "SIGN IN"
    }
}
