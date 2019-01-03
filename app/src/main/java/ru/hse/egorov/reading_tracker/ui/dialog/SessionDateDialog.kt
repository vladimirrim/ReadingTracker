package ru.hse.egorov.reading_tracker.ui.dialog;

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.dialog_session_date.view.*
import ru.hse.egorov.reading_tracker.R
import java.util.*


class SessionDateDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_session_date, null)
        builder.setView(view)
        val dialog = builder.create()
        var selectedDate = Calendar.getInstance()
        view.closeDialog.setOnClickListener {
            dialog.dismiss()
        }
        view.datePicker.setOnDateChangeListener { _, year, month, day ->
            selectedDate = GregorianCalendar(year, month, day)
        }
        view.confirmDialog.setOnClickListener {
            sendResult(0, selectedDate.timeInMillis)
            dialog.dismiss()
        }
        return dialog
    }

    private fun sendResult(REQUEST_CODE: Int, date: Long) {
        val intent = Intent()
        intent.putExtra("date", date)
        targetFragment!!.onActivityResult(
                targetRequestCode, REQUEST_CODE, intent)
    }
}