package com.vboard.bp_recorder_app.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.ui.blood_pressure.model_classes.BPTypesModelClass
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


fun getBPType(systolic:Int, diastolic:Int):String{

    if (systolic < 90 || diastolic < 60) {
         return Constansts.bp_hypotension


    }
    else if (systolic in 90..119 && diastolic in 60..79) {
          Constansts.bp_Normal

    }
    else if (systolic in 120..129 && diastolic in 60..79) {
        return Constansts.bp_Elevated




    }
    else if (systolic in 130..139 || diastolic in 80..89) {
        return Constansts.bp_Hypertension1



    }
    else if (systolic in 140..180 || diastolic in 90..120) {
        return Constansts.bp_Hypertension2



    }
    else if (systolic > 180 || diastolic > 120) {
        return  Constansts.bp_Crisis

    }
    return  Constansts.bp_Normal
}


fun getChipsList():ArrayList<String>{
    val chipsList:ArrayList<String> = arrayListOf()

    chipsList.add("after running")
    chipsList.add("before running")
    chipsList.add("after eating")
    chipsList.add("before eating")
    chipsList.add("right")
    chipsList.add("left")
    chipsList.add("bottom")
    chipsList.add("after jogging")
    chipsList.add("before jogging")
    chipsList.add("after lunch")



    return chipsList
}


fun String.toLong(): Long {
    val dateobj = this.toDate()

    return dateobj.time

}

fun Long.toString():String{
    val dateobj = Date(this)

    return dateobj.toString()
}




fun String.toDate():Date{
    val completeDateFormat = SimpleDateFormat(Constansts.completeDateFormat, Locale.getDefault())
    return completeDateFormat.parse(this)
}

fun Date.toString():String{
    val completeDateFormat = SimpleDateFormat(Constansts.completeDateFormat, Locale.getDefault())
    return  completeDateFormat.format(this)
}

fun getTime(calendar:Calendar):String{

    val timeFormat =SimpleDateFormat(Constansts.timeFormate, Locale.getDefault())
    return timeFormat.format(calendar.time)
}

fun getHistoryFormatDate(date:Date):String{
    val historyDateFormat = SimpleDateFormat(Constansts.historyDateFormat, Locale.getDefault())

    return historyDateFormat.format(date)
}

fun getCurrentDate(calendar: Calendar): String {
    val dateFormat = SimpleDateFormat(Constansts.completeDateFormat, Locale.getDefault())
    return dateFormat.format(calendar.time)

}


fun getWeekStartDateForTextview(calendar: Calendar):String{
    calendar.add(Calendar.DAY_OF_YEAR, -7)
    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    return dateFormat.format(calendar.time)
}


fun getWeekEndDateForTextview(calendar: Calendar):String{
    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    return dateFormat.format(calendar.time)
}











fun getWeekStartDate(calendar: Calendar):String{
    calendar.add(Calendar.DAY_OF_YEAR, -7)
    val dateFormat = SimpleDateFormat(Constansts.completeDateFormat, Locale.getDefault())
    return dateFormat.format(calendar.time)
}


fun getWeekEndDate(calendar: Calendar):String{
    val dateFormat = SimpleDateFormat(Constansts.completeDateFormat, Locale.getDefault())

    return dateFormat.format(calendar.time)
}








fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}



fun getNextDayDate(calendar: Calendar):String{
    calendar.add(Calendar.DAY_OF_MONTH,1)
    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    return dateFormat.format(calendar.time)
}

fun getPreviousDayDate(calendar: Calendar):String{
    calendar.add( Calendar.DAY_OF_YEAR,-1)
    val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    return dateFormat.format(calendar.time)

}







fun DatePickerDialog(context: Context, calendar: Calendar): MutableLiveData<String> {

    var choosenDate = MutableLiveData<String>()

    val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month
        calendar[Calendar.DAY_OF_MONTH] = day

        choosenDate.postValue(getCurrentDate(calendar) )
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


 fun getBPRangeTypesList( context: Context):ArrayList<BPTypesModelClass> {
     val bptypesList = arrayListOf<BPTypesModelClass>()
    bptypesList.add(
        BPTypesModelClass(
            R.color.hypotension_bp_color,
            Constansts.bp_hypotension,
           context.getString(R.string.hypo_bp_range),
            false
        )
    )
    bptypesList.add(
        BPTypesModelClass(
            R.color.normal_bp_color,
            Constansts.bp_Normal,
            context.getString(R.string.normal_bp_range), false
        )
    )
    bptypesList.add(
        BPTypesModelClass(
            R.color.elevated_bp_color,
            Constansts.bp_Elevated,
            context.getString(R.string.elevated_bp_range), false
        )
    )
    bptypesList.add(
        BPTypesModelClass(
            R.color.hyper_stage1_color,
            Constansts.bp_Hypertension1,
            context.getString(R.string.stage1_bp_range), false
        )
    )
    bptypesList.add(
        BPTypesModelClass(
            R.color.hyper_stage2_color,
            Constansts.bp_Hypertension2,
            context.getString(R.string.stage2_bp_range), false
        )
    )
    bptypesList.add(
        BPTypesModelClass(
            R.color.hyper_crisis_color,
            Constansts.bp_Crisis,
            context.getString(R.string.critical_bp_range), false
        )
    )


     return bptypesList
}


