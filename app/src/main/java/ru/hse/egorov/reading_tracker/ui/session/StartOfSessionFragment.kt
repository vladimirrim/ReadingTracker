package ru.hse.egorov.reading_tracker.ui.session

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.fragment_start_of_session.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.MainActivity
import ru.hse.egorov.reading_tracker.ui.book_library.AddingBookFragment

class StartOfSessionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_start_of_session, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.addBook.setOnClickListener {
            openFragment(AddingBookFragment.newInstance(), activity as AppCompatActivity)
        }
    }
    companion object {
        fun newInstance(): StartOfSessionFragment = StartOfSessionFragment()
    }

    private fun openFragment(fragment: Fragment, activity: AppCompatActivity) {
        activity.supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit()
    }
}
