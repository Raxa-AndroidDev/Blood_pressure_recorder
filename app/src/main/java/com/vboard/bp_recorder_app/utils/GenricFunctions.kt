package com.vboard.bp_recorder_app.utils

import android.app.TimePickerDialog
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.ui.fragments.blood_pressure.model_classes.BPTypesModelClass
import com.vboard.bp_recorder_app.ui.fragments.info_module.details.InfoDetailModelClass
import com.vboard.bp_recorder_app.ui.fragments.weight.WeightTypesModelClass
import com.vboard.bp_recorder_app.ui.language_selection.LangSelectionModelClass
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow


fun View.applyOnClick(onClickFunction: () -> Unit) {
    setOnClickListener {
        onClickFunction()
        isEnabled = false
        Handler(Looper.myLooper()!!).postDelayed({ isEnabled = true }, 2000)
    }

}
fun setLocale(context: Context,localeCode:String){
    val myLocale = Locale(localeCode)
    val res: Resources = context.resources
    val dm: DisplayMetrics = res.displayMetrics
    val conf: Configuration = res.configuration
    conf.setLocale(myLocale)
    Locale.setDefault(myLocale)
    conf.setLayoutDirection(myLocale)
    res.updateConfiguration(conf, dm)
}


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

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

fun getWeightType(bmi: Double): String {
    var weightType: String? = null

    if (bmi < 16.000000) {
        weightType = Constansts.verySeverlyUnderWeight
    } else if (bmi in 16.0..16.99999999999999) {
        weightType = Constansts.severlyUnderWeight
    } else if (bmi in 17.0..18.49999999999999) {
        weightType = Constansts.underWeight
    } else if (bmi in 18.5..24.99999999999999) {
        weightType = Constansts.normalWeight
    } else if (bmi in 25.0..29.99999999999999) {
        weightType = Constansts.overWeight
    } else if (bmi in 30.0..34.99999999999999) {
        weightType = Constansts.obeseClass1
    } else if (bmi in 35.0..39.99999999999999) {
        weightType = Constansts.obeseClass2
    } else if (bmi >= 40.0000000000000000) {
        weightType = Constansts.obeseClass3
    }


    Log.e("TAG", "getWeightType: $bmi")


    return weightType!!

}

fun calculateBMI(weight: Int, height: Int): Double {
    return ((weight.toDouble() / height.toDouble().pow(2)) * 10000)

}


fun getChipsList(mode: String, tag: String): ArrayList<String> {
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

    if (mode == "add") {
        chipsList.add(
            tag
        )
    }




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
            context.getString(R.string.veryseverlyunderweight_range),
            false
        )
    )
    weightTypesList.add(
        WeightTypesModelClass(
            R.color.severly_uw,
            Constansts.severlyUnderWeight,
            context.getString(R.string.severlyunderweight_range),
            false
        )
    )
    weightTypesList.add(
        WeightTypesModelClass(
            R.color.underweight,
            Constansts.underWeight,
            context.getString(R.string.underweigth_range), false
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
            context.getString(R.string.obese1_range), false
        )
    )

    weightTypesList.add(
        WeightTypesModelClass(
            R.color.obesestage2,
            Constansts.obeseClass2,
            context.getString(R.string.obese2_range), false
        )
    )

    weightTypesList.add(
        WeightTypesModelClass(
            R.color.obesestage3,
            Constansts.obeseClass3,
            context.getString(R.string.obese3_range), false
        )
    )


    return weightTypesList
}


fun getInfoBgColorsList(): ArrayList<Int> {
    val info_colors_bg_list: ArrayList<Int> = arrayListOf()

    info_colors_bg_list.add(R.color.tab_selected_color)
    info_colors_bg_list.add(R.color.info_yellow_color)
    info_colors_bg_list.add(R.color.info_red_color)
    info_colors_bg_list.add(R.color.info_light_green_color)
    info_colors_bg_list.add(R.color.info_blue_color)

    info_colors_bg_list.add(R.color.info_red_color)
    info_colors_bg_list.add(R.color.tab_selected_color)
    info_colors_bg_list.add(R.color.info_red_color)
    info_colors_bg_list.add(R.color.info_light_green_color)
    info_colors_bg_list.add(R.color.info_blue_color)

    info_colors_bg_list.add(R.color.info_yellow_color)
    info_colors_bg_list.add(R.color.tab_selected_color)
    info_colors_bg_list.add(R.color.info_red_color)
    info_colors_bg_list.add(R.color.info_yellow_color)
    info_colors_bg_list.add(R.color.info_blue_color)

    info_colors_bg_list.add(R.color.info_light_green_color)
    info_colors_bg_list.add(R.color.info_yellow_color)
    info_colors_bg_list.add(R.color.tab_selected_color)
    info_colors_bg_list.add(R.color.info_red_color)
    info_colors_bg_list.add(R.color.info_light_green_color)

    info_colors_bg_list.add(R.color.info_blue_color)
    info_colors_bg_list.add(R.color.info_yellow_color)
    info_colors_bg_list.add(R.color.tab_selected_color)
    info_colors_bg_list.add(R.color.info_red_color)
    info_colors_bg_list.add(R.color.info_light_green_color)

    info_colors_bg_list.add(R.color.info_blue_color)
    info_colors_bg_list.add(R.color.info_yellow_color)






    return info_colors_bg_list
}


fun getLangsList(context: Context):ArrayList<LangSelectionModelClass>{

    val langsList:ArrayList<LangSelectionModelClass> = arrayListOf()

    langsList.add(LangSelectionModelClass(R.drawable.lang_icon_settings,"English","en"))
    langsList.add(LangSelectionModelClass(R.drawable.lang_icon_settings,"Urdu","ur"))
    langsList.add(LangSelectionModelClass(R.drawable.lang_icon_settings,"Arabic","ar"))
    langsList.add(LangSelectionModelClass(R.drawable.lang_icon_settings,"Hindi","hi"))
    langsList.add(LangSelectionModelClass(R.drawable.lang_icon_settings,"Thai","th"))




    return  langsList

}


fun getInfoTitleIconsList(): ArrayList<Int> {
    var info_icon_bg_list: ArrayList<Int> = arrayListOf()

    info_icon_bg_list.add(R.drawable.icon_info_1)
    info_icon_bg_list.add(R.drawable.icon_info_2)
    info_icon_bg_list.add(R.drawable.icon_3)
    info_icon_bg_list.add(R.drawable.icon_info_4)
    info_icon_bg_list.add(R.drawable.icon_info_5)

    info_icon_bg_list.add(R.drawable.icon_info_6)
    info_icon_bg_list.add(R.drawable.icon_7)
    info_icon_bg_list.add(R.drawable.icon_info_8)
    info_icon_bg_list.add(R.drawable.icon_9)
    info_icon_bg_list.add(R.drawable.icon_10)

    info_icon_bg_list.add(R.drawable.icon_11)
    info_icon_bg_list.add(R.drawable.icon_12)
    info_icon_bg_list.add(R.drawable.icon_13)
    info_icon_bg_list.add(R.drawable.icon_14)
    info_icon_bg_list.add(R.drawable.icon_15)

    info_icon_bg_list.add(R.drawable.icon_16)
    info_icon_bg_list.add(R.drawable.icon_17)
    info_icon_bg_list.add(R.drawable.icon_18)
    info_icon_bg_list.add(R.drawable.icon_19)
    info_icon_bg_list.add(R.drawable.icon_20)

    info_icon_bg_list.add(R.drawable.icon_gestanioal_hypertension)
    info_icon_bg_list.add(R.drawable.icon_22)
    info_icon_bg_list.add(R.drawable.icon_23)
    info_icon_bg_list.add(R.drawable.icon_24)
    info_icon_bg_list.add(R.drawable.icon_25)

    info_icon_bg_list.add(R.drawable.icon_26)
    info_icon_bg_list.add(R.drawable.icon_27)



    return info_icon_bg_list
}


fun getInfoTitlesList(): ArrayList<String> {
    val info_titles_list: ArrayList<String> = arrayListOf()

    info_titles_list.add(Constansts.infoTitle1)
    info_titles_list.add(Constansts.infoTitle2)
    info_titles_list.add(Constansts.infoTitle3)
    info_titles_list.add(Constansts.infoTitle4)
    info_titles_list.add(Constansts.infoTitle5)
    info_titles_list.add(Constansts.infoTitle6)
    info_titles_list.add(Constansts.infoTitle7)
    info_titles_list.add(Constansts.infoTitle8)
    info_titles_list.add(Constansts.infoTitle9)
    info_titles_list.add(Constansts.infoTitle10)
    info_titles_list.add(Constansts.infoTitle11)
    info_titles_list.add(Constansts.infoTitle12)
    info_titles_list.add(Constansts.infoTitle13)
    info_titles_list.add(Constansts.infoTitle14)
    info_titles_list.add(Constansts.infoTitle15)
    info_titles_list.add(Constansts.infoTitle16)
    info_titles_list.add(Constansts.infoTitle17)
    info_titles_list.add(Constansts.infoTitle18)
    info_titles_list.add(Constansts.infoTitle19)
    info_titles_list.add(Constansts.infoTitle20)
    info_titles_list.add(Constansts.infoTitle21)
    info_titles_list.add(Constansts.infoTitle22)
    info_titles_list.add(Constansts.infoTitle23)
    info_titles_list.add(Constansts.infoTitle24)
    info_titles_list.add(Constansts.infoTitle25)
    info_titles_list.add(Constansts.infoTitle26)
    info_titles_list.add(Constansts.infoTitle27)


    return info_titles_list

}

fun getInfoDetailsData(title: String): ArrayList<InfoDetailModelClass> {
    val infoDetailList = arrayListOf<InfoDetailModelClass>()



    if (title == Constansts.infoTitle1) {

        infoDetailList.add(InfoDetailModelClass("Types of blood pressure and concerns", ""))
        infoDetailList.add(
            InfoDetailModelClass(
                "Blood pressure typically rises and falls throughout the course of the day, but if it remains high for an extended period of time, it can harm your heart and result in health issues. Hypertension, often known as high blood pressure, is elevated blood pressure.\n" +
                        "To define your blood pressure numbers, you can see the blood pressure category below.",
                ""
            )
        )
        infoDetailList.add(InfoDetailModelClass("Hypotension", "SYS < 90 or DIA < 60"))
        infoDetailList.add(InfoDetailModelClass("Normal", "SYS 90-119 and DIA 60-79"))
        infoDetailList.add(InfoDetailModelClass("Elevated", "SYS 120-129 and DIA 60-79"))
        infoDetailList.add(InfoDetailModelClass("Hypertension•Stage 1", "SYS 130-139 or DIA 80-89"))
        infoDetailList.add(
            InfoDetailModelClass(
                "Hypertension•Stage 2",
                "SYS 140-180 or DIA 90-120"
            )
        )
        infoDetailList.add(InfoDetailModelClass("Hypertensive", "SYS > 180 or DIA > 120"))
        infoDetailList.add(
            InfoDetailModelClass(
                "1.Hypotension Stage",
                "You are likely hypotensive if your readings are less than 90/60 mmHg. Since low blood pressure typically has no negative effects and no symptoms, you do not require any treatment. \n" +
                        "However, you should get help if your blood pressure frequently decreases by over 20 mmHg without warning, drops quickly as a result of particular medications, or if you experience symptoms like exhaustion, fainting, or dizziness."
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "2. Normal Ranges of blood pressure",
                "Your blood pressure is normal if your readings are greater than 90/60 mmHg and less than 120/80 mmHg. A healthy lifestyle must be maintained or adopted if you want to stop hypertension from developing. \n" +
                        "Additionally, since you have a higher chance of acquiring hypertension if you have any family members who suffer from the condition, it is advised that you pay even closer attention to your lifestyle."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "3.Elevated Stage",
                "Blood pressure is regarded as raised when it is greater than usual but falls short of 130/80 mmHg. \n" +
                        "Your likelihood of developing high blood pressure increases if your lifestyle and eating habits don't change."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "4.Hypertension Stage 1",
                "You are in stage 1 hypertension if your diastolic or systolic pressure consistently ranges between 80 and 89 millimeters of mercury (mmHg). \n" +
                        "In general, all it takes to reduce blood pressure is an improvement in lifestyle. However, you should begin taking the appropriate medications if you have a high risk of cardiovascular disease (heart disease, stroke, etc.)."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "5.Hypertension Stage 2",
                "When your systolic or diastolic blood pressure consistently ranges from 140 to 180 mmHg or between 90 and 120 mmHg, you have hypertension stage 2. \n" +
                        "You ought to combine one or more drugs with a change in your way of life at this point. \n" +
                        "If your blood pressure is under control after the first month of therapy and lifestyle changes, you can return to the hospital in three to six months for another reading. You should speak with a doctor about switching to alternative medicines if your blood pressure is greater or doesn't change."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "6.Hypertension Crisis",
                "If you are checking your blood pressure at home and see your systolic reading over 180 mmHg or diastolic number above 120 mmHg, you need to calm down at first then wait a few minutes to retest it. If you are still in the range of the hypertension crisis, don't hesitate to call the emergency service or go to the hospital right now."
            )
        )

    }

    if (title == Constansts.infoTitle2) {


        infoDetailList.add(
            InfoDetailModelClass(
                "How does blood pressure work?",
                "It must be confusing for you to understand what blood pressure means or how the two columns of numbers on the device screen work, whether you monitor your blood pressure at home or in a hospital. Fear not; all questions will be resolved after reading this essay."
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "What is blood pressure?",
                "The heart functions like a water pump, continuously pushing blood to the body's blood arteries. Blood flows and pushes against blood vessel walls. Blood pressure is the force that pushes."
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "What are the two numbers of the BP device?",
                "Systolic blood pressure and diastolic blood pressure are represented by two readings on your blood pressure monitor. Systolic blood pressure, or the top or leading number, refers to the force exerted by your heart as it pushes blood through your arteries. \n" +
                        "The diastolic number, which is the bottom or second number, describes the pressure in your arteries between heartbeats."
            )
        )
    }

    if (title == Constansts.infoTitle3) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Identify your blood pressure type.\n" +
                        "Hypertension",
                "The human cardiovascular system resembles a water circulatory system in that blood constantly circulates like water, carrying nutrients and oxygen to the organs and blood arteries. \n" +
                        "Blood flow gets challenging when blood vessels become less elastic. When this happens, your body tries to force blood to flow with more force, which leads to hypertension."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Hypotension",
                "You should monitor your blood pressure if it drops below 90/60 mmHg on a regular basis or drops sharply below 20 mmHg. In most cases, hypotension without symptoms doesn't require medical attention. \n" +
                        "Nevertheless, if symptoms or a rapid fall occur, a precise diagnosis and specific therapy are required."
            )
        )

    }


    if (title == Constansts.infoTitle4) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Break Myths About Blood Pressure",
                "There are a lot of myths about blood pressure. These myths can actually make your blood pressure control ineffective or possibly damage your situation. Here are seven typical misunderstandings about blood pressure. Stop trusting them after reading the following."
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "I don't need to worry if my blood pressure is low.",
                "In general, if you have hypotension, a few little lifestyle adjustments can help. However, you require medical attention if your blood pressure drops suddenly by more than 20 mmHg, or if you experience fainting, lethargy, impaired vision, etc. Hypotension can be fatal if it is not immediately treated."
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "If no symptoms appear, I don't have hypertension",
                "Do you know that high blood pressure is referred to as a \"silent killer\"? It frequently goes undetected and may not even show any symptoms, or it may show very moderate signs that are not treated seriously even though it is seriously harming your health. \n" +
                        "When symptoms start to show up, it means that the heart, brain, kidney, blood vessel, or other organs have suffered substantial damage and are no longer able to operate normally. The ideal opportunity for treatment has already passed by at that point. \n" +
                        "In the meantime, problems arise and life is in danger."
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "Treatment for hypertension is available",
                "The only form of hypertension that has a known cure is secondary hypertension. Secondary hypertension will go away after the secondary ailment is treated. \n" +
                        "Although primary hypertension cannot yet be cured, it is still feasible to prevent or lessen the harm it does to the body by bringing blood pressure under control with drugs or by making positive lifestyle changes. It is false to say that treating high blood pressure with easy steps can prevent relapse."
            )
        )


        infoDetailList.add(
            InfoDetailModelClass(
                "Red wine consumption helps to control blood pressure",
                "According to a myth, the resveratrol in red wine might keep the heart healthy. \n" +
                        "However, that claim cannot be supported by any investigation. Requiring large amounts of red wine to obtain the desired impact, which could seriously hurt your body, is required if resveratrol can truly safeguard your cardiac health. Additionally, alcohol has been shown to have no beneficial effects on the cardiovascular system."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Once my blood pressure is managed, I can stop using my medications.",
                "This is completely false and may deceive. The majority of patients with primary hypertension require lifelong drug therapy because there is no known treatment for the condition. Your blood pressure is under control because of pharmacological management, not because high blood pressure has been healed. Blood pressure is likely to rise again when the medicine is stopped. \n" +
                        "Furthermore, stopping the usage of some medicines can result in dangerous withdrawal symptoms. \n" +
                        "Thus, even if your blood pressure has been steady for a while, you should get medical advice before changing or stopping your prescription. Before changing the medicine, speak with the doctor. In accordance with your doctor's advice, you should cut back on your medication, keep an eye on your blood pressure, and lead an active lifestyle."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Youth won't get hypertension",
                "While middle-aged and elderly persons are more likely to have hypertension patients, young people can also be affected. Age, physical size, stage of sexual development, lifestyle, etc. all affect blood pressure levels."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Hypertension is only a problem for men",
                "Age truly affects how much of a risk differential there is between men and women. Men have a higher risk of developing hypertension than women do before the age of 45. They have virtually the same risk between the ages of 45 and 64. It is important to note that women are more likely than males to develop high blood pressure after the age of 64."
            )
        )


    }

    if (title == Constansts.infoTitle5) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Types of hypertensions you should be aware of",
                "Do you know there are two primary forms of high blood pressure? High blood pressure can be either primary or secondary. To learn more about their many causes, continue reading."
            )
        )


        infoDetailList.add(
            InfoDetailModelClass(
                "Primary Hypertension",
                "Primary hypertension is the one that occurs most frequently. It denotes persistently elevated blood pressure that is unrelated to another illness. The exact aetiology of hypertension is unknown after years of investigation. Your blood pressure may rise as a result of poor diets, inactivity, using tobacco or alcohol, abusing weight-gaining substances, and other factors. \n" +
                        "Age will cause a rise in patients' blood pressure. It has taken some time to discover the cure. The majority of people therefore require lifelong medication to manage their blood pressure."
            )
        )


        infoDetailList.add(
            InfoDetailModelClass(
                "Secondary Hypertension",
                "Only 5 to 10% of hypertension cases are secondary, on average. Additionally, it is known that secondary hypertension is more common in younger persons. \n" +
                        "Secondary hypertension is brought on by a number of medical diseases or drugs, such as obstructive sleep apnea, kidney illness, adrenal gland tumours, thyroid issues, blood vessel anomalies, etc. Secondary hypertension will improve after people cease taking their medications or treat the underlying cause."
            )
        )


    }


    if (title == Constansts.infoTitle6) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Headache",
                "High blood pressure and headache typically signal an emergency. Your brain's blood arteries may be harmed by too high blood pressure, which could raise intracranial pressure. On both sides of your head, pulsing headaches will be present, and any activity will just make them worse."
            )
        )


        infoDetailList.add(
            InfoDetailModelClass(
                "Dizziness",
                "Although taking antihypertensive medicine may cause dizziness as a side effect, you shouldn't take it for granted. Stroke is mostly brought on by severe high blood pressure. A stroke may be indicated by sudden dizziness, loss of balance, or shaky walking."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Nausea",
                "A hypertensive crisis could occur if you have hypertension and feel sudden nausea and loss of appetite. Usually, dizziness and nausea brought on by severely high blood pressure occur simultaneously."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "shortness of breath",
                "Shortness of breath will result from high blood pressure's impact on heart and lung health. That might be easier to see when patients exercise."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Chest pain",
                "A major risk factor for heart attacks and heart failure is hypertension. Atherosclerosis caused by high blood pressure makes it difficult to transport oxygen and blood. It implies that less blood will be able to reach the heart and eventually cause angina, or chest pain."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Blurred vision",
                "On your eveballs, there are numerous tiny vessels. These vessels may become damaged and begin to bleed when your blood pressure reaches a risky level, which can result in blurry or even lost vision."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Nosebleed",
                "Unless your blood pressure is exceedingly high, hypertension typically doesn't produce nosebleeds. Your nose has been bleeding because it has damaged blood vessels. You must now either summon an ambulance or leave right away for the hospital."
            )
        )

    }

    if (title == Constansts.infoTitle7) {

        infoDetailList.add(
            InfoDetailModelClass(
                "Understand the issues that high blood pressure causes.",
                "Your health is seriously threatened by high blood pressure. Your kidneys, eyes, heart, and brain can all slowly deteriorate without any warning. \n" +
                        "Read on for a comprehensive explanation of the impacts of hypertension on your body."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Hypertension causes heart disease",
                "Heart failure, coronary artery disease, and other conditions can all be brought on by high blood pressure. According to some research, high blood pressure is the root cause of roughly 25% of occurrences of heart failure. \n" +
                        "Due to hypertension, arteries become less flexible and fatty substances block blood vessels, making it impossible for the heart to receive smooth blood flow. The left ventricle must work harder at that time to pump blood to the body's numerous organs. \n" +
                        "The left ventricle will eventually enlarge as a result of the increased strain, dramatically raising the chance of developing cardiac conditions. Heart failure develops when the heart weakens and is unable to pump blood."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Hypertension can harm your brain",
                "Transient ischemic attack, stroke, dementia, moderate cognitive impairment, and other brain conditions can all be brought on by high blood pressure. \n" +
                        "Blood flow to the brain will be impeded by blocked arteries and elevated blood pressure. When the brain's blood supply is inadequate, it cannot function, which might cause a transient ischemic attack (TIA). \n" +
                        "Brain blood arteries become less flexible as a result of high blood pressure. This occurrence prevents nutrition and oxygen from reaching the brain, which results in brain cells dying and stroke. Your risk of having a stroke increase with increasing blood pressure. According to studies, males who have high blood pressure have a 220 percent increased risk of having a stroke.\n" +
                        "Except for TIA and stroke, high blood pressure can also result in vascular sclerosis of your cognitive area. That means patients can be affected with cognitive impairment and gradually may develop into dementia."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Hypertension will cause kidney problems",
                "Blood pressure and the kidney are strongly related. Through the kidneys, the blood transports metabolic waste. In a healthy kidney, around half a cup of blood is filtered per minute, and the extra water and metabolic waste are excreted as urine. \n" +
                        "In order to resist the excessive pressure, blood vessel walls will thicken when the blood pressure is high. The blood channel narrows as a result of this alteration, which makes it difficult for waste to be filtered. Furthermore, the kidneys' essential blood supply cannot be ensured. Over time, it causes chronic kidney disease, ischemic atrophy of the kidneys, and renal failure to develop."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Hypertension affects eyesight",
                "Extreme high blood pressure will cause swelling of the retina. Patients will experience blurred vision or even loss of eyesight.\n" +
                        "The retina is at the back of the eye and functions image focusing. When the blood pressure is too high, the blood vessel walls of the retina will thicken and make the blood vessels narrow. Over time, high blood pressure can damage retinal blood vessels, limit retinal function, and put pressure on the optic nerve, causing vision problems."
            )
        )


    }

    if (title == Constansts.infoTitle8) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Understanding Hypotension Symptoms", "Hypotension symptoms include:\n" +
                        "• Blurred vision\n" +
                        "• Dizziness or fainting\n" +
                        "• Inability to concentrate\n" +
                        "• Nausea\n" +
                        "• Rapid breathing\n" +
                        "•Fatigue"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Understanding Hypotension Type",
                "Whether you experience these symptoms frequently and are unsure of the cause, have your blood pressure checked. Then, determine if you fall under one of the categories below. "
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "1. orthostatic tachycardia ",
                "Orthostatic hypotension is the condition that occurs when a person stands up from a seated or lying down position. \n" +
                        "It is widespread, affecting around one-fifth of seniors over 65. \n" +
                        "Orthostatic hypotension can also be brought on by beta-blockers, ACE inhibitors, antidepressants, and medications used to treat Parkinson's disease."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "2.Postprandial hypotension",
                "Since postprandial hypotension nearly never affects young individuals and typically manifests after older folks have finished eating, it is frequently thought of as a geriatric condition. \n" +
                        "Following a meal, the heart beats more quickly as the blood arteries in other parts of the body contract to keep blood pressure stable. However, the bodies of certain elderly persons are unable to function normally. Blood pressure drops when blood goes to the intestines because the heart rate cannot be raised enough and the vasoconstriction is insufficient to keep blood pressure stable."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "3.Neurally mediated hypotension",
                "The heart and the brain are unable to communicate, which results in this kind of low blood pressure. Neurally mediated hypotension can occur in people who are upset or who have been standing for a long time. The heart must pump blood more quickly during that time to get blood to the brain. However, the brain sends the signal that the heart rate should be reduced, which causes the blood vessels in the arms and lower body to swell, which reduces the amount of blood that reaches the brain."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Understanding Other Hypotension Causes\n" +
                        "other causes of consistent hypotension",
                "• Pregnancy, hormonal changes affect the blood vessels and circulatory system so that blood pressure may be lower during the first 24 weeks of pregnancy\n" +
                        "• Arrhythmia (abnormal heartbeat)\n" +
                        "• Heart disease\n" +
                        ". Certain over-the-counter drugs used in combination with hypertension drugs\n" +
                        "• Endocrine disorders such as diabetes, adrenal insufficiency and thyroid disease"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Reasons for sudden fall of blood pressure", "• Loss of blood from bleeding\n" +
                        "• Low body temperature\n" +
                        "• High body temperature\n" +
                        "• Heart muscle disease causing heart failure\n" +
                        "• Sepsis, a severe blood infection\n" +
                        "• Severe dehydration from vomiting, diarrhea, or fever\n" +
                        "• A reaction to medication or alcohol\n" +
                        "• A severe allergic reaction"
            )
        )


    }

    if (title == Constansts.infoTitle9) {


        infoDetailList.add(
            InfoDetailModelClass(
                "First-Line Hypertension Treatment",
                "If you are told that you have high blood pressure and that you require medical care, you might ask what kind of care is best for your body. Here, we outline four drugs that your doctor may recommend to you initially. \n" +
                        "To better comprehend the therapy and reduce unpleasant hypertension, you can check the medications you are taking."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "1. Angiotensin-converting enzyme (ACE)",
                "In order to control blood pressure, this type of drug is crucial in relaxing blood vessels and obstructing the functions of specific hormones. It's also important to note that the blood flow to the kidneys would become less effective."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "WHO should not take ACE?", "• Expectant mothers \n" +
                        "In the latter six months of pregnancy, ACE can be hazardous to unborn children. Therefore, you should choose a different method of blood pressure regulation under the supervision of your doctor in order to better safeguard yourself and your unborn child. \n" +
                        "• Those who have kidney disorders \n" +
                        "It is not safe for this sort of person to utilize ACE since it could lower the blood supply to the kidneys. \n" +
                        "• Those who experience severe allergic reactions \n" +
                        "Stop taking the ACE and seek medical attention right away if it causes your body to experience any serious allergic reactions."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "2. Diuretics",
                "Diuretics, which make it easier to urinate and eliminate extra sodium (salt) and water to lower blood pressure, are another prominent type of hypertension drug. When treating high blood pressure, it is frequently used with other drugs. \n" +
                        "WHICH PEOPLE SHOULD NOT TAKE DIURETICS? \n" +
                        "• People beyond age \n" +
                        "Diuretics should be avoided since they typically react to dehydration more severely. \n" +
                        "• Expectant and nursing mothers \n" +
                        "Diuretics may be transferred from the mother's body to the infant, which could lead to the baby becoming dehydrated. \n" +
                        "• Kids \n" +
                        "Diuretics may result in a calcium shortage, which could affect bone growth."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "3. Angiotensin II receptor blockers (ARBs)",
                "We must discuss angiotensin to demonstrate how ARB lowers blood pressure. When it binds to a receptor, blood arteries may become constricted. The ARBs stop it from attaching to blood vessels, keeping the vessels open and lowering blood pressure. \n" +
                        "WHO shouldn't use an ARB? \n" +
                        "• Individuals with certain renal issues \n" +
                        "People who have renal artery stenosis, or restricted arteries to the kidneys, or who have really low kidney function may respond severely to ARBS. \n" +
                        "• ARBs can block the action of angiotensin II in people with low blood sodium levels, which will decrease the amount of sodium in the renal tubules and exacerbate your salt shortage.\n" +
                        "Pregnant and breastfeeding women & people with severe allergy\n" +
                        "Those people also need to ask for doctor's advice concerning taking ARBs."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "4. Calcium channel blockers",
                "You might be curious how calcium and blood pressure management are related. \n" +
                        "However, calcium can enter heart and artery muscle cells, causing muscles to contract more forcefully and strongly. \n" +
                        "Calcium channel blockers reduce blood pressure by preventing calcium from entering your blood arteries, which relaxes muscle contractions. \n" +
                        "WHO SHOULD USE CALCUTHYNE CHAIN BLOCKERS SAFELY? \n" +
                        "• Heart disease sufferers \n" +
                        "Calcium channel blockers may cause harm to your health and lower your heart rate if you have structural heart disease or congestive heart failure. \n" +
                        "• Women who are pregnant or nursing and those who have severe allergies"
            )
        )


    }


    if (title == Constansts.infoTitle10) {
        infoDetailList.add(
            InfoDetailModelClass(
                "How Can I Manage My Blood Pressure?",
                "It takes a lifetime to combat the \"silent killer.\" Make some modifications and don't only rely on the meds if you want to keep your heart healthy. By adopting the heart-healthy lifestyle recommended in this article, you might get your statistics back to normal. Let's do something right away to lower your blood pressure and enhance daily life."
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "1.Keep a healthy weight",
                "Blood pressure and weight have a tight relationship. The blood pressure increases as weight increases. Excessive weight loss can widen blood arteries, improving the heart's ability to pump blood. According to research, decreasing weight can lower systolic and diastolic blood pressure by 4.5 to 8.5 and 3.2 to 6.5 mmHg, respectively. \n" +
                        "The fact that only 5% of your body weight needs to be lost will not force you to do a lot of effort in order to significantly lower your blood pressure. \n" +
                        "You must, however, place a high priority on keeping an eye on your waistline. \n" +
                        "Your blood pressure may be impacted by excess body fat around your stomach. Men are advised to maintain a low waist measurement to less than 40 inches and women be no more than 35 inches."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "2. Eat a dash diet",
                "The Dietary Approaches to Stop Hypertension (DASH) diet was developed to lower cholesterol and treat and prevent high blood pressure. In addition, it helps with things like cancer risk reduction and weight loss. The standard DASH diet programme recommends consuming no more sodium than 1 teaspoon (2,300 mg) per day. The DASH diet can lower your systolic blood pressure by as much as 11mmHg by consuming less sodium and more potassium, magnesium, and calcium. \n" +
                        "Learn to read food labels before purchasing anything to determine the sodium content. Stop purchasing it if it has more than 20% salt. \n" +
                        "A DASH diet often consists of: • Consuming fruits, vegetables, whole grains, and lean meats."
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "3. Limit alcohol",
                "Unexpectedly, alcohol plays a significant role in 16% of all instances of hypertension worldwide. Drinking more than is considered moderate can cause a 7 mm Hg increase in blood pressure. \n" +
                        "Men should not consume more than two drinks per day, while women should limit their alcohol consumption to one drink per day in order to properly manage hypertension. One drink is equal to 12 ounces of beer, 5 ounces of wine, or 1.5 ounces of 80-proof liquor in order to quantify your alcohol consumption more naturally. \n" +
                        "Attention! Alcohol consumption should be avoided if you are on hypertensive drugs because it will reduce their effectiveness."
            )
        )


        infoDetailList.add(
            InfoDetailModelClass(
                "4. Exercise regularly",
                "Exercise causes your heartbeat and breathing to speed up, which strengthens the heart's ability to pump blood and lowers blood pressure. \n" +
                        "If you have hypertension, attempt moderately intense aerobic exercises, weight training, and flexibility drills. You can drastically lower your blood pressure with 150 minutes a week, which is equivalent to certain prescription drugs. To make your routine interesting and healthful, you can opt to jog, walk, swim, lift weights, practice yoga, or do any other activity you enjoy. \n" +
                        "Consistency is key because stopping an exercise programme can cause your blood pressure to spike once more."
            )
        )



        infoDetailList.add(
            InfoDetailModelClass(
                "5. Manage stress",
                "When you are under prolonged stress, your blood vessels narrow and your heart rate rises. While under stress, you are more prone to practice behaviors like consuming processed foods or alcohol, which can raise your blood pressure. \n" +
                        "Here are some suggestions for reducing stress: \n" +
                        "• Consider the problems you can solve. Make a strategy and do the activity as soon as you can if it irritates you. \n" +
                        "•Take it easy. It's time to embrace yourself and indulge in your favorite activities, whether they be yoga, music, or reading. \n" +
                        "• Be mindful to avoid bad habits like smoking and alcohol consumption."
            )
        )


        infoDetailList.add(
            InfoDetailModelClass(
                "6. Quit smoking",
                "Every cigarette puff causes your blood pressure to rise right away and your heart to beat more vigorously. If you continue to smoke, the nicotine will weaken the walls of your blood vessels and hasten the fatty substances that block arteries, leading to hypertension. \n" +
                        "Nicotine can have a detrimental impact on how well drugs work. Furthermore, inhaling secondhand smoke will have the same result. \n" +
                        "It's time to stop smoking for the sake of your loved ones and your heart."
            )
        )


    }

    if (title == Constansts.infoTitle11) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Exercises Can Help Lower Blood Pressure",
                "Are you looking for drug-free methods for controlling, preventing, and treating hypertension? If so, you've arrived to the correct place. This post will outline some exercises that can lower blood pressure. You can see what a significant impact you will make once you incorporate them into your everyday routine."
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "1. Aerobic exercise",
                "A good technique to lower blood pressure is through aerobic exercise. It has been demonstrated that consistent aerobic exercise can lead to a discernible rise in heart rate and breathing, which reduces blood vessel stiffness and improves blood flow. Regular aerobic activity often reduces resting systolic blood pressure by 5 to 8 mmHg. \n" +
                        "Please bear in mind that discontinuing an exercise programme could result in loss of gains. \n" +
                        "It is necessary to exercise at a moderate level five to seven days a week. Patients with hypertension must exercise aerobically for at least 30 minutes or up to 60 minutes each day. If a 30-minute workout seems too demanding, break it up into 10-minute sessions.\n"
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "EXAMPLES of aerobic activities using large muscle groups", "•Climbing stairs\n" +
                        "• Walking\n" +
                        "• Jogging\n" +
                        "• Bicycling\n" +
                        "• Swimming\n" +
                        "• Dancing\n"
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "2. Resistance training",
                "In addition to aerobic exercise, resistance training can be used to help widen blood vessels, which will immediately and safely lower blood pressure. In order to do it, you typically need weights or apparatus like resistance-training machines. It is advised to perform it two or three times per week with a moderate level of exertion. Additionally, avoid repeatedly using the same muscle group or performing resistance workouts. Changing the affected body part lowers blood pressure. "
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "PRECAUTIONS",
                "Consult a professional for advice; perform fewer repetitions; rest for 90 or more seconds between sets; perform resistance training at a controlled pace but not too slowly; don't hold your breath because doing so can raise your blood pressure."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Example of resistance training with equipment", "• Chest press\n" +
                        "• Shoulder press\n" +
                        "• Triceps extension\n" +
                        "• Biceps curl\n" +
                        "•Pull-down\n" +
                        "• Lower-back extension\n" +
                        "•Quadriceps extension\n" +
                        "• Calf raises\n"
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "3. Flexibility workout",
                "For hypertensives, simple flexibility exercises or stretches are beneficial. You actually stretch blood vessels and arteries and loosen up blood flow when you stretch your muscles and joints. It's time to incorporate stretching into your exercise programme. \n" +
                        "Studies show that exercising 2 to 5 days a week can lower blood pressure. \n" +
                        "Holding each exercise for 10 to 30 seconds is necessary in the meanwhile to guarantee gains. \n" +
                        "Stretches for increasing flexibility include: yoga and Pilates"
            )
        )
    }

    if (title == Constansts.infoTitle12) {
        infoDetailList.add(
            InfoDetailModelClass(
                "How Can Hypotension Be Diagnosed?",
                "If you experience the symptoms of chronic hypotension, you might be interested in learning how the hospital will identify and treat you. Let's examine what you should know about diagnosing hypotension. \n" +
                        "If your doctor thinks you have hypotension, he may need to do the tests listed below:"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "1. Blood test",
                "Blood tests are the most efficient technique to understand your overall blood information. We'll check your blood pressure, blood sugar, and red blood cell count to see if you have hypotension or not."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "2. Tilt table test",
                "The tilt table test is another often used procedure to examine for orthostatic hypotension. Before the exam, you will take a brief period of supine rest. The table would be steadily raised to an upright position during testing. Your blood pressure, heart rate, and symptoms will all be monitored throughout this period. \n" +
                        "If your systolic pressure drops by 20 mm or your diastolic pressure lowers by 10 mmHg from the baseline, you are said to have orthostatic hypotension. \n" +
                        "The patient should be quickly placed back in the supine position if symptoms arise during testing."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "3. Electrocardiogram",
                "An ECG is a must to look for any heart issues that can cause blood pressure decreases. To obtain the heart's electrical signals, soft, adhesive patches (electrodes) are applied to the skin of your chest, arms, and legs during this painless, noninvasive examination. \n" +
                        "The test would properly identify any anomalies in your heart's rhythm, structural problems, issues with the flow of blood and oxygen to your heart muscle, as well as any other cardiac conditions.\n"
            )
        )


    }


    if (title == Constansts.infoTitle13) {
        infoDetailList.add(
            InfoDetailModelClass(
                "First-Line Hypotension Treatment",
                "If the results of your tests are good and you are experiencing symptoms, you may need medical treatments to treat hypotension. \n" +
                        "How then are low blood pressure treatments?"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "1. Fludrocortisone",
                "A drug called fludrocortisone appears to be effective for treating most forms of low blood pressure. By elevating the body's salt content and resulting increase in blood volume, it raises blood pressure. \n" +
                        "Headache, nausea, dizziness, insomnia, supine hypertension, and congestive heart failure are some of the side effects. \n" +
                        "• Enhanced danger of infection"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "WHO shouldn't consume it? ",
                "• Before using it, patients with lung, liver, kidney, or cardiac issues should see their doctors. \n" +
                        ". Pregnant and nursing women, those who are nursing, those who have a severe allergy, and those who are at risk for cancer, hypertension, diabetes, or any other infection should exercise caution when taking it.\n"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "2. Pyridostigmine",
                "Pyridostigmine would aid individuals with orthostatic hypotension by enhancing nerve cell transmission and inducing the reflex that regulates blood pressure, which would be especially beneficial for those with high supine blood pressure. \n" +
                        "Its negative effects, however, are minimal and modest. \n" +
                        "Consequences include: • Leg cramps \n" +
                        "• More frequent urination"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "WHO shouldn't consume it?",
                "• People who have an obstruction in their urinary or gastrointestinal tract. Additionally, anyone who has a severe allergy, is pregnant, breastfeeding, or is taking the medication should consult a doctor before consuming it."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "3. Midodrine ",
                "Your blood vessels narrow as a result of it activating receptors that raise blood pressure. Even if you are at rest, midodrine can raise your blood pressure. Therefore, you should only use this medication if your significantly low blood pressure is interfering with your daily life or other treatments are ineffective. \n" +
                        "Symptoms include: \n" +
                        "• Chills\n" +
                        "• Numbness\n" +
                        "• Headache\n" +
                        "• Dizziness\n" +
                        "• Nausea\n" +
                        "• Fatigue\n" +
                        "• Difficulty in urination\n" +
                        "• Increased urination\n"
            )
        )


    }


    if (title == Constansts.infoTitle14) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Hypertensive Emergency Care",
                "When blood pressure suddenly spikes (systolic readings of 180 or higher and/or diastolic readings of 120 mmHg), emergency care may be needed. What can you do as first aid after dialing for emergency medical assistance or while you wait for the ambulance? No need to worry; we have everything ready for you."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "1. Calm yourself down benefits blood pressure lowering",
                "All people with hypertension must remember that less stress results in lower blood pressure. Systolic blood pressure can be reduced by at least 10 mmHg, according to research, by completely resting for a few minutes. Therefore, it is the healthiest strategy to reduce hypertensive urgency-related blood pressure. You can try the relaxation techniques listed below: \n" +
                        "• Stop your current task\n" +
                        "• Take deep breaths\n" +
                        "• Sit down or lie flat\n" +
                        "• Listen to relaxing sounds\n" +
                        "• Meditate\n"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "2. Take your blood pressure medication",
                "If you have hypertension, you need to take the blood pressure medications your doctor has prescribed. The major treatment for high blood pressure is medication. Therefore, if you or a member of your family has a hypertensive emergency, don't neglect to get medical attention as quickly as you can."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "3. Have dark chocolate",
                "A tiny piece of dark chocolate can assist in lowering blood pressure. It has procyanidins and catechins, both of which can widen blood arteries. According to studies, eating dark chocolate decreased systolic and diastolic blood pressure by 2 to 3 mmHg. Even though the adjustments are little, the positive impact is real. \n" +
                        "Additionally, dark chocolate can indirectly lower your blood pressure by encouraging the release of calming endorphins. Time to keep some on hand just in case.\n"
            )
        )


    }


    if (title == Constansts.infoTitle15) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Hypotensive Emergency Care",
                "There may be a medical emergency if your blood pressure falls dramatically or very low. Making an urgent medical appointment with your doctor and carefully reviewing your current pharmaceutical regimen should be your initial steps. What else can you do, though? You won't remain clueless for long if you follow the advice below."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "1. Lie down",
                "Try to securely sit or lie down on a flat surface as soon as you feel any hypotensive symptoms. Continuing to stand can exacerbate your postural hypotension. Sitting or lying down helps your blood pressure return to normal."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "2. Stay hydrated",
                "Dehydration is one of the common causes of hypotension. So, drinking additional liquids, such as water, coconut water, and sports drinks, can help you stay hydrated and prevent dehydration. Additionally, you can add a tiny bit of salt or sugar to raise your blood pressure or bring your glucose level back to normal."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "3. Take adequate salt",
                "If you want your blood pressure to rise, try adding extra salt to your diet or licking a pinch of salt. Additionally, you can consume sports beverages or oral rehydration salts (ORS), which can help you rehydrate and deliver salt and other electrolytes to lower your blood pressure. \n" +
                        "However, if you have diabetes, stay away from ORS, and watch how much salt you consume. It could result in issues including fluid retention and elevated blood pressure.\n"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "4. Wear compression stockings",
                "Blood typically pools in the legs of those who suffer orthostatic hypotension. \n" +
                        "The thigh-high or waist-high version will be effective since compression stockings function by gently pressing your legs to help blood flow to your heart. \n" +
                        "Please be aware that some patients should not wear them. Thus, it is crucial to see a doctor. \n" +
                        "Now that you are aware of what to do, you can help someone who is experiencing hypotension. \n" +
                        "But resist the urge to pose as an authority. Always seek medical attention from a professional; never self-medicate."
            )
        )


    }

    if (title == Constansts.infoTitle16) {
        infoDetailList.add(
            InfoDetailModelClass(
                "How does blood pressure change as you get older?",
                "Do you want to discover how ageing affects blood pressure? Adult male and female normal ranges, broken down by age group, are shown below."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Normal blood pressure by age for men:", "• Age 21-25: SYS 115.5 and DIA 70.5\n" +
                        "• Age 26-30: SYS 113.5 and DIA 71.5\n" +
                        "• Age 31-35: SYS 110.5 and DIA 72.5\n" +
                        "• Age 36-40: SYS 112.5 and DIA 74.5\n" +
                        "• Age 41-45: SYS 116.5 and DIA 73.5\n" +
                        "• Age 46-50: SYS 124 and DIA 78.5\n" +
                        "• Age 51-55: SYS 122.55 and DIA 74.5\n" +
                        "• Age 56-60: SYS 132.5 and DIA 78.5\n" +
                        "•Age 61-65: SYS 130.5 and DIA 77.5\n"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Why does the change happen ",
                "Age-related increases in blood pressure in humans have been demonstrated. Blood pressure rises with ageing and is primarily related to structural changes in the arteries or decreased elastic tissue, particularly with stiffening of the major arteries. \n" +
                        "In particular, as you become older, your systolic blood pressure (SYS) will rise by roughly 7 mmHg every ten years. Your DIA will likewise increase, though more slowly than your systolic blood pressure. Additionally, it should be understood that people's diastolic blood pressure may decline in their latter years. \n" +
                        "Up until menopause, when the SYS of women surpasses that of males, women exhibit lower SYS and DIA than men do.\n" +
                        "People between the ages of 30 and 84 or older will experience an increase in SYS, according to the Framingham Heart Study. Participants' DIA rose from 30 to 50 while gradually declining from 60 to at least 84 years of age. The majority of persons with isolated systolic hypertension are above 50.\n"
            )
        )


    }


    if (title == Constansts.infoTitle17) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Control of hypertension during the COVID-19\n" +
                        "\n" +
                        "What is the link between them?",
                "The American Heart Association claims that when compared to the same time period in 2019, people's blood pressure increased during the epidemic (April to December 2020). (pre-pandemic). A recent study that used de-identified health information from an employee wellness programme came to that result. Nearly 500,000 adults from all around the country are included in the statistics, with a 54% female representation and an average age of 45.7 years. They took blood pressure readings every year during employee wellness exams from 2018 to 2020. \n" +
                        "In particular, the average monthly rise in systolic blood pressure ranged from 1.1 to 2.5 mm Hg, and the average monthly rise in diastolic blood pressure ranged from 0.14 to 0.53 mm Hg. Those alterations happened regardless of age and the participants in the study's genders. \n" +
                        "But when older people's systolic blood pressure increased, women's blood pressure generally increased. The results of diastolic blood pressure increased in certain younger subjects. \n" +
                        "What the causes might be may be a mystery to you. Multiple triggers exist. On the one hand, people neglected to lead healthy lifestyles during the pandemic, which included skipping meals, getting insufficient rest, and engaging in fewer physical activities. They also experienced increased stress and alcohol consumption. On the other side, people would not visit the hospital or have their blood pressure taken as frequently as they did before to the pandemic.\n" +
                        "\n" +
                        "The risk of morbidity and mortality from COVID-19 infection is higher in elderly adults with hypertension. This is because high blood pressure damages the arteries and reduces blood flow, weakening the hearts of hypertension sufferers. Since coronavirus also directly affects their hearts, people are likely to experience severe symptoms when they contract COVID-19. Additionally, compared to Covid-19 patients without hypertension, those with hypertension are thought to have a 2.5 times higher risk of developing a serious illness.\n"
            )
        )


        infoDetailList.add(
            InfoDetailModelClass(
                "What should a patient with hypertension do? ",
                "You would want to find out what to do if you were aware of the rise in blood pressure during the pandemic and the connection between hypertension and COVID-19. \n" +
                        "Here are some crucial rules: \n" +
                        "· Stock up on medicines \n" +
                        "During the pandemic, you must obtain enough medications to manage your blood pressure. So that you don't have to go to the pharmacy as frequently as before, you should check with your doctor and pharmacist to see if you can receive a bigger supply of prescription drugs. \n" +
                        "Additionally, you should stock up on over-the-counter medications in case you develop a fever or experience other health issues.\n"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Measure BP regularly at home",
                "People above 18 years need to measure their blood pressure (BP) regularly. To protect yourself against COVID-19, it is recommended to measure your BP at home rather than at hospitals."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Practice a healthy lifestyle",
                "Living a healthy lifestyle can help you get blood pressure controlled. You need to keep a healthy weight, eat a DASH diet, limit alcohol, exercise regularly, manage stress, and quit smoking.\n" +
                        "Following a DASH diet includes:\n" +
                        "• Eating fruits, vegetables, whole grains, and lean meats\n" +
                        "• Taking low-fat dairy products\n" +
                        "• Limiting foods with saturated and trans fats, such as processed foods\n" +
                        "• Cut back sugary foods and drinks\n"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Keep up with your hygiene ",
                "To prevent COVID-19, it's crucial to practice good hygiene. Keep commonly touched surfaces clean, and wash your hands frequently with soap or alcohol-based hand rub. Additionally, when sneezing or coughing, cover your mouth. Don't use your tissues again thereafter, and wash your hands right away. "
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Remain socially distant ",
                "Maintain a safe distance of at least 1 meter and stay away from people, crowds, and close contact. When physical distance cannot be maintained or in places with poor ventilation, put on an appropriate mask in the interim."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Get a flu vaccination",
                "You ought to receive a flu shot. Since coronavirus symptoms are similar to those of the flu, it may be more challenging for your doctor to make a diagnosis if you become ill. "
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Adhere to medical guidance. ",
                "In spite of the fact that it can be difficult for you to visit your doctor frequently during the pandemic, you can speak with your doctor by phone or email to follow medical instructions."
            )
        )


    }



    if (title == Constansts.infoTitle18) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Discover the Causes of Hypertension",
                "Many hypertension patients today place little value on eating a balanced diet and making other lifestyle modifications, which results in unstable blood pressure. Although the precise causes of high blood pressure are unknown, various variables might be at play. \n" +
                        "It should be noted that the topics covered here are those related to main hypertension risk factors. Since the aetiologia of secondary hypertension can be identified through diagnosis, it will not be discussed."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "1. Genetics",
                "As unjust as it may seem, you have a high probability of acquiring hypertension if either of your parents does. Your DNA is likely to have some of the hypertension-causing genes from your family."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "2. Lifestyle habits\n" +
                        "• Feeling anxious",
                "Chronically high levels of stress alter your body's hormonal balance, increasing your risk of high blood pressure. "
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• A poor diet ",
                "You will experience dehydration, or higher blood pressure, if you consume too much sodium, too little potassium, and too much alcohol. Processed foods, salt, and other foods are examples of foods high in sodium. Fruits and vegetables are high in potassium. "
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Excess weight ",
                "An increased need for oxygen and nutrition is indicated by excess fat. It results in increased blood flow through blood vessels, increasing the pressure on blood vessel walls."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "   • Smoking",
                "Smoking increases the risk of high blood pressure by stimulating the sympathetic nervous system strongly. "
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Not working out ",
                "People with sedentary lifestyles typically have greater heart rates. The force with which the heart pumps blood through the arteries increases with heart rate. When the heart beats, the blood pressure increases."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "2.Medicines", "Certain medicines can raise your blood pressure:\n" +
                        "• The contraceptive pill\n" +
                        "• Steroids\n" +
                        "• Non-steroidal anti-inflammatory drugs\n" +
                        "•Some pharmacy cough and cold remedies\n" +
                        "• Some herbal remedies\n" +
                        "• Some recreational drugs\n" +
                        "• Some antidepressants\n"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "4. Age and gender ",
                "Men are more likely than women to acquire hypertension before the age of 50. Their risks of developing high blood pressure after the age of 50 are essentially the same. Women are more likely than men to have hypertension after the age of 65. The average blood pressure rises with age in both men and women equally. Systolic blood pressure increases greater than diastolic blood pressure during this time. \n" +
                        "It is clear that both hereditary and environmental variables can cause hypertension. Although you have little influence over those genetic variables, you may monitor your blood pressure frequently to detect hypertension and receive prompt treatment for it. Besides, you should pay attention to those external ones to avoid hypertension and keep a healthy lifestyle.\n"
            )
        )


    }



    if (title == Constansts.infoTitle19) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Blood pressure monitoring at home",
                "Since symptoms only appear when there are significant variations in blood pressure values, people who have hypotension or hypertension will typically not even be aware of blood pressure changes. \n" +
                        "You must therefore periodically check your blood pressure, which is also a useful way to assess your health. \n" +
                        "The significance of taking your blood pressure at home is highlighted by the fact that a variety of hospital-related circumstances may interfere with your blood pressure monitoring. Would you like to learn how to easily take your blood pressure at home? Keep reading.\n"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "1. Check your monitor's accuracy beforehand",
                "Take the blood pressure monitor to the hospital and have your doctor check it before you start using it. In the meanwhile, you must frequently check to see if your device has been damaged. You must maintain it in a location with an appropriate temperature for storage."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "2. Measure your blood pressure in the morning and evening",
                "The best time to monitor yourself in the morning is right before breakfast and any medications. However, please refrain from measuring as soon as you awaken. The following measurement should take place after 6:00 pm. Take two or three readings each time to ensure accuracy."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "3. Measurement Preparation ",
                "At least 30 minutes before your measurement, avoid physical activity, smoking, drinking alcohol, coffee, and tea. Clear your bladder if necessary before beginning. Get a cushion for your back and take a five-minute nap. Your blood pressure reading will without a doubt be accurate after all of these preparations. Please take note that you should not forego your hypertension treatment due to the measurement."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "4. Correct Position ",
                "Make sure your arm is bare and resting on a table at heart level as you measure, keeping your back supported and legs at ease. \n" +
                        "Attach the cuff to the upper arm so that its lower border is 2.5 cm above the elbow. Inaccurate measurement results will arise from positioning the cuff incorrectly. It is advised to check your blood pressure on the same arm so that readings are comparable. Blood pressure readings often vary by two points, although variations of up to 10 mmHg are considered acceptable. You should see a doctor if it is excessively huge.\n"
            )
        )


        infoDetailList.add(
            InfoDetailModelClass(
                "5. Take repeat readings",
                "When you have your first reading, you should let go of your cuff and get ready to follow the procedures above to get your second set or even third reading. In order to maintain ongoing monitoring, you can manually enter readings into our app if your device is unable to do so automatically.\n" +
                        "\n" +
                        "You'll undoubtedly do better at frequently monitoring your blood pressure at home if you use the advice above. On the plus side, it will make it easier for your doctor to treat you. The converse is also true—it might make you more aware of the link between a healthy lifestyle and appropriate blood pressure. However, it should be noted that your home self-monitoring will never be able to take the position of your doctor. Simply use it as a guide and consult your doctor as necessary.\n"
            )
        )


    }



    if (title == Constansts.infoTitle20) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Change your way of living to overcome hypotension.",
                "Different forms of hypotension occur in different populations and are rather prevalent. Raising awareness about nutrition and lifestyle choices is your best option if you decide against using medication to treat low blood pressure with moderate symptoms in order to reduce those symptoms and raise low blood pressure to a healthy level. This article outlines various lifestyle modifications that can aid you in your efforts to both avoid and treat hypotension."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "1. Eat a well-balanced diet",
                "One of the causes of hypotension and other negative effects is a lack of nutrition. Anemia sets in when your body lacks the nutrients it needs to produce enough blood, which decreases your blood pressure. \n" +
                        "As a result, it is imperative to consume extra salt, caffeine, and water, as well as meals high in folate and vitamin B-12. Additionally beneficial for hypotension, eating smaller, more frequent meals.\n"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "2. Check your blood pressure at home",
                "An important step and a simple technique to control low blood pressure is to check your blood pressure at home. You can • Encourage a better understanding of your situation by conducting self-monitoring of your blood pressure at home. \n" +
                        "• Assistance with early detection and management of low blood pressure. \n" +
                        "• Put money aside to visit a doctor."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "3. Check your blood sugar regularly",
                "Hypotension and diabetes are closely related. Low blood pressure is more common in diabetics and in people with high blood sugar levels. This is because diabetes can harm the neurons in your body that regulate your blood pressure. The nerve receptors in your arteries may therefore not function as they should to promote physiological changes. If you don't make quick and significant improvements, your blood pressure will drop dangerously. \n" +
                        "The second reason for low blood pressure is volume depletion brought on by diuresis, which happens when blood sugar levels are high. \n" +
                        "Because of this, be sure to check your blood sugar levels frequently throughout the day in addition to monitoring your blood pressure."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "4. Treat infections",
                "Even though people occasionally disregard infections, they can be to blame for low blood pressure. Bacterial and viral infections cause toxins to be released into your bloodstream, which can result in a dangerous drop in blood pressure. Find out whether you have any infections by asking your doctor, then start treatment right away."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "5.Exercise regularly",
                "Patients with low blood pressure must perform some simple activities. Such low-intensity activities can assist increase heart rate to improve blood circulation. To help with low blood pressure, try exercising for 30 to 60 minutes two or three days a week, but stay away from hot, muggy weather."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "6. Relax your mind and body",
                "Physical factors including fatigue, stress, and anxiety can occasionally induce low blood pressure. The best method to treat the hypotension they create is to unwind mentally and physically. You can calm your body and mind by engaging in gradual muscle relaxation, yoga, meditation, and mental visualization."
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "7.Other tips for hypotensive patients",
                "• Wear compressive socks that extend to the thighs or waist.\n" +
                        "• Get up gradually after you've been sitting or lying down.\n" +
                        "• Avoid standing for long periods of time.\n" +
                        "• Sit up and breathe deeply for a few minutes and move legs first before getting out of bed.\n" +
                        "• Avoid long exposure to hot water or visiting saunas, hot tubs, or steam rooms."
            )
        )


    }


    if (title == Constansts.infoTitle21) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Understanding and Treating Gestational Hypertension\n" +
                        "\n" +
                        "What is gestational hypertension?",
                "A type of elevated blood pressure known as gestational hypertension frequently begins around the 20th week of pregnancy and resolves after delivery. Approximately 6% of all pregnancies have it. This illness can cause swelling in the hands and face, headaches, eye and skin discoloration, and visual problems in pregnant women."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "A woman is diagnosed with gestational hypertension when:",
                "• The measurements of her blood pressure are greater than 140/90 mm Hg. \n" +
                        "• For the first 20 weeks of her pregnancy, she had normal blood pressure. \n" +
                        "• She doesn't have proteinuria (excess protein in the urine)."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Why is gestational hypertension a concern?",
                "If the aforementioned criteria apply to you, you should be particularly concerned about your pregnancy because gestational hypertension can result in a number of issues. \n" +
                        "In general, it can reduce blood flow to the expecting mother's liver, kidneys, brain, uterus, and placenta, among other organ systems. \n" +
                        "Risks for both the mother and the unborn child increase when it gets severe (blood pressure readings higher than 160/110 mm Hg), including: Placental abruption (premature placenta separation from the uterus); intrauterine growth restriction; and preterm birth (poor fetal growth), Stillbirth, seizures (eclampsia); death of the mother and child; aging-related increases in the risk of hypertension and cardiovascular disease \n" +
                        "The infant must be delivered early, before 37 week’s gestation, due to these dangers."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Remedies for Gestational Hypertension\n" +
                        "\n" +
                        "• Medications",
                "Although debatable, rug therapy is useful for reducing pregnancy-related hypertension. Given that there are potential dangers for both the mother and the child, it should only be used after cautious selection and administration. \n" +
                        "We advise you to only use it as directed by your doctor to lower severe hypertension. Additionally, the American College of Obstetricians and Gynecologists (ACOG) advises starting drugs right once when blood pressure readings are higher than 160/110 mm Hg for 15 minutes or more. Drugs that apply include: \n" +
                        "• Methyldopa: a blood vessel relaxant with a central action \n" +
                        "• Beta-blockers Oxprenolol and Labetalol"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Prenatal Monitoring",
                "Regular prenatal examinations for both the mother and the unborn child are advised by ACOG. \n" +
                        "For the mom: \n" +
                        "• Weekly examination of protein in the urine; • Weekly evaluations of your platelet count, serum creatinine, and liver enzyme values (indicating preeclampsia) \n" +
                        "• Frequently checking your blood pressure, either at home or with a doctor (after receiving medical guidance and the proper supplies) \n" +
                        "For the newborn: \n" +
                        "• Every three to four weeks of gestation, ultrasounds to assess fetal growth are performed. \n" +
                        "During specific stages of pregnancy, hospitalization is advised if your illness worsens."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Healthy Lifestyle",
                "A healthy lifestyle includes: Eating fruits, vegetables, whole-grain bread, lean meats, and low-fat dairy; Prenatal vitamins; and Consuming healthful foods (Get more calcium, folic acid, magnesium and zinc) \n" +
                        "Avoid using tobacco and alcohol. \n" +
                        "• Get in shape and lose weight before getting pregnant • Move around and take regular walks while pregnant • Lessen workplace stress Attend routine examinations both during pregnancy and following birth. \n" +
                        "Although prenatal hypertension cannot be prevented, you can still take all the necessary precautions to keep both you and your unborn child as healthy as possible throughout the pregnancy."
            )
        )


    }



    if (title == Constansts.infoTitle22) {
        infoDetailList.add(
            InfoDetailModelClass(
                "9 Foods to Avoid If You Have Hypertension",
                "People with hypertension can better manage their blood pressure by following a healthy diet, which includes the DASH diet, and may even lessen the need for medication. \n" +
                        "Additionally, certain food categories that may cause your blood pressure to rise must be avoided. Continue reading to learn which foods people with hypertension should avoid.\n"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "1. Salt ",
                "Salt intake needs to be restricted since it significantly contributes to high blood pressure. Blood pressure rises as a result of sodium's effect on the blood's fluid content. \n" +
                        "The American Heart Association (AHA) recommends a salt intake of 1,500 mg per day for patients with hypertension (less than half a teaspoon). \n" +
                        "But cutting back on the table salt you add to your meals is insufficient. \n" +
                        "Salt is a common ingredient in processed and packaged meals. Reading food labels before purchasing it and substituting tasty, salt-free herbs and spices for salt are thus two actions that persons with hypertension need to do."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "2. Salty snacks",
                "It's time to find wholesome alternatives to your preferred salty snacks. Potato chips, crackers, jerky, dips, and other salty snacks are high in sodium and calories. A diet that is too high in calories leads to weight growth, which raises blood pressure and damages heart health. \n" +
                        "Low-sodium variants are available for you to consume in moderation."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "3. Pickles",
                "Pickled foods include a lot of sodium since salt is needed to flavour and preserve them. \n" +
                        "Pickled foods are salted more densely the longer they are preserved in canning liquids. Avoid pickled foods like pickled cucumber, kimchi, sauerkraut, and other similar items if you have hypertension. \n" +
                        "You should at the very least wash them before eating if you can't help taking them."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "4. Processed meats",
                "Despite being delicious, processed deli and lunch meats are frequently high in salt. This is as a result of their salt-based preservation, seasoning, and soaking processes. \n" +
                        "It is very simple to ingest more salt than the recommended daily limit of 1500 mg while eating processed meats. The U.S. Department of Agriculture (USDA), for instance, states that two slices of bologna have 910 mg of salt in them. There are 567 mg in one hot dog, or frankfurter."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "5. Condiments",
                "Even condiments that don't taste salty could contain a lot of sodium. \n" +
                        "Salad dressings, red and white pasta sauces, barbecue sauce, soy sauce, ketchup, and other sauces frequently include a lot of salt. When purchasing condiments, you should choose those with reduced salt or sodium content and study the labels."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "6. Potassium-rich food",
                "Including potassium-rich foods in one's diet while taking medicine to lower blood pressure can be harmful. ARBs and ACE inhibitors are two popular blood pressure-lowering drugs. \n" +
                        "They could result in your body retaining more potassium. That often has no negative effects. But let's say you consume foods high in potassium while taking ACE inhibitors or ARBs. The chance of developing irregular heartbeats will therefore increase as a result of having too much potassium. Bananas, oranges, avocados, tomatoes, white and sweet potatoes, and dried fruits, particularly apricots, are foods high in potassium."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "7. Licorice",
                "According to the U.S. Food and Drug Administration, licorice can be dangerous for those with hypertension because it lowers potassium levels in the body and results in an arrhythmia (irregular heartbeat) (FDA). Additionally, consuming licorice may lessen the effects of blood pressure or diuretic (urine-producing) drugs that are used to manage blood pressure levels."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "8. Grapefruit",
                "You should avoid eating grapefruits or drinking grapefruit juice if you take medication to decrease your blood pressure. The U.S. Food and Drug Administration (FDA) has established that grapefruit interacts with some medications and increases the amount of those pharmaceuticals that enter the bloodstream. Side effects are more likely to occur in that situation."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "9. Sugar",
                "People's blood pressure is not primarily raised by sugar. People who consume excessive amounts of sugar are more likely to put on weight and potentially get diabetes, which raises their chance of developing high blood pressure. The recommended daily sugar intake for women is 6 teaspoons (or 25 grammes) and for men is 9 teaspoons (or 36 grammes)."
            )
        )


    }

    if (title == Constansts.infoTitle23) {
        infoDetailList.add(
            InfoDetailModelClass(
                "5 Foods to Avoid If You Have Hypotension",
                "We all experience low blood pressure from time to time, and when it gets too low, it can be harmful. Continue reading to learn which meals persons with hypotension should avoid."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "1. Low salt foods",
                "Patients with hypertension should consume less salt to reduce their blood pressure. In contrast, hypotensive individuals must stay away from low-sodium foods in order to maintain a normal blood pressure level. Ask your doctor how much salt you should consume each day if you suffer from hypotension."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "2. Large meals",
                "Extra blood must be given to the stomach and small intestine in order to properly digest large meals. To move the extra blood, the heart must beat more quickly and vigorously. Because the heart and blood arteries don't behave as they should after meals in those with postprandial hypotension, they frequently experience fatigue and dizziness. Postprandial hypotension is more likely to be brought on by large meals than by modest ones. Therefore, having six to seven smaller meals rather than three larger ones is advised."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "3. Alcohol",
                "Alcohol dehydrates you, which can lower blood pressure. Alcohol is a diuretic, which means that it causes your kidneys to eliminate fluids from your bloodstream much more quickly than they would with other fluids. As a result, your blood volume or the amount of fluid moving through your blood vessels will lessen, which may cause dehydration and a reduction in blood pressure."
            )
        )


        infoDetailList.add(
            InfoDetailModelClass(
                "4. Red onion",
                "Red onions contain quercetin, which can considerably lower both systolic and diastolic blood pressure only by consuming them. As a result, if you have hypotension, you should avoid including red onions in your diet to prevent your blood pressure from dropping."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "5. Carrots",
                "According to PubMed Central, carrots, particularly raw carrots, can reduce blood pressure. A study that looked at the blood pressure of 2195 people between the ages of 40 and 59 discovered a strong correlation between eating raw carrots and reduced blood pressure. This is so because carrots are one of the best diuretic meals and aid in the body's detoxification of all pollutants. If that happens, you can become dehydrated and lose blood pressure."
            )
        )


    }

    if (title == Constansts.infoTitle24) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Why is a healthy diet important for lowering blood pressure?",
                "First of all, not all cases of low blood pressure have evident symptoms. Patients with low blood pressure typically only exhibit mild symptoms like weariness, thirst, and dizziness. To treat certain problems, a healthy diet is preferable to taking medications. \n" +
                        "Second, nutritional inadequacies are one of several variables that contribute to low blood pressure. Your body won't be able to produce the appropriate number of red blood cells if you don't consume enough iron, folic acid, and vitamin B12, which can lead to anemia and low blood pressure. \n" +
                        "To combat this problem, it is essential to understand which foods are beneficial for low blood pressure and to use nutritional therapy."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "What to eat to help raise low blood pressure:\n" +
                        "\n" +
                        "• Drink plenty of fluids",
                "By consuming a lot of liquids, you can keep your body from becoming dehydrated, which lowers blood volume and lowers blood pressure. Because of this, hypotension necessitates keeping your body hydrated by drinking lots of fluids. \n" +
                        "Doctors advise drinking at least two litres (about eight glasses) of water daily, with water consumption increasing in hot weather and during physical activity."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Drink caffeinated drinks",
                "Caffeinated tea and coffee can improve the cardiovascular system. The process results in an increase in heart rate, which raises blood pressure. That rise, though, is just transitory, and it only benefits those who do not regularly consume them. Caffeine-containing foods and beverages, such as chocolate, tea, cocoa, some sodas, and energy drinks, may be beneficial for those with low blood pressure."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Increase the intake of salt",
                "Foods heavy in salt might alter your body's water balance and raise your blood pressure. You can achieve this by doing the following: • Adding a pinch of salt to a glass of water • Consuming salty foods including tuna, olives, cottage cheese, salted almonds, and canned soup. \n" +
                        "• Snacking on salted nuts \n" +
                        "Your heart may suffer and develop heart disease if you eat too much salt. Find out from your doctor how much is ideal for you."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "•Eat foods high in vitamin",
                "In order for our body to create blood, vitamins 12 and folate are essential. Meat, eggs, fish, and dairy items like milk and cheese are good dietary sources of vitamin B12. Vitamin-12 is also abundant in foods like plant-based milk and morning cereals. To obtain adequate folate, consider foods like wheat germ, broccoli, beans, eggs, beets, citrus fruits, nuts, seeds, and liver."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Avoid skipping meals",
                "In most cases, skipping meals will result in overeating later to make up for it, which is problematic for breakfast skippers and intermittent fasters in particular. Take care not to frequently skip meals to avoid such issue."
            )
        )


    }

    if (title == Constansts.infoTitle25) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Get High blood pressure Tests",
                "Regular blood pressure checks can provide a crucial window into your health and give you the chance to keep your heart and brain in excellent shape because high blood pressure typically has no symptoms. \n" +
                        "Let's say that you have already received a diagnosis of hypertension. In that situation, your doctor might advise you to undergo a number of further tests to rule out the diagnosis of hypertension and its underlying causes and problems."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "Tests for patients with chronic hypertension\n" +
                        "• Blood Tests",
                "Blood tests may be required to determine if you have secondary hypertension due to a severe or treatable health condition and assist in diagnosing hypertension. It tests:\n" +
                        "• Electrolyte levels\n" +
                        "• Blood glucose\n" +
                        "• Blood urea nitrogen\n" +
                        "• Creatinine levels (to assess kidney involvement)"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Urine Tests ",
                "Testing the urine can assist identify whether high blood pressure is being caused or exacerbated by diabetes, kidney disease, or illegal drugs. \n" +
                        "It evaluates: \n" +
                        "• Electrolytes \n" +
                        "• Hormones."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Cholesterol Tests",
                "It will test your lipid profile for levels of various kinds of cholesterol."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Renal Function Tests",
                "Renal Function Tests can assess damage or enlargement of the kidneys and adrenal glands. It tests:\n" +
                        "• Ultrasound of the kidneys\n" +
                        "• CT scan of the abdomen\n" +
                        "• Special tests for hormones of the adrenal gland"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Thyroid Function Tests\n" +
                        "• Eye Examination\n" +
                        "• Echocardiogram\n" +
                        "• Ultrasound (Doppler\n" +
                        "• CT Scan or MRI\n", ""
            )
        )


    }
    if (title == Constansts.infoTitle26) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Drugs That Cause Hypertension",
                "Why do so many hypertensives struggle to lower their blood pressure? \n" +
                        "In persons who were not using antihypertensives, the researchers found that several drugs could raise blood pressure and increase the risk of uncontrolled hypertension. \n" +
                        "You should avoid using some medications to prevent your hypertension from growing worse because: Some medications can cause your blood pressure to rise to risky levels. \n" +
                        "Your blood pressure medication may interact with other medications and cease to function as intended."
            )
        )


        infoDetailList.add(
            InfoDetailModelClass(
                "Here are common types of drugs you may need to avoid:\n" +
                        "• Non-steroidal", "Anti-inflammatory Drugs (NSAIDs)\n" +
                        "NSAIDs, which include both prescription and over-the-counter medicines, are frequently used to treat illnesses including arthritis and relieve pain or reduce inflammation. These painkillers and anti-inflammatory drugs may make you retain water, raising your blood pressure or potentially jeopardizing your kidney and heart health.\n" +
                        "For example:\n" +
                        "• Indomethacin (Indocin, Tyvorbex)\n" +
                        "• Aspirin\n" +
                        "• Ibuprofen (Advil, Motrin IB)\n" +
                        "• Naproxen (Aleve, Naprosyn)\n" +
                        "• Piroxicam (Feldene)"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Cough and Cold Medications",
                "Decongestants can make blood pressure worse in two ways: one may cause an increase in your heart rate and blood pressure, and the other may stop your blood pressure medicine from working as it should. In particular, it will cause your blood vessels to narrow, which will make it more difficult for blood to flow through them. Decongestant use may also reduce the effectiveness of your blood pressure meds. For instance: \n" +
                        "• Phenylephrine; pseudoephedrine (Sudafed 12-hour) (Neo-Synephrine)"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Antidepressants",
                "Antidepressants alter how your body reacts to dopamine, serotonin, and other brain chemicals, which in turn affects your mood. \n" +
                        "These substances could raise your blood pressure.\n" +
                        "Consider this:\n" +
                        "• Monoamine oxidase inhibitors\n" +
                        "• Tricyclic antidepressants\n" +
                        "• Fluoxetine (Prozac, Sarafem)\n" +
                        "• Migraine Headache"
            )
        )


        infoDetailList.add(
            InfoDetailModelClass(
                "Medications ",
                "Some medications primarily function by constricting the blood arteries in your brain. \n" +
                        "But they also narrow the blood arteries all over your body, raising blood pressure. For instance: \n" +
                        "• Sumatriptan, Rizatriptan, and Zolmitriptan"
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Hormonal birth control",
                "The majority of birth control tablets contain hormones that may raise your blood pressure by constricting tiny blood arteries, as do other hormonal birth control methods including patches and vaginal rings."
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "• Weight Loss Drugs",
                "The norepinephrine effect of appetite suppressants frequently causes blood pressure to rise. Even worse, it may make heart disease worse."
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "• Herbal Remedies and Dietary Supplements",
                "Despite being natural, herbal supplements may not always be secure. \n" +
                        "As a result, they can increase your blood pressure or conflict with any blood pressure medications you're on. For instance, consider ephedra (ma-huang). sour orange \n" +
                        "• Ginseng (Panax quinquefolius and Panax ginseng), Guarana (Paullinia cupana), Licorice, Arnica (Arnica montana), and Panax quinquefolius (Glycyrrhiza glabra)"
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "Tips for Avoiding Medication Problems",
                "• Before purchasing a drug, check the label to make sure it doesn't contain any NSAIDs or decongestants that could make your high blood pressure worse. \n" +
                        "• Request a review of ALL of your prescription, over-the-counter, vitamin, herbal, and dietary supplement medications from your physician or pharmacist. \n" +
                        "• Add more medications to your regimen to manage your hypertension. For instance, taking acetaminophen rather than ibuprofen; using nasal sprays or antihistamines to treat congestion problems. \n" +
                        "• Adopt a better lifestyle rather than taking medication."
            )
        )

    }
    if (title == Constansts.infoTitle27) {
        infoDetailList.add(
            InfoDetailModelClass(
                "Discover the daily pattern of blood pressure",
                "Your blood pressure does change during the day since blood pressure does follow a daily pattern. \n" +
                        "It typically increases between the time you wake up in the morning and midday, declines in the late afternoon and evening, and reaches its lowest point when you go to sleep. This pattern is compatible with the circadian rhythm, a natural 24-hour activity cycle that governs our patterns of sleep and wakefulness in the body. \n" +
                        "Some people's blood pressure patterns are aberrant. There are other abnormal blood pressure patterns, such as: Nocturnal hypertension and morning-surge hypertension (High blood pressure during the night) \n" +
                        "• Nondripping blood pressure (a decline in blood pressure of less than 10% over night) when you notice abnormal changes in your blood pressure remember to record your numbers, activities, and the time that the numbers reach normal again, which will be helpful for the doctors\n"
            )
        )


        infoDetailList.add(
            InfoDetailModelClass(
                "Factors that affect your blood pressure pattern:\n" +
                        "• Mental and physical stress",
                "Blood pressure might momentarily rise as a result of daily stress, including professional, personal, and health-related stress, as well as from seeing the doctor."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Exercise",
                "Due to increased cardiac output, whose magnitude depends on exercise intensity, blood pressure rises during exercise and drops after. "
            )
        )


        infoDetailList.add(
            InfoDetailModelClass(
                "• Food intake and postprandial hypotension",
                "BP somewhat increases before meals, but it decreases thereafter. Tyramine-containing meals and dietary salt intake can raise blood pressure. In contrast, taking extra potassium, calcium, or magnesium lowers blood pressure."
            )
        )

        infoDetailList.add(
            InfoDetailModelClass(
                "• Obesity and sleep apnea",
                "In obese people, obstructive sleep apnea is frequent. Sleep apnea and obesity may contribute to nocturnal hypertension. "
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "• Hot Bathing",
                "When taking a hot bath, blood pressure tends to rise at first, drop during immersion, drop even more right away, and then slowly return to the baseline level."
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "• Drinking and smoking",
                "Both drinking alcohol and smoking cigarettes can raise blood pressure during the day."
            )
        )
        infoDetailList.add(
            InfoDetailModelClass(
                "2. Caffeine ",
                "Blood pressure can dramatically rise briefly after consuming caffeine."
            )
        )


    }


    return infoDetailList
}



