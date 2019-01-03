package ru.hse.egorov.reading_tracker.ui.dialog;

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import kotlinx.android.synthetic.main.dialog_session_time.view.*
import ru.hse.egorov.reading_tracker.R

class SessionTimeDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_session_time, null)
        builder.setView(view)
        val dialog = builder.create()
        var startTime = arguments!!["startTime"] as Int
        var endTime = arguments!!["endTime"] as Int
        setInitialTime(view, startTime, endTime)

        view.closeDialog.setOnClickListener {
            dialog.dismiss()
        }
        view.startTime.setOnTimeChangedListener { _, hours, minutes ->
            startTime = hours * 60 + minutes
        }
        view.endTime.setOnTimeChangedListener { _, hours, minutes ->
            endTime = hours * 60 + minutes
        }
        view.confirmDialog.setOnClickListener {
            sendResult(0, startTime, endTime)
            dialog.dismiss()
        }
        return dialog
    }

    private fun setInitialTime(view: View, startTime: Int,endTime: Int){
        view.startTime.setIs24HourView(true)
        view.endTime.setIs24HourView(true)
        view.startTime.hour = startTime / 60
        view.startTime.minute = startTime % 60
        view.endTime.hour = endTime / 60
        view.endTime.minute = endTime % 60
    }

    private fun sendResult(REQUEST_CODE: Int, startTime: Int, endTime: Int) {
        val intent = Intent()
        intent.putExtra("startTime", startTime)
        intent.putExtra("endTime", endTime)
        targetFragment!!.onActivityResult(
                targetRequestCode, REQUEST_CODE, intent)
    }
}