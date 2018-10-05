package ru.hse.egorov.reading_tracker

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*
import ru.hse.egorov.reading_tracker.adapter.SignUpInfoAdapter
import ru.hse.egorov.reading_tracker.database.DatabaseManager

/**
 * A login screen that offers login via email/password.
 */
class SignUpActivity : AppCompatActivity() {

    private val userInfoAdapter = SignUpInfoAdapter()
    private val dbManager = DatabaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        infoList.adapter = userInfoAdapter
        infoList.layoutManager = LinearLayoutManager(this)
        userInfoAdapter.set(getHintsInfo())

        signUpButton.setOnClickListener { _ ->
            if (checkAllFieldsFilled()) {
                dbManager.auth(email.text.toString(), password.text.toString())?.addOnCompleteListener(this) { it ->
                    if (it.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        dbManager.addUserInfo(getInfo()).addOnSuccessListener {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", it.exception)
                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkAllFieldsFilled(): Boolean {
        for (i in 0 until userInfoAdapter.itemCount) {
            val holder = infoList.findViewHolderForAdapterPosition(i) as SignUpInfoAdapter.SignUpInfoViewHolder
            holder.getTextView() ?: return false
        }
        return email.text != null &&
                password.text != null


    }

    private fun getHintsInfo(): Collection<String> {
        val list = ArrayList<String>()
        list.add("Пол")
        list.add("Образование (ступень)")
        list.add("Направление образования")
        list.add("Род занятий (должность)")
        list.add("Любимые авторы/книги")
        list.add("Любимый формат книги (бумажная/телефон/планшет/читалка)")
        return list
    }

    private fun getInfo(): MutableMap<String, String> {
        val map = HashMap<String, String>()
        //TODO("get user data")
        map["gender"] = ("Пол")
        map["degree"] = ("Образование (ступень)")
        map["major"] = ("Направление образования")
        map["occupation"] = "Род занятий (должность)"
        map["favorite books and authors"] = "Любимые авторы/книги"
        map["favorite book format"] = "Любимый формат книги (бумажная/телефон/планшет/читалка)"
        return map

    }

    companion object {
        private const val TAG = "SIGN UP"
    }
}
