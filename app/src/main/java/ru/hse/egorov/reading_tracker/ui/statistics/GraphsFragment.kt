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
import ru.hse.egorov.reading_tracker.R


class GraphsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_graphs, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpChartMinutesPerDay()
        setUpChartPagesPerDay()
        setUpChartTimeOfDay()
        setUpChartSessionsPerDay()
    }

    private fun setUpChartSessionsPerDay() {
        chartSessionsPerDay.setBackgroundResource(R.color.light)
        chartSessionsPerDay.xAxis.setDrawGridLines(false)
        chartSessionsPerDay.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chartSessionsPerDay.axisRight.isEnabled = false
        chartSessionsPerDay.axisLeft.axisMinimum = 0f
        chartSessionsPerDay.legend.isEnabled = false
        val data = ArrayList<Data>()
        data.add(Data(0f, 500f, "1"))
        data.add(Data(1f, 0f, "2"))
        data.add(Data(2f, 300f, "3"))
        data.add(Data(3f, 400f, "4"))
        data.add(Data(4f, 500f, "5"))
        chartSessionsPerDay.xAxis.setValueFormatter { value, _ ->
            return@setValueFormatter data[value.toInt()].xAxisValue
        }
        setSessionsData(data)
    }

    private fun setSessionsData(dataList: List<Data>) {
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
        val data = ArrayList<Data>()
        data.add(Data(0f, 500f, "1"))
        data.add(Data(1f, 0f, "2"))
        data.add(Data(2f, 300f, "3"))
        data.add(Data(3f, 400f, "4"))
        data.add(Data(4f, 500f, "5"))
        chartPagesPerDay.xAxis.setValueFormatter { value, _ ->
            return@setValueFormatter data[value.toInt()].xAxisValue
        }
        setLineData(data)
    }

    private fun setLineData(dataList: List<Data>) {
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

        val data = ArrayList<Data>()
        data.add(Data(0f, 500f, "1"))
        data.add(Data(1f, 0f, "2"))
        data.add(Data(2f, 300f, "3"))
        data.add(Data(3f, 400f, "4"))
        data.add(Data(4f, 500f, "5"))

        chartMinutesPerDay.xAxis.setValueFormatter { value, _ ->
            return@setValueFormatter data[value.toInt()].xAxisValue
        }

        setData(data)
    }

    private fun setData(dataList: List<Data>) {

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

    private inner class Data internal constructor(internal val xValue: Float, internal val yValue: Float, internal val xAxisValue: String)

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         * */
        fun newInstance() = GraphsFragment()
    }
}