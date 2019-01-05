package ru.hse.egorov.reading_tracker.ui.date

import android.content.res.Resources
import ru.hse.egorov.reading_tracker.R
import java.util.*

interface DateTranslator {
    fun translateMonth(month: Int, resources: Resources, ending: Int): String {
        return when (month) {
            Calendar.JANUARY -> resources.getQuantityString(R.plurals.january, ending)
            Calendar.FEBRUARY -> resources.getQuantityString(R.plurals.february, ending)
            Calendar.MARCH -> resources.getQuantityString(R.plurals.march, ending)
            Calendar.APRIL -> resources.getQuantityString(R.plurals.april, ending)
            Calendar.MAY -> resources.getQuantityString(R.plurals.may, ending)
            Calendar.JUNE -> resources.getQuantityString(R.plurals.june, ending)
            Calendar.JULY -> resources.getQuantityString(R.plurals.july, ending)
            Calendar.AUGUST -> resources.getQuantityString(R.plurals.august, ending)
            Calendar.SEPTEMBER -> resources.getQuantityString(R.plurals.september, ending)
            Calendar.OCTOBER -> resources.getQuantityString(R.plurals.october, ending)
            Calendar.NOVEMBER -> resources.getQuantityString(R.plurals.november, ending)
            Calendar.DECEMBER -> resources.getQuantityString(R.plurals.december,ending)
            else -> ""
        }
    }

    fun translateDayOfTheWeek(day: Int, resources: Resources): String {
        return when (day) {
            Calendar.MONDAY -> resources.getString(R.string.monday)
            Calendar.TUESDAY -> resources.getString(R.string.tuesday)
            Calendar.WEDNESDAY -> resources.getString(R.string.wednesday)
            Calendar.THURSDAY -> resources.getString(R.string.thursday)
            Calendar.FRIDAY -> resources.getString(R.string.friday)
            Calendar.SATURDAY -> resources.getString(R.string.saturday)
            Calendar.SUNDAY -> resources.getString(R.string.sunday)
            else -> ""
        }
    }

    companion object {
        const val MONTH_NOMINATIVE = 0
        const val MONTH_GENITIVE = 1
    }
}