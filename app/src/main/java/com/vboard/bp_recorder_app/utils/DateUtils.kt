package com.example.step.helper

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun getCurrentMonth() = (android.text.format.DateFormat.format("MMM", Date()) as String)

fun getCurrentDateInStringFormat(): String =
    SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(Date())

fun getCurrentDate(): Date = Calendar.getInstance().time

fun getTime(): String = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date())

fun getIndex(): String = SimpleDateFormat("HH", Locale.ENGLISH).format(Date())

fun getDateRange(date:String): Pair<Date, Date> {
    var begining: Date
    var end: Date
    run {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        cal.time = sdf.parse(date) as Date

        cal[Calendar.DAY_OF_MONTH] = cal.getActualMinimum(Calendar.DAY_OF_MONTH)
        setTimeToBeginningOfDay(cal)
        begining = cal.time
    }
    run {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        cal.time = sdf.parse(date) as Date
        cal[Calendar.DAY_OF_MONTH] = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        setTimeToEndofDay(cal)
        end = cal.time
    }

    return Pair(begining, end)
}

private fun getCalendarForNow(): Calendar {
    val calendar = GregorianCalendar.getInstance()
    calendar.time = Date()
    return calendar
}

private fun setTimeToBeginningOfDay(calendar: Calendar) {
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0
}

private fun setTimeToEndofDay(calendar: Calendar) {
    calendar[Calendar.HOUR_OF_DAY] = 23
    calendar[Calendar.MINUTE] = 59
    calendar[Calendar.SECOND] = 59
    calendar[Calendar.MILLISECOND] = 999
}

fun getWeekStartDate(): Date {
    val calendar = Calendar.getInstance()
    while (calendar[Calendar.DAY_OF_WEEK] !== Calendar.MONDAY) {
        calendar.add(Calendar.DATE, -1)
    }
    val date = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(calendar.time)
    val inputSDF = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
    return inputSDF.parse(date)
}

fun getWeekEndDate(): Date {
    val calendar = Calendar.getInstance()
    while (calendar[Calendar.DAY_OF_WEEK] !== Calendar.MONDAY) {
        calendar.add(Calendar.DATE, 1)
    }
    calendar.add(Calendar.DATE, -1)
    val date = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(calendar.time)
    val inputSDF = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
    return inputSDF.parse(date)
}

fun getCurrentWeekDates(week: Int) :Pair<String,String> {
    val c = GregorianCalendar.getInstance()
    c.firstDayOfWeek = Calendar.MONDAY
    c[Calendar.DAY_OF_WEEK] = c.firstDayOfWeek
    c.add(Calendar.DAY_OF_WEEK, week)
    val df: DateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
    val startDate: String = df.format(c.time)
    c.add(Calendar.DAY_OF_MONTH, 6)
    val endDate: String = df.format(c.time)
    return Pair(startDate,endDate)
}

@SuppressLint("SimpleDateFormat")
fun getCalculatedMonths( month: Int): String {
    val c: Calendar = GregorianCalendar()
    c.add(Calendar.MONTH, -month)
    val sdfr = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
    return sdfr.format(c.time).toString()
}


fun getWeekDays(): Array<String?> {
    val format: DateFormat = SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH)
    val calendar = Calendar.getInstance()
    calendar.firstDayOfWeek = Calendar.MONDAY
    calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY

    val days = arrayOfNulls<String>(7)
    for (i in 0..6) {
        days[i] = format.format(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
    return days
}

fun incrementDateByOne(date: String): String {
    val c = Calendar.getInstance()
    c.time = stringToDate(date)
    c.add(Calendar.DATE, 1)
    return SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(c.time)
}

fun decrementDateByOne(date: String): String {
    val c = Calendar.getInstance()
    c.time = stringToDate(date)
    c.add(Calendar.DATE, -1)
    return SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH).format(c.time)
}
@SuppressLint("SimpleDateFormat")
 fun stringToDate(strDate:String) :Date{
    val format = SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH)
    return format.parse(strDate)
}