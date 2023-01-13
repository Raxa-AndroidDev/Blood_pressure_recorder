package com.vboard.bp_recorder_app.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.MutableLiveData
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*




fun DateLongtoString(longDate:Long):String{

    val date = Date(longDate)

    val dateFormat = SimpleDateFormat("dd/MMM/yy", Locale.getDefault())
     return dateFormat.format(date)
}
fun Date.toString(): String {
    val formatter = SimpleDateFormat(Constansts.dateFormate, Locale.getDefault())
    return formatter.format(this)
}

fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}


fun DateToCalendar(date: Date?): Calendar? {
    val cal = Calendar.getInstance()
    cal.time = date
    return cal
}
fun getNextDayDate(calendar: Calendar):String{
    calendar.add(Calendar.DAY_OF_MONTH,1)

    return CurrentDate(calendar)
}

fun getPreviousDayDate(calendar: Calendar):String{
    calendar.add( Calendar.DAY_OF_MONTH,-1)

    return CurrentDate(calendar)
}


fun CurrentDate(calendar: Calendar): String {

    val dateFormat = SimpleDateFormat("dd/MMM/yy", Locale.getDefault())
    return dateFormat.format(calendar.time)
}


fun dateAndTime(): String {
    val datecurrent = Date()
    val dateFormatcurrent = SimpleDateFormat("dd-MM-yyyy hh:mm:ss", Locale.getDefault())
    return dateFormatcurrent.format(datecurrent)
}

fun DatePickerDialog(context: Context, calendar: Calendar): MutableLiveData<String> {

    var choosenDate = MutableLiveData<String>()

    val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = day

        choosenDate.postValue(CurrentDate(calendar) )
    }


    val datePickerDialog = DatePickerDialog(
        context,
        dateSetListener,
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )

    datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
    datePickerDialog.show()

    return choosenDate
}

fun TimePickerDialog(context: Context):MutableLiveData<String>{

    var choosenTime = MutableLiveData<String>()

    val mcurrentTime = Calendar.getInstance()
    val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
    val minute = mcurrentTime.get(Calendar.MINUTE)


    TimePickerDialog(context, { timePicker, hour, minute ->

        choosenTime.postValue( GetTime(hour, minute))

    },hour,minute,false).show()

    return choosenTime
}


fun GetTime(hr: Int, min: Int): String {
    val cal = Calendar.getInstance()
    cal[Calendar.HOUR_OF_DAY] = hr
    cal[Calendar.MINUTE] = min

    val formatter: Format = SimpleDateFormat("h:mm a", Locale.getDefault())
    return formatter.format(cal.time)
}


