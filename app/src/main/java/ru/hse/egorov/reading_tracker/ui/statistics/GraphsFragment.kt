package ru.hse.egorov.reading_tracker.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
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

        setUpChartReadingPerDay()
        setUpChartPagesPerDay()
    }

    private fun setUpChartPagesPerDay() {
        chartPagesPerDay.setBackgroundResource(R.color.light)
        chartPagesPerDay.xAxis.setDrawGridLines(false)
        chartPagesPerDay.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chartPagesPerDay.axisRight.isEnabled = false
        chartPagesPerDay.axisLeft.axisMinimum = 0f
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

        val green = Color.rgb(110, 190, 102)

        for (elem in dataList) {
            val entry = BarEntry(elem.xValue, elem.yValue)
            values.add(entry)
            colors.add(green)
        }

        val set: LineDataSet

        if (chartPagesPerDay.data != null && chartPagesPerDay.data.dataSetCount > 0) {
            set = chartPagesPerDay.data.getDataSetByIndex(0) as LineDataSet
            set.values = values
            chartReadingPerDay.data.notifyDataChanged()
            chartReadingPerDay.notifyDataSetChanged()
        } else {
            set = LineDataSet(values, "Values")
            set.setDrawFilled(true)
            set.fillColor = green
            set.colors = colors
            set.setValueTextColors(colors)

            val data = LineData(set)
            chartPagesPerDay.data = data
        }

        chartReadingPerDay.invalidate()
    }

    private fun setUpChartReadingPerDay() {
        chartReadingPerDay.setBackgroundColor(Color.WHITE)
        chartReadingPerDay.xAxis.granularity = 1f
        chartReadingPerDay.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chartReadingPerDay.axisRight.isEnabled = false
        chartReadingPerDay.axisLeft.labelCount = 5

        val data = ArrayList<Data>()
        data.add(Data(0f, 500f, "1"))
        data.add(Data(1f, 0f, "2"))
        data.add(Data(2f, 300f, "3"))
        data.add(Data(3f, 400f, "4"))
        data.add(Data(4f, 500f, "5"))

        chartReadingPerDay.xAxis.setValueFormatter { value, _ ->
            return@setValueFormatter data[value.toInt()].xAxisValue
        }

        setData(data)
    }

    private fun setData(dataList: List<Data>) {

        val values = ArrayList<BarEntry>()
        val colors = ArrayList<Int>()

        val green = Color.rgb(110, 190, 102)

        for (elem in dataList) {
            val entry = BarEntry(elem.xValue, elem.yValue)
            values.add(entry)
            colors.add(green)
        }

        val set: BarDataSet

        if (chartReadingPerDay.data != null && chartReadingPerDay.data.dataSetCount > 0) {
            set = chartReadingPerDay.data.getDataSetByIndex(0) as BarDataSet
            set.values = values
            chartReadingPerDay.data.notifyDataChanged()
            chartReadingPerDay.notifyDataSetChanged()
        } else {
            set = BarDataSet(values, "Values")
            set.colors = colors
            set.setValueTextColors(colors)

            val data = BarData(set)
            data.setValueTextSize(13f)
            data.barWidth = 0.8f

            chartReadingPerDay.data = data
        }

        chartReadingPerDay.invalidate()
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