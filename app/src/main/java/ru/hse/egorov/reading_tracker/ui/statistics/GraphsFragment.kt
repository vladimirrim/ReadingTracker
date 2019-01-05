package ru.hse.egorov.reading_tracker.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import kotlinx.android.synthetic.main.fragment_graphs.*
import kotlinx.android.synthetic.main.fragment_overall_statistics.*
import ru.hse.egorov.reading_tracker.R
import ru.hse.egorov.reading_tracker.ui.date.DateTranslator
import ru.hse.egorov.reading_tracker.ui.date.DateTranslator.Companion.MONTH_NOMINATIVE
import java.util.*


class GraphsFragment : Fragment(), DateTranslator {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_graphs, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpChartMinutesPerDay()
        setUpChartPagesPerDay()
        setUpChartTimeOfDay()
        setUpChartSessions()
    }

    private fun setUpChartSessions() {
        chartSessionsPerDay.setBackgroundResource(R.color.light)
        chartSessionsPerDay.xAxis.setDrawGridLines(false)
        chartSessionsPerDay.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chartSessionsPerDay.axisRight.isEnabled = false
        chartSessionsPerDay.axisLeft.axisMinimum = 0f
        chartSessionsPerDay.legend.isEnabled = false
        chartSessionsPerDay.axisLeft.labelCount = 3
        chartSessionsPerDay.xAxis.setAvoidFirstLastClipping(true)
        chartSessionsPerDay.description.isEnabled = false
        val data = getSessionsData()
        chartSessionsPerDay.xAxis.setLabelCount(data.size, true)
        chartSessionsPerDay.xAxis.setValueFormatter { value, _ ->
            return@setValueFormatter data[value.toInt()].xAxisValue
        }
        setSessionsData(data)
    }

    private fun getSessionsData(): ArrayList<ChartData> {
        val timePeriod = OverallStatisticsFragment.getStatisticsPeriod()
        val sessions = OverallStatisticsFragment.getSessionsForPeriod()
        val data = ArrayList<ChartData>()
        when (timePeriod) {
            resources.getString(R.string.all_time) -> {
                for (session in sessions) {
                    val sessionMonth = translateMonth(session.startTime.get(Calendar.MONTH), resources, MONTH_NOMINATIVE)
                    when {
                        data.isEmpty() -> data.add(ChartData(data.size.toFloat(), 1f, sessionMonth))
                        data.last().xAxisValue != sessionMonth -> {
                            val skippedMonths = ArrayDeque<ChartData>()
                            val skippedMonth = Calendar.getInstance()
                            skippedMonth.timeInMillis = session.startTime.timeInMillis
                            skippedMonth.add(Calendar.MONTH, -1)
                            while (translateMonth(skippedMonth.get(Calendar.MONTH), resources, MONTH_NOMINATIVE) != data.last().xAxisValue) {
                                skippedMonths.addFirst(GraphsFragment.ChartData(0f, 0f,
                                        translateMonth(skippedMonth.get(Calendar.MONTH), resources, MONTH_NOMINATIVE)))
                                skippedMonth.add(Calendar.MONTH, -1)
                            }
                            val start = data.size
                            data.addAll(skippedMonths)
                            for (i in start until data.size) {
                                data[i].xValue = i.toFloat()
                            }
                            data.add(GraphsFragment.ChartData(data.size.toFloat(), 1f, sessionMonth))
                        }
                        else -> data.last().yValue++
                    }
                }
            }
            resources.getString(R.string.year) -> {

            }
            resources.getString(R.string.month) -> {

            }
            resources.getString(R.string.week) -> {

            }
            resources.getString(R.string.day) -> {

            }
        }
        return data
    }

    private fun setSessionsData(dataList: List<ChartData>) {
        val values = ArrayList<Entry>()
        val colors = ArrayList<Int>()

        val green = Color.rgb(110, 190, 102)

        for (elem in dataList) {
            val entry = BarEntry(elem.xValue, elem.yValue)
            values.add(entry)
            colors.add(green)
        }

        val set: LineDataSet

        if (chartSessionsPerDay.data != null && chartSessionsPerDay.data.dataSetCount > 0) {
            set = chartPagesPerDay.data.getDataSetByIndex(0) as LineDataSet
            set.values = values
            chartSessionsPerDay.data.notifyDataChanged()
            chartSessionsPerDay.notifyDataSetChanged()
        } else {
            set = LineDataSet(values, "Values")
            set.setDrawFilled(true)
            set.fillColor = green
            set.colors = colors
            set.setValueTextColors(colors)

            val data = LineData(set)
            chartSessionsPerDay.data = data
        }

        chartSessionsPerDay.invalidate()
    }

    private fun setUpChartTimeOfDay() {
        chartTimeOfDay.axisLeft.isEnabled = false
        chartTimeOfDay.axisRight.isEnabled = false
        chartTimeOfDay.axisLeft.setDrawGridLines(false)
        chartTimeOfDay.axisRight.setDrawGridLines(false)
        chartTimeOfDay.xAxis.setDrawGridLines(false)
        chartTimeOfDay.xAxis.position = XAxis.XAxisPosition.BOTTOM
        val entries = ArrayList<BarEntry>()

        entries.add(BarEntry(0f, 18.5f, "Morning"))
        entries.add(BarEntry(1f, 26.7f, "Day"))
        entries.add(BarEntry(2f, 24.0f, "Evening"))
        entries.add(BarEntry(3f, 30.8f, "Night"))

        val set = BarDataSet(entries, "")
        set.color = ContextCompat.getColor(context!!, R.color.colorPrimary)
        val data = BarData(set)
        data.barWidth = 0.9f
        chartTimeOfDay.data = data
        chartTimeOfDay.legend.isEnabled = false
        chartTimeOfDay.invalidate()
    }

    private fun setUpChartPagesPerDay() {
        chartPagesPerDay.setBackgroundResource(R.color.light)
        chartPagesPerDay.xAxis.setDrawGridLines(false)
        chartPagesPerDay.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chartPagesPerDay.axisRight.isEnabled = false
        chartPagesPerDay.axisLeft.axisMinimum = 0f
        chartPagesPerDay.legend.isEnabled = false
        val data = ArrayList<ChartData>()
        data.add(ChartData(0f, 500f, "1"))
        data.add(ChartData(1f, 0f, "2"))
        data.add(ChartData(2f, 300f, "3"))
        data.add(ChartData(3f, 400f, "4"))
        data.add(ChartData(4f, 500f, "5"))
        chartPagesPerDay.xAxis.setValueFormatter { value, _ ->
            return@setValueFormatter data[value.toInt()].xAxisValue
        }
        setLineData(data)
    }

    private fun setLineData(dataList: List<ChartData>) {
        val values = ArrayList<Entry>()
        val colors = ArrayList<Int>()

        val pink = ContextCompat.getColor(context!!, R.color.pink)

        for (elem in dataList) {
            val entry = BarEntry(elem.xValue, elem.yValue)
            values.add(entry)
            colors.add(pink)
        }

        val set: LineDataSet

        if (chartPagesPerDay.data != null && chartPagesPerDay.data.dataSetCount > 0) {
            set = chartPagesPerDay.data.getDataSetByIndex(0) as LineDataSet
            set.values = values
            chartPagesPerDay.data.notifyDataChanged()
            chartPagesPerDay.notifyDataSetChanged()
        } else {
            set = LineDataSet(values, "Values")
            set.setDrawFilled(true)
            set.fillColor = pink
            set.colors = colors
            set.setValueTextColors(colors)

            val data = LineData(set)
            chartPagesPerDay.data = data
        }

        chartPagesPerDay.invalidate()
    }

    private fun setUpChartMinutesPerDay() {
        chartMinutesPerDay.setBackgroundColor(Color.WHITE)
        chartMinutesPerDay.xAxis.granularity = 1f
        chartMinutesPerDay.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chartMinutesPerDay.axisRight.isEnabled = false
        chartMinutesPerDay.axisLeft.labelCount = 5
        chartMinutesPerDay.legend.isEnabled = false

        val data = ArrayList<ChartData>()
        data.add(ChartData(0f, 500f, "1"))
        data.add(ChartData(1f, 0f, "2"))
        data.add(ChartData(2f, 300f, "3"))
        data.add(ChartData(3f, 400f, "4"))
        data.add(ChartData(4f, 500f, "5"))

        chartMinutesPerDay.xAxis.setValueFormatter { value, _ ->
            return@setValueFormatter data[value.toInt()].xAxisValue
        }

        setData(data)
    }

    private fun setData(dataList: List<ChartData>) {

        val values = ArrayList<Entry>()
        val colors = ArrayList<Int>()

        val darkBlue = ContextCompat.getColor(context!!, R.color.colorPrimary)

        for (elem in dataList) {
            val entry = Entry(elem.xValue, elem.yValue)
            values.add(entry)
            colors.add(darkBlue)
        }

        val set: LineDataSet

        if (chartMinutesPerDay.data != null && chartMinutesPerDay.data.dataSetCount > 0) {
            set = chartMinutesPerDay.data.getDataSetByIndex(0) as LineDataSet
            set.values = values
            chartMinutesPerDay.data.notifyDataChanged()
            chartMinutesPerDay.notifyDataSetChanged()
        } else {
            set = LineDataSet(values, "Values")
            set.setDrawFilled(true)
            set.fillColor = darkBlue
            set.colors = colors
            set.setValueTextColors(colors)

            val data = LineData(set)
            chartMinutesPerDay.data = data
        }

        chartMinutesPerDay.invalidate()
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