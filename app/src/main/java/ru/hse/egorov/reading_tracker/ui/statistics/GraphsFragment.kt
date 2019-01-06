package ru.hse.egorov.reading_tracker.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.fragment_graphs.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.adapter.SessionAdapter.Companion.Session
import ru.hse.egorov.reading_tracker.ui.date.DateTranslator
import ru.hse.egorov.reading_tracker.ui.date.DateTranslator.Companion.MONTH_SHORT
import ru.hse.egorov.reading_tracker.ui.date.DateTranslator.Companion.WEEK_SHORT
import java.util.*
import kotlin.collections.ArrayList


class GraphsFragment : Fragment(), DateTranslator, StatisticsUpdater {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_graphs, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateStatistics()
    }

    override fun updateStatistics() {
        setUpChartMinutesPerDay()
        setUpChartPagesPerDay()
        setUpChartTimeOfDay()
        setUpChartSessions()
    }

    private fun setUpChartSessions() {
        if (chartSessionsPerDay == null)
            return

        setUpChart(chartSessionsPerDay)
        val data = getSessionsData { data, _ ->
            data.yValue++
        }
        setSessionsData(data, chartSessionsPerDay, Color.rgb(110, 190, 102))
        chartSessionsPerDay.xAxis.setLabelCount(data.size, true)
        chartSessionsPerDay.xAxis.setValueFormatter { value, _ ->
            return@setValueFormatter data[value.toInt()].xAxisValue
        }
        chartSessionsPerDay.axisLeft.setValueFormatter { value, _ ->
            return@setValueFormatter value.toInt().toString() + " подходов"
        }
    }

    private fun setUpChartPagesPerDay() {
        if (chartPagesPerDay == null)
            return

        setUpChart(chartPagesPerDay)
        val data = getSessionsData { data, session ->
            data.yValue += session.endPage - session.startPage
        }
        setSessionsData(data, chartPagesPerDay, ContextCompat.getColor(context!!, R.color.pink))
        chartPagesPerDay.xAxis.setLabelCount(data.size, true)
        chartPagesPerDay.xAxis.setValueFormatter { value, _ ->
            return@setValueFormatter data[value.toInt()].xAxisValue
        }
        chartPagesPerDay.axisLeft.setValueFormatter { value, _ ->
            return@setValueFormatter value.toInt().toString() + " страниц"
        }
    }

    private fun setUpChartMinutesPerDay() {
        if (chartMinutesPerDay == null)
            return

        setUpChart(chartMinutesPerDay)
        val data = getSessionsData { data, session ->
            data.yValue += session.duration
        }
        setSessionsData(data, chartMinutesPerDay, ContextCompat.getColor(context!!, R.color.colorPrimary))
        chartMinutesPerDay.xAxis.setLabelCount(data.size, true)
        chartMinutesPerDay.xAxis.setValueFormatter { value, _ ->
            return@setValueFormatter data[value.toInt()].xAxisValue
        }
        chartMinutesPerDay.axisLeft.setValueFormatter { value, _ ->
            return@setValueFormatter (value.toInt() / 60 / 60).toString() + " час " +
                    ((value.toInt() / 60) % 60).toString() + " мин"
        }
    }

    private fun setUpChart(chart: LineChart) {
        chart.setBackgroundResource(R.color.light)
        chart.xAxis.setDrawGridLines(false)
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.axisRight.isEnabled = false
        chart.axisLeft.axisMinimum = 0f
        chart.legend.isEnabled = false
        chart.axisLeft.setLabelCount(3, true)
        chart.xAxis.setAvoidFirstLastClipping(true)
        chart.description.isEnabled = false
    }

    private fun getSessionsData(sessionAccumulator: (ChartData, Session) -> Unit): ArrayList<ChartData> {
        val timePeriod = OverallStatisticsFragment.getStatisticsPeriod()
        val sessions = OverallStatisticsFragment.getSessionsForPeriod()
        return when (timePeriod) {
            resources.getString(R.string.all_time), resources.getString(R.string.year) -> {
                getSessionsDataForTimePeriod(Calendar.MONTH, sessions, sessionAccumulator) { month ->
                    translateMonth(month, resources, MONTH_SHORT)
                }
            }
            resources.getString(R.string.month) -> {
                getSessionsDataForTimePeriod(Calendar.DAY_OF_MONTH, sessions, sessionAccumulator) { dayOfMonth ->
                    dayOfMonth.toString()
                }
            }
            resources.getString(R.string.week) -> {
                getSessionsDataForTimePeriod(Calendar.DAY_OF_WEEK, sessions, sessionAccumulator) { dayOfWeek ->
                    translateDayOfTheWeek(dayOfWeek, resources, WEEK_SHORT)
                }
            }
            resources.getString(R.string.day) -> {
                getSessionsDataForTimePeriod(Calendar.HOUR_OF_DAY, sessions, sessionAccumulator) { hour ->
                    hour.toString()
                }
            }
            else -> throw IllegalArgumentException("Unknown time period.")
        }
    }

    private fun getSessionsDataForTimePeriod(timePeriod: Int, sessions: ArrayList<Session>,
                                             sessionAccumulator: (ChartData, Session) -> Unit,
                                             periodTranslator: (Int) -> String): ArrayList<ChartData> {
        val data = ArrayList<ChartData>()
        for (session in sessions.reversed()) {
            val sessionMonth = periodTranslator(session.startTime.get(timePeriod))
            when {
                data.isEmpty() -> data.add(ChartData(data.size.toFloat(), 1f, sessionMonth))
                data.last().xAxisValue != sessionMonth -> {
                    val skippedPeriods = ArrayDeque<ChartData>()
                    val skippedPeriod = Calendar.getInstance()
                    skippedPeriod.timeInMillis = session.startTime.timeInMillis
                    skippedPeriod.add(timePeriod, -1)
                    while (periodTranslator(skippedPeriod.get(timePeriod)) != data.last().xAxisValue) {
                        skippedPeriods.addFirst(ChartData(0f, 0f,
                                periodTranslator(skippedPeriod.get(timePeriod))))
                        skippedPeriod.add(timePeriod, -1)
                    }
                    val start = data.size
                    data.addAll(skippedPeriods)
                    for (i in start until data.size) {
                        data[i].xValue = i.toFloat()
                    }
                    data.add(ChartData(data.size.toFloat(), 0f, sessionMonth))
                    sessionAccumulator(data.last(), session)
                }
                else -> sessionAccumulator(data.last(), session)
            }
        }
        if (data.size == 1) {
            data.last().xValue = 1f
            val prev = Calendar.getInstance()
            prev.timeInMillis = sessions[0].startTime.timeInMillis
            prev.add(timePeriod, -1)
            data.add(0, ChartData(0f, 0f,
                    periodTranslator(prev.get(timePeriod))))
        }

        if (data.isEmpty()) {
            val skippedPeriods = ArrayDeque<ChartData>()
            val skippedPeriod = Calendar.getInstance()
            val lastPeriod = periodTranslator(skippedPeriod.get(timePeriod))
            skippedPeriod.add(timePeriod, -1)
            while (periodTranslator(skippedPeriod.get(timePeriod)) != lastPeriod) {
                skippedPeriods.addFirst(ChartData(0f, 0f,
                        periodTranslator(skippedPeriod.get(timePeriod))))
                skippedPeriod.add(timePeriod, -1)
            }
            val start = data.size
            data.addAll(skippedPeriods)
            for (i in start until data.size) {
                data[i].xValue = i.toFloat()
            }
            data.add(ChartData(data.size.toFloat(), 0f, lastPeriod))
        }
        return data
    }

    private fun setSessionsData(dataList: List<ChartData>, chart: LineChart, color: Int) {
        val values = ArrayList<Entry>()
        val colors = ArrayList<Int>()

        for (elem in dataList) {
            val entry = BarEntry(elem.xValue, elem.yValue)
            values.add(entry)
            colors.add(color)
        }

        val set = LineDataSet(values, "Values")
        set.setDrawFilled(true)
        set.fillColor = color
        set.colors = colors
        set.setValueTextColors(colors)

        val data = LineData(set)
        chart.xAxis.valueFormatter = null
        chart.data?.clearValues()
        chart.notifyDataSetChanged()
        chart.clear()
        chart.invalidate()
        chart.data = data

        chart.invalidate()
    }

    private fun setUpChartTimeOfDay() {
    }

    private class ChartData(var xValue: Float, var yValue: Float, val xAxisValue: String)

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         * */
        fun newInstance() = GraphsFragment()
    }
}