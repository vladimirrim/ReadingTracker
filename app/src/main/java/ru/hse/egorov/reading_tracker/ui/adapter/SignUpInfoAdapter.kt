package ru.hse.egorov.reading_tracker.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.hse.egorov.reading_tracker.R

class SignUpInfoAdapter : RecyclerView.Adapter<SignUpInfoAdapter.SignUpInfoViewHolder>() {
    private var userInfo: TextView? = null
    private val userInfoList = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignUpInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_signup_info, parent, false)
        return SignUpInfoViewHolder(view)
    }

    fun set(items: Collection<String>) {
        userInfoList.addAll(items)
    }

    fun get(): ArrayList<String> {
        return userInfoList
    }

    fun clear() {
        userInfoList.clear()
    }

    override fun getItemCount(): Int {
        return userInfoList.size
    }

    override fun onBindViewHolder(holder: SignUpInfoViewHolder, position: Int) {
        holder.bind(userInfoList[position])
    }

    inner class SignUpInfoViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        init {
            userInfo = itemView?.findViewById(R.id.userInfo)
        }

        fun getTextView(): TextView? {
            return userInfo
        }
        fun bind(info: String) {
            userInfo?.hint = info
        }
    }
}