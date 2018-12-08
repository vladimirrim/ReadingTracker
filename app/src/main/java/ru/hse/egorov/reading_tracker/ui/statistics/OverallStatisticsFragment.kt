package ru.hse.egorov.reading_tracker.ui.statistics

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.fragment_overall_statistics.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.action_bar.ActionBarSetter
import ru.hse.egorov.reading_tracker.ui.adapter.ViewPagerAdapter
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher

class OverallStatisticsFragment : Fragment(), ActionBarSetter, FragmentLauncher {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_overall_statistics, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPager(view)
    }

    private fun setUpViewPager(view: View) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(SessionsStatisticsFragment.newInstance(), "Записи")
        adapter.addFragment(BooksStatisticsFragment.newInstance(), "По книгам")
        adapter.addFragment(GraphsFragment.newInstance(), "Графики")
        view.statisticsPager.adapter = adapter
    }

    override fun setActionBar(activity: AppCompatActivity) {
        setHasOptionsMenu(true)
        activity.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        activity.supportActionBar?.setCustomView(R.layout.statistics_action_bar)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.statistics_action_bar, menu)
        val toProfileButton = menu?.getItem(0)
        toProfileButton?.setOnMenuItemClickListener {
            //TODO
            true
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        fun newInstance() = OverallStatisticsFragment()
    }
}