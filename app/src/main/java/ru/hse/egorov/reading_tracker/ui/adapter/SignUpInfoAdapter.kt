package ru.hse.egorov.reading_tracker.ui.adapter

import android.support.v7.widget.RecyclerView
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.hse.egorov.reading_tracker.R
import android.text.Editable


class SignUpInfoAdapter(private val infoMap: MutableMap<String, String?>) : RecyclerView.Adapter<SignUpInfoAdapter.SignUpInfoViewHolder>() {
    private var userInfo: TextView? = null
    private val userInfoList = ArrayList<String>()
    private val infoKeys = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignUpInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_signup_info, parent, false)
        return SignUpInfoViewHolder(view)
    }

    fun set(hints: Collection<String>, keys: Collection<String>) {
        userInfoList.addAll(hints)
        infoKeys.addAll(keys)
    }

    fun get(): ArrayList<String> {
        return userInfoList
    }

    override fun getItemCount(): Int {
        return userInfoList.size
    }

    override fun onBindViewHolder(holder: SignUpInfoViewHolder, position: Int) {
        holder.bind(userInfoList[position], infoKeys[position])
    }

    inner class SignUpInfoViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        init {
            userInfo = itemView?.findViewById(R.id.userInfo)


        }

        fun bind(info: String, key: String) {
            if (userInfo?.hint == null)
                userInfo?.hint = info
            userInfo?.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    infoMap[key] = s.toString()
                }

            })
        }
    }
}