package ru.hse.egorov.reading_tracker.ui.book_library

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_search_results.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.action_bar.ActionBarSetter
import ru.hse.egorov.reading_tracker.ui.adapter.GoogleBooksAdapter
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher

class SearchResultsFragment : Fragment(), ActionBarSetter, FragmentLauncher {

    private val libraryAdapter = GoogleBooksAdapter()

    fun getAdapter() = libraryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_search_results, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setActionBar(view.context as AppCompatActivity)
        setUpAdapter(view)
    }

    override fun setActionBar(activity: AppCompatActivity) {
        setHasOptionsMenu(true)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_action_back)
    }

    private fun setUpAdapter(view: View) {
        view.results.layoutManager = LinearLayoutManager(context)
        view.results.adapter = libraryAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            setHasOptionsMenu(false)
            openTemporaryFragment(activity as AppCompatActivity, SearchBookByTittleFragment.newInstance(),
                    R.id.temporaryFragment)
        }
        return true
    }

    companion object {
        fun newInstance() = SearchResultsFragment()
    }
}