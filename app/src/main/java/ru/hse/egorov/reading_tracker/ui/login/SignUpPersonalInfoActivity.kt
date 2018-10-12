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
    private val userInfoAdapter = SignUpInfoAdapter()
    private val dbManager = DatabaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_personal_info)

        infoList.adapter = userInfoAdapter
        infoList.layoutManager = LinearLayoutManager(this)
        userInfoAdapter.set(getHintsInfo())

        signUpButton.setOnClickListener { _ ->
            if (checkAllFieldsFilled()) {
                dbManager.addUserInfo(getInfo()).addOnSuccessListener {
                    Log.d(TAG, "addUserSignUpInfo:success")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Please fill all fields.",
                        Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkAllFieldsFilled(): Boolean {
        for (i in 0 until userInfoAdapter.itemCount) {
            return getTextFromHolderByPosition(i) != ""
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

    private fun getInfo(): HashMap<String, String?> {
        val map = HashMap<String, String?>()
        map["gender"] = getGender()
        map["degree"] = getDegree()
        map["major"] = getMajor()
        map["occupation"] = getOccupation()
        map["favorite books and authors"] = getFavBooksAndAuthors()
        map["favorite book format"] = getFavBookFormat()
        return map
    }

    private fun getGender(): String? = getTextFromHolderByPosition(0)

    private fun getDegree(): String? = getTextFromHolderByPosition(1)

    private fun getMajor(): String? = getTextFromHolderByPosition(2)

    private fun getOccupation(): String? = getTextFromHolderByPosition(3)

    private fun getFavBooksAndAuthors(): String? = getTextFromHolderByPosition(4)

    private fun getFavBookFormat(): String? = getTextFromHolderByPosition(5)

    private fun getTextFromHolderByPosition(position: Int): String {
        //TODO fix NPE when items not visible
        val holder = infoList.getChildViewHolder(infoList.getChildAt(position)) as SignUpInfoAdapter.SignUpInfoViewHolder
        return holder.getTextView()?.text.toString()
    }

    companion object {
        private const val TAG = "SignUpInfo"
    }
}
