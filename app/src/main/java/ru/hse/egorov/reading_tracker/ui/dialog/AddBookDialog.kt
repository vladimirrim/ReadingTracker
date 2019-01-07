package ru.hse.egorov.reading_tracker.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.dialog_add_book.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.book_library.AddingBookFragment
import ru.hse.egorov.reading_tracker.ui.book_library.SearchBookByTittleFragment
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher

class AddBookDialog : DialogFragment(), FragmentLauncher {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_add_book, null)
        builder.setView(view)
        val dialog = builder.create()
        view.manualAdd.setOnClickListener {
            openTemporaryFragment(activity as AppCompatActivity, AddingBookFragment.newInstance(), R.id.temporaryFragment)
            dialog.dismiss()
        }
        view.findByName.setOnClickListener {
            openTemporaryFragment(activity as AppCompatActivity, SearchBookByTittleFragment.newInstance(), R.id.temporaryFragment)
            dialog.dismiss()
        }
        return dialog
    }
}