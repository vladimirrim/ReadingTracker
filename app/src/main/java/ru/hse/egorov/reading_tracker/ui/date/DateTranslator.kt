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
            Calendar.DECEMBER -> resources.getQuantityString(R.plurals.december, ending)
            else -> ""
        }
    }

    fun translateDayOfTheWeek(day: Int, resources: Resources, version: Int): String {
        return when (day) {
            Calendar.MONDAY -> resources.getQuantityString(R.plurals.monday, version)
            Calendar.TUESDAY -> resources.getQuantityString(R.plurals.tuesday, version)
            Calendar.WEDNESDAY -> resources.getQuantityString(R.plurals.wednesday, version)
            Calendar.THURSDAY -> resources.getQuantityString(R.plurals.thursday, version)
            Calendar.FRIDAY -> resources.getQuantityString(R.plurals.friday, version)
            Calendar.SATURDAY -> resources.getQuantityString(R.plurals.saturday, version)
            Calendar.SUNDAY -> resources.getQuantityString(R.plurals.sunday, version)
            else -> ""
        }
    }

    companion object {
        const val MONTH_NOMINATIVE = 5
        const val MONTH_GENITIVE = 1
        const val MONTH_SHORT = 2
        const val WEEK_FULL = 1
        const val WEEK_SHORT = 2
    }
}