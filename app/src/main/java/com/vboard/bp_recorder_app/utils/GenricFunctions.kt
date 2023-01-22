package com.vboard.bp_recorder_app.utils

import android.app.TimePickerDialog
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.ui.blood_pressure.model_classes.BPTypesModelClass
import com.vboard.bp_recorder_app.ui.weight.WeightTypesModelClass
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*


fun getBPType(systolic: Int, diastolic: Int): String {

    if (systolic < 90 || diastolic < 60) {
        return Constansts.bp_hypotension


    } else if (systolic in 90..119 && diastolic in 60..79) {
        Constansts.bp_Normal

    } else if (systolic in 120..129 && diastolic in 60..79) {
        return Constansts.bp_Elevated


    } else if (systolic in 130..139 || diastolic in 80..89) {
        return Constansts.bp_Hypertension1


    } else if (systolic in 140..180 || diastolic in 90..120) {
        return Constansts.bp_Hypertension2


    } else if (systolic > 180 || diastolic > 120) {
        return Constansts.bp_Crisis

    }
    return Constansts.bp_Normal
}

 fun getWeightType(bmi:Double):String{
    var weightType:String? = null

    if (bmi<16.0){
        weightType = Constansts.verySeverlyUnderWeight
    }
    else if (bmi in 16.0..16.9){
        weightType = Constansts.severlyUnderWeight
    }

    else if (bmi in 17.0..18.4){
        weightType = Constansts.underWeight
    }
    else if (bmi in 18.5..24.9){
        weightType = Constansts.normalWeight
    }
    else if (bmi in 25.0..29.9){
        weightType = Constansts.overWeight
    }

    else if (bmi in 30.0..34.9){
        weightType = Constansts.obeseClass1
    }

    else if (bmi in 35.0..39.9){
        weightType = Constansts.obeseClass2
    }

    else if (bmi >=40.0){
        weightType = Constansts.obeseClass3
    }

    return weightType!!

}

 fun calculateBMI(weight:Int,height:Int):Double{
    return (weight/(height*height)).toDouble()

}

fun getChipsList(): ArrayList<String> {
    val chipsList: ArrayList<String> = arrayListOf()

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


fun getTime(calendar: Calendar): String {

    val timeFormat = SimpleDateFormat(Constansts.timeFormate, Locale.getDefault())
    return timeFormat.format(calendar.time)
}


   /// function to store date in db
fun getDateToStore(calendar: Calendar): String {

    val sdf = SimpleDateFormat(Constansts.dateFormat, Locale.getDefault())
   return sdf.format(calendar.time)


}


fun getNextDayDate(calendar: Calendar): String {
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    val dateFormat = SimpleDateFormat(Constansts.dateFormat, Locale.getDefault())
    return dateFormat.format(calendar.time)


}

fun getPreviousDayDate(calendar: Calendar): String {
    calendar.add(Calendar.DAY_OF_YEAR, -1)
    val dateFormat = SimpleDateFormat(Constansts.dateFormat, Locale.getDefault())
    return dateFormat.format(calendar.time)


}


fun getWeekStartDate(calendar: Calendar): String {
    calendar.add(Calendar.DAY_OF_YEAR, -7)
    val dateFormat = SimpleDateFormat(Constansts.dateFormat, Locale.getDefault())
    return dateFormat.format(calendar.time)


}

fun getWeekEndDate(calendar: Calendar): String {
    val dateFormat = SimpleDateFormat(Constansts.dateFormat, Locale.getDefault())
    return dateFormat.format(calendar.time)

}


fun Long.tostring(): String {
    val dateobj = Date(this)

    val dateFormat = SimpleDateFormat(Constansts.dateFormat, Locale.getDefault())
    return dateFormat.format(dateobj)



}

fun Long.toDate(): Date {
    return Date(this)
}

fun String.toLong(): Long {
    val dateobj = this.toDate()

    return dateobj.time

}

fun String.toDate(): Date {
    val completeDateFormat = SimpleDateFormat(Constansts.dateFormat, Locale.getDefault())
    return completeDateFormat.parse(this) as Date
}


/*
fun Date.toString():String{
    val completeDateFormat = SimpleDateFormat(Constansts.SearchdateFormate, Locale.getDefault())
    return  completeDateFormat.format(this)
}


fun getOnlyDate(calendar: Calendar):String{
    val simpleSDF = SimpleDateFormat(Constansts.SearchdateFormate,Locale.getDefault())
    return simpleSDF.format(calendar)
}



fun getHistoryFormatDate(date:Date):String{
    val historyDateFormat = SimpleDateFormat(Constansts.historyDateFormat, Locale.getDefault())

    return historyDateFormat.format(date)
}

fun getCurrentDate(calendar: Calendar): String {
    val dateFormat = SimpleDateFormat(Constansts.SearchdateFormate, Locale.getDefault())
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
}*/


/*


fun getWeekStartDate(calendar: Calendar):String{
    calendar.add(Calendar.DAY_OF_YEAR, -7)
    val dateFormat = SimpleDateFormat(Constansts.SearchdateFormate, Locale.getDefault())
    return dateFormat.format(calendar.time)
}


fun getWeekEndDate(calendar: Calendar):String{

    val dateFormat = SimpleDateFormat(Constansts.SearchdateFormate, Locale.getDefault())

    return dateFormat.format(calendar.time)
}*/








fun getCurrentDateTime(): Date {
    return Calendar.getInstance().time
}


/*fun DatePickerDialog(context: Context, calendar: Calendar): MutableLiveData<String> {

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
}*/

fun TimePickerDialog(context: Context): MutableLiveData<String> {

    var choosenTime = MutableLiveData<String>()

    val mcurrentTime = Calendar.getInstance()
    val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
    val minute = mcurrentTime.get(Calendar.MINUTE)


    TimePickerDialog(context, { timePicker, hour, minute ->

        choosenTime.postValue(GetTime(hour, minute))

    }, hour, minute, false).show()

    return choosenTime
}


fun GetTime(hr: Int, min: Int): String {
    val cal = Calendar.getInstance()
    cal[Calendar.HOUR_OF_DAY] = hr
    cal[Calendar.MINUTE] = min

    val formatter: Format = SimpleDateFormat("h:mm a", Locale.getDefault())
    return formatter.format(cal.time)
}


fun getBPRangeTypesList(context: Context): ArrayList<BPTypesModelClass> {
    val bpTypesList = arrayListOf<BPTypesModelClass>()
    bpTypesList.add(
        BPTypesModelClass(
            R.color.hypotension_bp_color,
            Constansts.bp_hypotension,
            context.getString(R.string.hypo_bp_range),
            false
        )
    )
    bpTypesList.add(
        BPTypesModelClass(
            R.color.normal_bp_color,
            Constansts.bp_Normal,
            context.getString(R.string.normal_bp_range), false
        )
    )
    bpTypesList.add(
        BPTypesModelClass(
            R.color.elevated_bp_color,
            Constansts.bp_Elevated,
            context.getString(R.string.elevated_bp_range), false
        )
    )
    bpTypesList.add(
        BPTypesModelClass(
            R.color.hyper_stage1_color,
            Constansts.bp_Hypertension1,
            context.getString(R.string.stage1_bp_range), false
        )
    )
    bpTypesList.add(
        BPTypesModelClass(
            R.color.hyper_stage2_color,
            Constansts.bp_Hypertension2,
            context.getString(R.string.stage2_bp_range), false
        )
    )
    bpTypesList.add(
        BPTypesModelClass(
            R.color.hyper_crisis_color,
            Constansts.bp_Crisis,
            context.getString(R.string.critical_bp_range), false
        )
    )


    return bpTypesList
}

fun getWeightRangeTypesList(context: Context): ArrayList<WeightTypesModelClass> {
    val weightTypesList = arrayListOf<WeightTypesModelClass>()
    weightTypesList.add(
        WeightTypesModelClass(
            R.color.very_severly_uw,
            Constansts.verySeverlyUnderWeight,
           context.getString(R.string.veryseverlyuw_range),
            false
        )
    )
    weightTypesList.add(
        WeightTypesModelClass(
            R.color.severly_uw,
            Constansts.severlyUnderWeight,
            context.getString(R.string.severluw_range), false
        )
    )
    weightTypesList.add(
        WeightTypesModelClass(
            R.color.underweight,
            Constansts.underWeight,
            context.getString(R.string.underweight_range), false
        )
    )
    weightTypesList.add(
        WeightTypesModelClass(
            R.color.normalweight,
            Constansts.normalWeight,
            context.getString(R.string.normalweight_range), false
        )
    )
    weightTypesList.add(
        WeightTypesModelClass(
            R.color.overweight,
            Constansts.overWeight,
            context.getString(R.string.overweight_range), false
        )
    )
    weightTypesList.add(
        WeightTypesModelClass(
            R.color.obesestage1,
            Constansts.obeseClass1,
            context.getString(R.string.obesestage1_range), false
        )
    )

    weightTypesList.add(
        WeightTypesModelClass(
            R.color.obesestage2,
            Constansts.obeseClass2,
            context.getString(R.string.obesestage2_range), false
        )
    )

    weightTypesList.add(
        WeightTypesModelClass(
            R.color.obesestage3,
            Constansts.obeseClass3,
            context.getString(R.string.obesestage3_range), false
        )
    )


    return weightTypesList
}



