package ru.hse.egorov.reading_tracker.ui.statistics

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.fragment_overall_statistics.*
import kotlinx.android.synthetic.main.fragment_overall_statistics.view.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.action_bar.ActionBarSetter
import ru.hse.egorov.reading_tracker.ui.adapter.SessionAdapter.Companion.Session
import ru.hse.egorov.reading_tracker.ui.adapter.ViewPagerAdapter
import ru.hse.egorov.reading_tracker.ui.dialog.StatisticsPeriodDialog
import ru.hse.egorov.reading_tracker.ui.fragment.FragmentLauncher
import ru.hse.egorov.reading_tracker.ui.profile.ProfileFragment


class OverallStatisticsFragment : Fragment(), ActionBarSetter, FragmentLauncher {
    private lateinit var pagerAdapter: ViewPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_overall_statistics, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPager(view)

        view.statisticsPeriod.setOnClickListener {
            val dispatchedFragment = StatisticsPeriodDialog()
            dispatchedFragment.setTargetFragment(this, STATISTICS_PERIOD_RC)
            dispatchedFragment.show(fragmentManager, "Statistics Period")
        }

        view.arrowDown.setOnClickListener {
            it.visibility = View.GONE
            arrowLeft.visibility = View.VISIBLE
            arrowRight.visibility = View.VISIBLE
            view.totalStatistics.visibility = View.VISIBLE
            view.totalStatistics.alpha = 0.0f
            view.totalStatistics.animate()
                    .translationY(0f)
                    .alpha(1.0f)
                    .setListener(null)
        }

        view.totalStatistics.setOnTouchListener { container, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                arrowLeft.visibility = View.GONE
                arrowRight.visibility = View.GONE

                container.animate()
                        .translationY(-container.height.toFloat())
                        .alpha(0.0f)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                super.onAnimationEnd(animation)
                                container.visibility = View.GONE
                                view.arrowDown.visibility = View.VISIBLE
                            }
                        })
            }
            true
        }
    }

    private fun setUpViewPager(view: View) {
        pagerAdapter = ViewPagerAdapter(childFragmentManager)
        pagerAdapter.addFragment(SessionsStatisticsFragment.newInstance(), "Записи")
        pagerAdapter.addFragment(BooksStatisticsFragment.newInstance(), "По книгам")
        pagerAdapter.addFragment(GraphsFragment.newInstance(), "Графики")
        view.statisticsPager.adapter = pagerAdapter
    }

    override fun setActionBar(activity: AppCompatActivity) {
        setHasOptionsMenu(true)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        activity.supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        activity.supportActionBar?.setCustomView(R.layout.statistics_action_bar)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.statistics_action_bar, menu)
        menu?.getItem(0)?.setOnMenuItemClickListener {
            openTemporaryFragment(activity as AppCompatActivity, ProfileFragment.newInstance(), R.id.temporaryFragment)
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            STATISTICS_PERIOD_RC -> {
                statisticsPeriod.text = data!!.getStringExtra("selectedPeriod")
                timePeriod = statisticsPeriod.text.toString()
                setSessionsForPeriod(data.getLongExtra("thresholdDate", 0))
                for (i in 0 until pagerAdapter.count) {
                    (pagerAdapter.getItem(i) as StatisticsUpdater).updateStatistics()
                }
            }
        }
    }

    private fun setSessionsForPeriod(thresholdDate: Long) {
        sessionsForPeriod.clear()
        for (session in allSessions) {
            if (session.startTime.timeInMillis >= thresholdDate) {
                sessionsForPeriod.add(session)
            }
        }
    }

    companion object {
        private const val STATISTICS_PERIOD_RC = 1
        private val allSessions = ArrayList<Session>()
        private val sessionsForPeriod = ArrayList<Session>()
        private var timePeriod = "Всё время"

        fun newInstance() = OverallStatisticsFragment()

        fun getSessionsForPeriod() = sessionsForPeriod

        fun getAllSessions() = allSessions

        fun getStatisticsPeriod() = timePeriod
    }
}