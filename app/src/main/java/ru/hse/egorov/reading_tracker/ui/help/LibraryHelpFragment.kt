package ru.hse.egorov.reading_tracker.ui.help

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_help.*
import kotlinx.android.synthetic.main.fragment_help.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.adapter.ViewPagerAdapter

class LibraryHelpFragment : Fragment() {
    private lateinit var pagerAdapter: ViewPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_help, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPager(view)
    }

    private fun setUpViewPager(view: View) {
        pagerAdapter = ViewPagerAdapter(childFragmentManager)
        helpPager.setPagingEnabled(true)
        pagerAdapter.addFragment(DeleteBookHelpFragment.newInstance(), "")
        pagerAdapter.addFragment(UndoDeleteBookHelpFragment.newInstance(), "")
        pagerAdapter.addFragment(EditBookHelpFragment.newInstance(), "")
        view.helpPager.adapter = pagerAdapter
    }

    companion object {
        fun newInstance() = LibraryHelpFragment()
    }
}