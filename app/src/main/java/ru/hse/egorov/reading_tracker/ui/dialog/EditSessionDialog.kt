package ru.hse.egorov.reading_tracker.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.dialog_edit_session.view.*
import ru.hse.egorov.reading_tracker.R

class EditSessionDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_edit_session, null)
        builder.setView(view)
        val dialog = builder.create()
        view.closeDialog.setOnClickListener {
            dialog.dismiss()
        }
        return dialog
    }
}