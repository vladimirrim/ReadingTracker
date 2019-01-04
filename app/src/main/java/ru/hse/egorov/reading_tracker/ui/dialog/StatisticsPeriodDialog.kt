package ru.hse.egorov.reading_tracker.ui.dialog

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.dialog_statistics_period.view.*
import ru.hse.egorov.reading_tracker.R
import java.util.*

class StatisticsPeriodDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_statistics_period, null)
        builder.setView(view)
        val dialog = builder.create()
        val thresholdDate = Calendar.getInstance()

        view.allTime.setOnClickListener {
            thresholdDate.add(Calendar.YEAR, -10)
            sendResult(0, resources.getString(R.string.all_time), thresholdDate.timeInMillis)
            dialog.dismiss()
        }

        view.year.setOnClickListener {
            thresholdDate.add(Calendar.YEAR, -1)
            sendResult(0, resources.getString(R.string.year), thresholdDate.timeInMillis)
            dialog.dismiss()
        }

        view.month.setOnClickListener {
            thresholdDate.add(Calendar.MONTH, -1)
            sendResult(0, resources.getString(R.string.month), thresholdDate.timeInMillis)
            dialog.dismiss()
        }

        view.week.setOnClickListener {
            thresholdDate.add(Calendar.DATE, -7)
            sendResult(0, resources.getString(R.string.week), thresholdDate.timeInMillis)
            dialog.dismiss()
        }

        view.day.setOnClickListener {
            thresholdDate.add(Calendar.DATE, -1)
            sendResult(0, resources.getString(R.string.day), thresholdDate.timeInMillis)
            dialog.dismiss()
        }

        return dialog
    }

    private fun sendResult(REQUEST_CODE: Int, selectedPeriod: String, lowerBound: Long) {
        val intent = Intent()
        intent.putExtra("selectedPeriod", selectedPeriod)
        intent.putExtra("lowerBound", lowerBound)
        targetFragment!!.onActivityResult(
                targetRequestCode, REQUEST_CODE, intent)
    }
}