package com.vboard.bp_recorder_app.utils

import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.ui.fragments.blood_pressure.model_classes.BPTypesModelClass
import com.vboard.bp_recorder_app.ui.fragments.info_module.details.InfoDetailModelClass
import com.vboard.bp_recorder_app.ui.fragments.weight.WeightTypesModelClass
import timber.log.Timber
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow


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

fun getInfoTitleIconsList(): ArrayList<Int> {
    var info_icon_bg_list: ArrayList<Int> = arrayListOf()

    info_icon_bg_list.add(R.drawable.icon_info_1)
    info_icon_bg_list.add(R.drawable.icon_info_2)
    info_icon_bg_list.add(R.drawable.icon_gestanioal_hypertension)
    info_icon_bg_list.add(R.drawable.icon_info_4)
    info_icon_bg_list.add(R.drawable.icon_info_5)

    info_icon_bg_list.add(R.drawable.icon_info_6)
    info_icon_bg_list.add(R.drawable.icon_gestanioal_hypertension)
    info_icon_bg_list.add(R.drawable.icon_info_8)
    info_icon_bg_list.add(R.drawable.icon_9)
    info_icon_bg_list.add(R.drawable.icon_10)

    info_icon_bg_list.add(R.drawable.icon_11)
    info_icon_bg_list.add(R.drawable.icon_12)
    info_icon_bg_list.add(R.drawable.icon_13)
    info_icon_bg_list.add(R.drawable.icon_gestanioal_hypertension)
    info_icon_bg_list.add(R.drawable.icon_gestanioal_hypertension)

    info_icon_bg_list.add(R.drawable.icon_16)
    info_icon_bg_list.add(R.drawable.icon_17)
    info_icon_bg_list.add(R.drawable.icon_gestanioal_hypertension)
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
        infoDetailList.add(InfoDetailModelClass("Identify your blood pressure type.\n" +
                "Hypertension", "The human cardiovascular system resembles a water circulatory system in that blood constantly circulates like water, carrying nutrients and oxygen to the organs and blood arteries. \n" +
                "Blood flow gets challenging when blood vessels become less elastic. When this happens, your body tries to force blood to flow with more force, which leads to hypertension."))

        infoDetailList.add(InfoDetailModelClass("Hypotension",
                "You should monitor your blood pressure if it drops below 90/60 mmHg on a regular basis or drops sharply below 20 mmHg. In most cases, hypotension without symptoms doesn't require medical attention. \n" +
                "Nevertheless, if symptoms or a rapid fall occur, a precise diagnosis and specific therapy are required."))

    }


    if (title == Constansts.infoTitle4) {
        infoDetailList.add(InfoDetailModelClass("Break Myths About Blood Pressure", "There are a lot of myths about blood pressure. These myths can actually make your blood pressure control ineffective or possibly damage your situation. Here are seven typical misunderstandings about blood pressure. Stop trusting them after reading the following."))
        infoDetailList.add(InfoDetailModelClass("I don't need to worry if my blood pressure is low.", "In general, if you have hypotension, a few little lifestyle adjustments can help. However, you require medical attention if your blood pressure drops suddenly by more than 20 mmHg, or if you experience fainting, lethargy, impaired vision, etc. Hypotension can be fatal if it is not immediately treated."))
        infoDetailList.add(InfoDetailModelClass("If no symptoms appear, I don't have hypertension", "Do you know that high blood pressure is referred to as a \"silent killer\"? It frequently goes undetected and may not even show any symptoms, or it may show very moderate signs that are not treated seriously even though it is seriously harming your health. \n" +
                "When symptoms start to show up, it means that the heart, brain, kidney, blood vessel, or other organs have suffered substantial damage and are no longer able to operate normally. The ideal opportunity for treatment has already passed by at that point. \n" +
                "In the meantime, problems arise and life is in danger."))
        infoDetailList.add(InfoDetailModelClass("Treatment for hypertension is available", "The only form of hypertension that has a known cure is secondary hypertension. Secondary hypertension will go away after the secondary ailment is treated. \n" +
                "Although primary hypertension cannot yet be cured, it is still feasible to prevent or lessen the harm it does to the body by bringing blood pressure under control with drugs or by making positive lifestyle changes. It is false to say that treating high blood pressure with easy steps can prevent relapse."))


        infoDetailList.add(InfoDetailModelClass("Red wine consumption helps to control blood pressure", "According to a myth, the resveratrol in red wine might keep the heart healthy. \n" +
                "However, that claim cannot be supported by any investigation. Requiring large amounts of red wine to obtain the desired impact, which could seriously hurt your body, is required if resveratrol can truly safeguard your cardiac health. Additionally, alcohol has been shown to have no beneficial effects on the cardiovascular system."))

        infoDetailList.add(InfoDetailModelClass("Once my blood pressure is managed, I can stop using my medications.", "This is completely false and may deceive. The majority of patients with primary hypertension require lifelong drug therapy because there is no known treatment for the condition. Your blood pressure is under control because of pharmacological management, not because high blood pressure has been healed. Blood pressure is likely to rise again when the medicine is stopped. \n" +
                "Furthermore, stopping the usage of some medicines can result in dangerous withdrawal symptoms. \n" +
                "Thus, even if your blood pressure has been steady for a while, you should get medical advice before changing or stopping your prescription. Before changing the medicine, speak with the doctor. In accordance with your doctor's advice, you should cut back on your medication, keep an eye on your blood pressure, and lead an active lifestyle."))

        infoDetailList.add(InfoDetailModelClass("Youth won't get hypertension", "While middle-aged and elderly persons are more likely to have hypertension patients, young people can also be affected. Age, physical size, stage of sexual development, lifestyle, etc. all affect blood pressure levels."))

        infoDetailList.add(InfoDetailModelClass("Hypertension is only a problem for men", "Age truly affects how much of a risk differential there is between men and women. Men have a higher risk of developing hypertension than women do before the age of 45. They have virtually the same risk between the ages of 45 and 64. It is important to note that women are more likely than males to develop high blood pressure after the age of 64."))


    }

    if (title == Constansts.infoTitle5) {
        infoDetailList.add(
            InfoDetailModelClass("Types of hypertensions you should be aware of", "Do you know there are two primary forms of high blood pressure? High blood pressure can be either primary or secondary. To learn more about their many causes, continue reading."))


        infoDetailList.add(
            InfoDetailModelClass("Primary Hypertension", "Primary hypertension is the one that occurs most frequently. It denotes persistently elevated blood pressure that is unrelated to another illness. The exact aetiology of hypertension is unknown after years of investigation. Your blood pressure may rise as a result of poor diets, inactivity, using tobacco or alcohol, abusing weight-gaining substances, and other factors. \n" +
                    "Age will cause a rise in patients' blood pressure. It has taken some time to discover the cure. The majority of people therefore require lifelong medication to manage their blood pressure."))


        infoDetailList.add(
            InfoDetailModelClass("Secondary Hypertension", "Only 5 to 10% of hypertension cases are secondary, on average. Additionally, it is known that secondary hypertension is more common in younger persons. \n" +
                    "Secondary hypertension is brought on by a number of medical diseases or drugs, such as obstructive sleep apnea, kidney illness, adrenal gland tumours, thyroid issues, blood vessel anomalies, etc. Secondary hypertension will improve after people cease taking their medications or treat the underlying cause."))





    }


    if (title == Constansts.infoTitle6) {
        infoDetailList.add(InfoDetailModelClass("Headache", "High blood pressure and headache typically signal an emergency. Your brain's blood arteries may be harmed by too high blood pressure, which could raise intracranial pressure. On both sides of your head, pulsing headaches will be present, and any activity will just make them worse."))


        infoDetailList.add(InfoDetailModelClass("Dizziness", "Although taking antihypertensive medicine may cause dizziness as a side effect, you shouldn't take it for granted. Stroke is mostly brought on by severe high blood pressure. A stroke may be indicated by sudden dizziness, loss of balance, or shaky walking."))

        infoDetailList.add(InfoDetailModelClass("Nausea", "A hypertensive crisis could occur if you have hypertension and feel sudden nausea and loss of appetite. Usually, dizziness and nausea brought on by severely high blood pressure occur simultaneously."))

        infoDetailList.add(InfoDetailModelClass("shortness of breath", "Shortness of breath will result from high blood pressure's impact on heart and lung health. That might be easier to see when patients exercise."))

        infoDetailList.add(InfoDetailModelClass("Chest pain", "A major risk factor for heart attacks and heart failure is hypertension. Atherosclerosis caused by high blood pressure makes it difficult to transport oxygen and blood. It implies that less blood will be able to reach the heart and eventually cause angina, or chest pain."))

        infoDetailList.add(InfoDetailModelClass("Blurred vision", "On your eveballs, there are numerous tiny vessels. These vessels may become damaged and begin to bleed when your blood pressure reaches a risky level, which can result in blurry or even lost vision."))

        infoDetailList.add(InfoDetailModelClass("Nosebleed", "Unless your blood pressure is exceedingly high, hypertension typically doesn't produce nosebleeds. Your nose has been bleeding because it has damaged blood vessels. You must now either summon an ambulance or leave right away for the hospital."))

    }

    if (title == Constansts.infoTitle7) {

        infoDetailList.add(InfoDetailModelClass("Understand the issues that high blood pressure causes.", "Your health is seriously threatened by high blood pressure. Your kidneys, eyes, heart, and brain can all slowly deteriorate without any warning. \n" +
                "Read on for a comprehensive explanation of the impacts of hypertension on your body."))

        infoDetailList.add(InfoDetailModelClass("Hypertension causes heart disease", "Heart failure, coronary artery disease, and other conditions can all be brought on by high blood pressure. According to some research, high blood pressure is the root cause of roughly 25% of occurrences of heart failure. \n" +
                "Due to hypertension, arteries become less flexible and fatty substances block blood vessels, making it impossible for the heart to receive smooth blood flow. The left ventricle must work harder at that time to pump blood to the body's numerous organs. \n" +
                "The left ventricle will eventually enlarge as a result of the increased strain, dramatically raising the chance of developing cardiac conditions. Heart failure develops when the heart weakens and is unable to pump blood."))

        infoDetailList.add(InfoDetailModelClass("Hypertension can harm your brain", "Transient ischemic attack, stroke, dementia, moderate cognitive impairment, and other brain conditions can all be brought on by high blood pressure. \n" +
                "Blood flow to the brain will be impeded by blocked arteries and elevated blood pressure. When the brain's blood supply is inadequate, it cannot function, which might cause a transient ischemic attack (TIA). \n" +
                "Brain blood arteries become less flexible as a result of high blood pressure. This occurrence prevents nutrition and oxygen from reaching the brain, which results in brain cells dying and stroke. Your risk of having a stroke increase with increasing blood pressure. According to studies, males who have high blood pressure have a 220 percent increased risk of having a stroke.\n" +
                "Except for TIA and stroke, high blood pressure can also result in vascular sclerosis of your cognitive area. That means patients can be affected with cognitive impairment and gradually may develop into dementia."))

        infoDetailList.add(InfoDetailModelClass("Hypertension will cause kidney problems", "Blood pressure and the kidney are strongly related. Through the kidneys, the blood transports metabolic waste. In a healthy kidney, around half a cup of blood is filtered per minute, and the extra water and metabolic waste are excreted as urine. \n" +
                "In order to resist the excessive pressure, blood vessel walls will thicken when the blood pressure is high. The blood channel narrows as a result of this alteration, which makes it difficult for waste to be filtered. Furthermore, the kidneys' essential blood supply cannot be ensured. Over time, it causes chronic kidney disease, ischemic atrophy of the kidneys, and renal failure to develop."))

        infoDetailList.add(InfoDetailModelClass("Hypertension affects eyesight", "Extreme high blood pressure will cause swelling of the retina. Patients will experience blurred vision or even loss of eyesight.\n" +
                "The retina is at the back of the eye and functions image focusing. When the blood pressure is too high, the blood vessel walls of the retina will thicken and make the blood vessels narrow. Over time, high blood pressure can damage retinal blood vessels, limit retinal function, and put pressure on the optic nerve, causing vision problems."))


    }

    if (title == Constansts.infoTitle8) {
        infoDetailList.add(InfoDetailModelClass("Understanding Hypotension Symptoms", "Hypotension symptoms include:\n" +
                "• Blurred vision\n" +
                "• Dizziness or fainting\n" +
                "• Inability to concentrate\n" +
                "• Nausea\n" +
                "• Rapid breathing\n" +
                "•Fatigue"))

        infoDetailList.add(InfoDetailModelClass("Understanding Hypotension Type", "Whether you experience these symptoms frequently and are unsure of the cause, have your blood pressure checked. Then, determine if you fall under one of the categories below. "))

        infoDetailList.add(InfoDetailModelClass("   1. orthostatic tachycardia ", "Orthostatic hypotension is the condition that occurs when a person stands up from a seated or lying down position. \n" +
                "It is widespread, affecting around one-fifth of seniors over 65. \n" +
                "Orthostatic hypotension can also be brought on by beta-blockers, ACE inhibitors, antidepressants, and medications used to treat Parkinson's disease."))

        infoDetailList.add(InfoDetailModelClass("   2.Postprandial hypotension", "Since postprandial hypotension nearly never affects young individuals and typically manifests after older folks have finished eating, it is frequently thought of as a geriatric condition. \n" +
                "Following a meal, the heart beats more quickly as the blood arteries in other parts of the body contract to keep blood pressure stable. However, the bodies of certain elderly persons are unable to function normally. Blood pressure drops when blood goes to the intestines because the heart rate cannot be raised enough and the vasoconstriction is insufficient to keep blood pressure stable."))

        infoDetailList.add(InfoDetailModelClass("   3.Neurally mediated hypotension", "The heart and the brain are unable to communicate, which results in this kind of low blood pressure. Neurally mediated hypotension can occur in people who are upset or who have been standing for a long time. The heart must pump blood more quickly during that time to get blood to the brain. However, the brain sends the signal that the heart rate should be reduced, which causes the blood vessels in the arms and lower body to swell, which reduces the amount of blood that reaches the brain."))

        infoDetailList.add(InfoDetailModelClass("Understanding Other Hypotension Causes\n" +
                "other causes of consistent hypotension", "• Pregnancy, hormonal changes affect the blood vessels and circulatory system so that blood pressure may be lower during the first 24 weeks of pregnancy\n" +
                "• Arrhythmia (abnormal heartbeat)\n" +
                "• Heart disease\n" +
                ". Certain over-the-counter drugs used in combination with hypertension drugs\n" +
                "• Endocrine disorders such as diabetes, adrenal insufficiency and thyroid disease"))

        infoDetailList.add(InfoDetailModelClass("Reasons for sudden fall of blood pressure", "• Loss of blood from bleeding\n" +
                "• Low body temperature\n" +
                "• High body temperature\n" +
                "• Heart muscle disease causing heart failure\n" +
                "• Sepsis, a severe blood infection\n" +
                "• Severe dehydration from vomiting, diarrhea, or fever\n" +
                "• A reaction to medication or alcohol\n" +
                "• A severe allergic reaction"))





    }

    if (title == Constansts.infoTitle9) {


        infoDetailList.add(InfoDetailModelClass("First-Line Hypertension Treatment", "If you are told that you have high blood pressure and that you require medical care, you might ask what kind of care is best for your body. Here, we outline four drugs that your doctor may recommend to you initially. \n" +
                "To better comprehend the therapy and reduce unpleasant hypertension, you can check the medications you are taking."))

        infoDetailList.add(InfoDetailModelClass("   1. Angiotensin-converting enzyme (ACE)", "In order to control blood pressure, this type of drug is crucial in relaxing blood vessels and obstructing the functions of specific hormones. It's also important to note that the blood flow to the kidneys would become less effective."))

        infoDetailList.add(InfoDetailModelClass("   WHO should not take ACE?", "• Expectant mothers \n" +
                "In the latter six months of pregnancy, ACE can be hazardous to unborn children. Therefore, you should choose a different method of blood pressure regulation under the supervision of your doctor in order to better safeguard yourself and your unborn child. \n" +
                "• Those who have kidney disorders \n" +
                "It is not safe for this sort of person to utilize ACE since it could lower the blood supply to the kidneys. \n" +
                "• Those who experience severe allergic reactions \n" +
                "Stop taking the ACE and seek medical attention right away if it causes your body to experience any serious allergic reactions."))

        infoDetailList.add(InfoDetailModelClass("   2. Diuretics", "Diuretics, which make it easier to urinate and eliminate extra sodium (salt) and water to lower blood pressure, are another prominent type of hypertension drug. When treating high blood pressure, it is frequently used with other drugs. \n" +
                "WHICH PEOPLE SHOULD NOT TAKE DIURETICS? \n" +
                "• People beyond age \n" +
                "Diuretics should be avoided since they typically react to dehydration more severely. \n" +
                "• Expectant and nursing mothers \n" +
                "Diuretics may be transferred from the mother's body to the infant, which could lead to the baby becoming dehydrated. \n" +
                "• Kids \n" +
                "Diuretics may result in a calcium shortage, which could affect bone growth."))

        infoDetailList.add(InfoDetailModelClass("   3. Angiotensin II receptor blockers (ARBs)", "We must discuss angiotensin to demonstrate how ARB lowers blood pressure. When it binds to a receptor, blood arteries may become constricted. The ARBs stop it from attaching to blood vessels, keeping the vessels open and lowering blood pressure. \n" +
                "WHO shouldn't use an ARB? \n" +
                "• Individuals with certain renal issues \n" +
                "People who have renal artery stenosis, or restricted arteries to the kidneys, or who have really low kidney function may respond severely to ARBS. \n" +
                "• ARBs can block the action of angiotensin II in people with low blood sodium levels, which will decrease the amount of sodium in the renal tubules and exacerbate your salt shortage.\n" +
                "Pregnant and breastfeeding women & people with severe allergy\n" +
                "Those people also need to ask for doctor's advice concerning taking ARBs."))

        infoDetailList.add(InfoDetailModelClass("   4. Calcium channel blockers", "You might be curious how calcium and blood pressure management are related. \n" +
                "However, calcium can enter heart and artery muscle cells, causing muscles to contract more forcefully and strongly. \n" +
                "Calcium channel blockers reduce blood pressure by preventing calcium from entering your blood arteries, which relaxes muscle contractions. \n" +
                "WHO SHOULD USE CALCUTHYNE CHAIN BLOCKERS SAFELY? \n" +
                "• Heart disease sufferers \n" +
                "Calcium channel blockers may cause harm to your health and lower your heart rate if you have structural heart disease or congestive heart failure. \n" +
                "• Women who are pregnant or nursing and those who have severe allergies"))






    }


    if (title == Constansts.infoTitle10) {
        infoDetailList.add(InfoDetailModelClass("How Can I Manage My Blood Pressure?", "It takes a lifetime to combat the \"silent killer.\" Make some modifications and don't only rely on the meds if you want to keep your heart healthy. By adopting the heart-healthy lifestyle recommended in this article, you might get your statistics back to normal. Let's do something right away to lower your blood pressure and enhance daily life."))
        infoDetailList.add(InfoDetailModelClass("   1.Keep a healthy weight", "Blood pressure and weight have a tight relationship. The blood pressure increases as weight increases. Excessive weight loss can widen blood arteries, improving the heart's ability to pump blood. According to research, decreasing weight can lower systolic and diastolic blood pressure by 4.5 to 8.5 and 3.2 to 6.5 mmHg, respectively. \n" +
                "The fact that only 5% of your body weight needs to be lost will not force you to do a lot of effort in order to significantly lower your blood pressure. \n" +
                "You must, however, place a high priority on keeping an eye on your waistline. \n" +
                "Your blood pressure may be impacted by excess body fat around your stomach. Men are advised to maintain a low waist measurement to less than 40 inches and women be no more than 35 inches."))

        infoDetailList.add(InfoDetailModelClass("   2. Eat a dash diet", "The Dietary Approaches to Stop Hypertension (DASH) diet was developed to lower cholesterol and treat and prevent high blood pressure. In addition, it helps with things like cancer risk reduction and weight loss. The standard DASH diet programme recommends consuming no more sodium than 1 teaspoon (2,300 mg) per day. The DASH diet can lower your systolic blood pressure by as much as 11mmHg by consuming less sodium and more potassium, magnesium, and calcium. \n" +
                "Learn to read food labels before purchasing anything to determine the sodium content. Stop purchasing it if it has more than 20% salt. \n" +
                "A DASH diet often consists of: • Consuming fruits, vegetables, whole grains, and lean meats."))
        infoDetailList.add(InfoDetailModelClass("   3. Limit alcohol", "Unexpectedly, alcohol plays a significant role in 16% of all instances of hypertension worldwide. Drinking more than is considered moderate can cause a 7 mm Hg increase in blood pressure. \n" +
                "Men should not consume more than two drinks per day, while women should limit their alcohol consumption to one drink per day in order to properly manage hypertension. One drink is equal to 12 ounces of beer, 5 ounces of wine, or 1.5 ounces of 80-proof liquor in order to quantify your alcohol consumption more naturally. \n" +
                "Attention! Alcohol consumption should be avoided if you are on hypertensive drugs because it will reduce their effectiveness."))


        infoDetailList.add(InfoDetailModelClass("   4. Exercise regularly", "Exercise causes your heartbeat and breathing to speed up, which strengthens the heart's ability to pump blood and lowers blood pressure. \n" +
                "If you have hypertension, attempt moderately intense aerobic exercises, weight training, and flexibility drills. You can drastically lower your blood pressure with 150 minutes a week, which is equivalent to certain prescription drugs. To make your routine interesting and healthful, you can opt to jog, walk, swim, lift weights, practice yoga, or do any other activity you enjoy. \n" +
                "Consistency is key because stopping an exercise programme can cause your blood pressure to spike once more."))



        infoDetailList.add(InfoDetailModelClass("   5. Manage stress", "When you are under prolonged stress, your blood vessels narrow and your heart rate rises. While under stress, you are more prone to practice behaviors like consuming processed foods or alcohol, which can raise your blood pressure. \n" +
                "Here are some suggestions for reducing stress: \n" +
                "• Consider the problems you can solve. Make a strategy and do the activity as soon as you can if it irritates you. \n" +
                "•Take it easy. It's time to embrace yourself and indulge in your favorite activities, whether they be yoga, music, or reading. \n" +
                "• Be mindful to avoid bad habits like smoking and alcohol consumption."))


        infoDetailList.add(InfoDetailModelClass("   6. Quit smoking", "Every cigarette puff causes your blood pressure to rise right away and your heart to beat more vigorously. If you continue to smoke, the nicotine will weaken the walls of your blood vessels and hasten the fatty substances that block arteries, leading to hypertension. \n" +
                "Nicotine can have a detrimental impact on how well drugs work. Furthermore, inhaling secondhand smoke will have the same result. \n" +
                "It's time to stop smoking for the sake of your loved ones and your heart."))


    }

    if (title == Constansts.infoTitle11) {
        infoDetailList.add(InfoDetailModelClass("", ""))
        infoDetailList.add(InfoDetailModelClass("", ""))
        infoDetailList.add(InfoDetailModelClass("", ""))
        infoDetailList.add(InfoDetailModelClass("", ""))
        infoDetailList.add(InfoDetailModelClass("", ""))
        infoDetailList.add(InfoDetailModelClass("", ""))
        infoDetailList.add(InfoDetailModelClass("", ""))
    }

    if (title == Constansts.infoTitle12) {
        infoDetailList.add(
            InfoDetailModelClass("", ""))
    }


    if (title == Constansts.infoTitle13) {
        infoDetailList.add(
            InfoDetailModelClass("", ""))
    }


    if (title == Constansts.infoTitle14) {
        infoDetailList.add(
            InfoDetailModelClass("", ""))
    }


    if (title == Constansts.infoTitle15) {
        infoDetailList.add(
            InfoDetailModelClass("", ""))
    }

    if (title == Constansts.infoTitle16) {
        infoDetailList.add(
            InfoDetailModelClass("", ""))
    }


    if (title == Constansts.infoTitle17) {
        infoDetailList.add(
            InfoDetailModelClass("", ""))
    }



    if (title == Constansts.infoTitle18) {
        infoDetailList.add(
            InfoDetailModelClass("", ""))
    }



    if (title == Constansts.infoTitle19) {
        infoDetailList.add(
            InfoDetailModelClass("", ""))
    }



    if (title == Constansts.infoTitle20) {
        infoDetailList.add(
            InfoDetailModelClass("", ""))
    }


    if (title == Constansts.infoTitle21) {
        infoDetailList.add(
            InfoDetailModelClass("", ""))
    }



    if (title == Constansts.infoTitle22) {
        infoDetailList.add(
            InfoDetailModelClass("", ""))
    }

    if (title == Constansts.infoTitle23) {
        infoDetailList.add(
            InfoDetailModelClass("", ""))
    }

    if (title == Constansts.infoTitle24) {
        infoDetailList.add(
            InfoDetailModelClass("", ""))
    }

    if (title == Constansts.infoTitle25) {
        infoDetailList.add(
            InfoDetailModelClass("", ""))
    }
    if (title == Constansts.infoTitle26) {
        infoDetailList.add(
            InfoDetailModelClass("", ""))
    }
    if (title == Constansts.infoTitle27) {
        infoDetailList.add(
            InfoDetailModelClass("", ""))
    }


    return infoDetailList
}



