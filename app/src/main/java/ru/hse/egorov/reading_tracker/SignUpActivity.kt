package ru.hse.egorov.reading_tracker

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

/**
 * A login screen that offers login via email/password.
 */
class SignUpActivity : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private val authManager: FirebaseAuth? = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signUpButton.setOnClickListener { _ ->
            if (email.text != null && password.text != null) {
                authManager?.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                        ?.addOnCompleteListener(this) {
                            if (it.isSuccessful) {
                                Log.d(TAG, "createUserWithEmail:success")
                                val user = authManager.currentUser
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", it.exception)
                                Toast.makeText(this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show()
                            }
                        }
            }
        }
    }


    companion object {
        private const val TAG = "SIGN UP"
    }
}
