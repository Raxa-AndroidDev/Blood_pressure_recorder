package com.vboard.bp_recorder_app.utils

import android.app.DatePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

fun CurrentDate(calendar: Calendar):String{

        val dateFormat = SimpleDateFormat( "MM/dd/yy", Locale.US)
       val date =  dateFormat.format(calendar.time)
    return date
    }

fun DatePickerDialog(context: Context,calendar: Calendar):String{

   var choosenDate:String? = null
    DatePickerDialog(
        context,
        DatePickerDialog.OnDateSetListener { view, year, month, day ->
            calendar[Calendar.YEAR] = year
            calendar[Calendar.MONTH] = month
            calendar[Calendar.DAY_OF_MONTH] = day
             choosenDate =CurrentDate(calendar)

        },
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    ).show()

    return choosenDate!!
}
