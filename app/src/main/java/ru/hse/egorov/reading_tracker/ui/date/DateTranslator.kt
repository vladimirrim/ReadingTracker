package ru.hse.egorov.reading_tracker.ui.date

import android.content.res.Resources
import ru.hse.egorov.reading_tracker.R
import java.util.*

interface DateTranslator {
    fun translateMonth(month: Int, resources: Resources): String {
        return when (month) {
            Calendar.JANUARY -> resources.getQuantityString(R.plurals.january, 1)
            Calendar.FEBRUARY -> resources.getQuantityString(R.plurals.february, 1)
            Calendar.MARCH -> resources.getQuantityString(R.plurals.march, 1)
            Calendar.APRIL -> resources.getQuantityString(R.plurals.april, 1)
            Calendar.MAY -> resources.getQuantityString(R.plurals.may, 1)
            Calendar.JUNE -> resources.getQuantityString(R.plurals.june, 1)
            Calendar.JULY -> resources.getQuantityString(R.plurals.july, 1)
            Calendar.AUGUST -> resources.getQuantityString(R.plurals.august, 1)
            Calendar.SEPTEMBER -> resources.getQuantityString(R.plurals.september, 1)
            Calendar.OCTOBER -> resources.getQuantityString(R.plurals.october, 1)
            Calendar.NOVEMBER -> resources.getQuantityString(R.plurals.november, 1)
            Calendar.DECEMBER -> resources.getQuantityString(R.plurals.december, 1)
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
}