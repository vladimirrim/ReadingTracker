package ru.hse.egorov.reading_tracker.ui.statistics

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_overall_statistics.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.adapter.ViewPagerAdapter
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryFragment
import ru.hse.egorov.reading_tracker.ui.book_library.LibraryWelcomeFragment
import ru.hse.egorov.reading_tracker.ui.session.StartOfSessionFragment

class OverallStatisticsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_overall_statistics, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ViewPagerAdapter(activity!!.supportFragmentManager)
        adapter.addFragment(LibraryWelcomeFragment.newInstance(), "Записи")
        adapter.addFragment(StartOfSessionFragment.newInstance(), "По книгам")
        adapter.addFragment(LibraryFragment.newInstance(), "Графики")
        view.statisticsPager.adapter = adapter
    }

    companion object {
        fun newInstance() = OverallStatisticsFragment()
    }
}