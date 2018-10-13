package ru.hse.egorov.reading_tracker.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up_personal_info.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.MainActivity
import ru.hse.egorov.reading_tracker.ui.adapter.SignUpInfoAdapter

class SignUpPersonalInfoActivity : AppCompatActivity() {
    private val infoMap = HashMap<String, String?>()
    private val userInfoAdapter = SignUpInfoAdapter(infoMap)
    private val dbManager = DatabaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_personal_info)

        userInfoAdapter.set(getHintsInfo(), getInfoKeys())
        infoList.adapter = userInfoAdapter
        infoList.layoutManager = LinearLayoutManager(this)

        signUpButton.setOnClickListener { _ ->
            if (checkAllFieldsFilled()) {
                dbManager.addUserInfo(getInfo()).addOnSuccessListener {
                    Log.d(TAG, "addUserSignUpInfo:success")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Please fill all fields.\n $" +
                        "${getGender()}" +
                        "${getDegree()}" +
                        "${getMajor()}" +
                        "${getOccupation()}" +
                        "${getFavBooksAndAuthors()}" +
                        "${getFavBookFormat()}",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkAllFieldsFilled(): Boolean {
        if (getGender() == null) {
            return false
        }
        if (getDegree() == null) {
            return false
        }
        if (getMajor() == null) {
            return false
        }
        if (getOccupation() == null) {
            return false
        }
        if (getFavBooksAndAuthors() == null) {
            return false
        }
        if (getFavBookFormat() == null) {
            return false
        }
        return true
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

    private fun getInfoKeys(): Collection<String> {
        val list = ArrayList<String>()
        list.add("gender")
        list.add("degree")
        list.add("major")
        list.add("occupation")
        list.add("favorite books and authors")
        list.add("favorite book format")
        return list
    }

    private fun getInfo(): HashMap<String, String?> {
        return infoMap
    }

    private fun getGender(): String? {
        return infoMap["gender"]
    }

    private fun getDegree(): String? {
        return infoMap["degree"]
    }

    private fun getMajor(): String? {
        return infoMap["major"]
    }

    private fun getOccupation(): String? {
        return infoMap["occupation"]
    }

    private fun getFavBooksAndAuthors(): String? {
        return infoMap["favorite books and authors"]
    }

    private fun getFavBookFormat(): String? {
        return infoMap["favorite book format"]
    }

    companion object {
        private const val TAG = "SignUpInfo"
    }
}
