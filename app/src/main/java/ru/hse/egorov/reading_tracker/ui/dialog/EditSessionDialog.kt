package ru.hse.egorov.reading_tracker.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.dialog_edit_session.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.statistics.edit_session.EditSessionInfoFragment
import ru.hse.egorov.reading_tracker.ui.statistics.edit_session.EditSessionTimeFragment

class EditSessionDialog : DialogFragment(), FragmentLauncher {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_edit_session, null)
        builder.setView(view)
        val dialog = builder.create()

        view.closeDialog.setOnClickListener {
            dialog.dismiss()
        }

        view.changeTime.setOnClickListener {
            openTemporaryFragment(activity as AppCompatActivity, EditSessionTimeFragment.newInstance().apply {
                arguments = this@EditSessionDialog.arguments
            }, R.id.temporaryFragment)
        }

        view.changeRating.setOnClickListener {
            openTemporaryFragment(activity as AppCompatActivity, EditSessionInfoFragment.newInstance().apply {
                arguments = this@EditSessionDialog.arguments
            }, R.id.temporaryFragment)
        }
        return dialog
    }
}