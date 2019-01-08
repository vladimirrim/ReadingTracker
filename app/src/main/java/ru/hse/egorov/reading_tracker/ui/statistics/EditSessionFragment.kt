package ru.hse.egorov.reading_tracker.ui.statistics


import android.app.ActionBar
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.book.*
import kotlinx.android.synthetic.main.fragment_edit_session.*
import kotlinx.android.synthetic.main.session_time.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.database.DatabaseManager
import ru.hse.egorov.reading_tracker.ui.MainActivity.Companion.PROFILE_FRAGMENT_POSITION
import ru.hse.egorov.reading_tracker.ui.action_bar.ActionBarSetter
import ru.hse.egorov.reading_tracker.ui.dialog.EditSessionDialog
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher


class EditSessionFragment : Fragment(), ActionBarSetter, FragmentLauncher {
    private val dbManager = DatabaseManager()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_edit_session, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        setActionBar(view.context as AppCompatActivity)
        setUpSession()
    }

    private fun setUpSession() {
        date.text = arguments!!["date"] as String
        dayOfTheWeek.text = arguments!!["dayOfTheWeek"] as String
        author.text = arguments!!["author"] as String
        title.text = arguments!!["title"] as String
        comment.text = arguments!!["comment"] as String
        startTime.hours.text = arguments!!["startTimeHours"] as String
        startTime.minutes.text = arguments!!["startTimeMinutes"] as String
        endTime.hours.text = arguments!!["endTimeHours"] as String
        endTime.minutes.text = arguments!!["endTimeMinutes"] as String
        hours.text = arguments!!["hours"] as String
        if (hours.text == "") hoursStatic.visibility = View.GONE
        minutes.text = arguments!!["minutes"] as String
        if (arguments!!["place"] as String == "") placeFlag.visibility = View.GONE
        if (arguments!!["emotion"] as String == "") emotionFlag.visibility = View.GONE

        if (arguments!!["startPage"] != null && arguments!!["endPage"] != null) {
            val startPage = arguments!!["startPage"] as Int
            val endPage = arguments!!["endPage"] as Int
            val pageCount = endPage - startPage
            val pagesText = pageCount.toString() + " " + PAGES + ", " + startPage.toString() + " - " + endPage.toString()
            readPages.text = pagesText
        } else {
            readPages.visibility = View.GONE
        }

        dbManager.getBookCover(arguments!!["bookId"] as String, context!!).into(cover)
    }

    override fun setActionBar(activity: AppCompatActivity) {
        setHasOptionsMenu(true)
        activity.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_TITLE
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.title = ACTION_BAR_TITLE
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.edit_session_action_bar, menu)
        menu?.getItem(0)?.setOnMenuItemClickListener {
            EditSessionDialog().show(childFragmentManager, "Edit Session")
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            setHasOptionsMenu(false)
            (activity?.supportFragmentManager?.findFragmentByTag("android:switcher:" + R.id.fragmentPager + ":"
                    + PROFILE_FRAGMENT_POSITION) as ActionBarSetter).setActionBar(activity as AppCompatActivity)
            openPagerFragment(activity as AppCompatActivity, PROFILE_FRAGMENT_POSITION)
        }
        return true
    }

    companion object {
        private const val PAGES = "страниц"
        private const val ACTION_BAR_TITLE = "Запись о чтении"

        fun newInstance() = EditSessionFragment()
    }
}
