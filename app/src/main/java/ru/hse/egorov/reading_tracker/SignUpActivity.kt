package ru.hse.egorov.reading_tracker

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import ru.hse.egorov.reading_tracker.adapter.SignUpInfoAdapter

/**
 * A login screen that offers login via email/password.
 */
class SignUpActivity : AppCompatActivity() {

    private val authManager: FirebaseAuth? = FirebaseAuth.getInstance()
    private val userInfoAdapter = SignUpInfoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        infoList.adapter = userInfoAdapter
        infoList.layoutManager = LinearLayoutManager(this)
        userInfoAdapter.set(getInfo())

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

    private fun getInfo(): Collection<String> {
        val list = ArrayList<String>()
        list.add("Пол")
        list.add("Образование (ступень)")
        list.add("Направление образования")
        list.add("Род занятий (должность)")
        list.add("Любимые авторы/книги")
        list.add("Любимый формат книги (бумажная/телефон/планшет/читалка)")
        return list
    }

    companion object {
        private const val TAG = "SIGN UP"
    }
}
