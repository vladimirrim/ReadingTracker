package ru.hse.egorov.reading_tracker.ui.dialog;

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.TextView
import kotlinx.android.synthetic.main.dialog_session_time.view.*
import ru.hse.egorov.reading_tracker.R
import java.util.*

class SessionTimeDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_session_time, null)
        builder.setView(view)
        val dialog = builder.create()
        val selectedDate = Calendar.getInstance()
        view.closeDialog.setOnClickListener {
            dialog.dismiss()
        }
        view.startTime.setIs24HourView(true)
        view.endTime.setIs24HourView(true)
        view.confirmDialog.setOnClickListener {
            activity?.findViewById<TextView>(R.id.sessionDate)?.text = selectedDate.get(Calendar.DAY_OF_MONTH).toString()
            dialog.dismiss()
        }
        return dialog
    }
}