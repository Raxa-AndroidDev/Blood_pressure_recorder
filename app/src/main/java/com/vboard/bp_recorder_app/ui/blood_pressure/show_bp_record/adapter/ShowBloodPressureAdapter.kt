package com.vboard.bp_recorder_app.ui.blood_pressure.show_bp_record.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.databinding.ItemShowRecordsLayoutBinding
import com.vboard.bp_recorder_app.utils.interfaces.ShowBPAdapterCallbacks
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ShowBloodPressureAdapter(var callBacks:ShowBPAdapterCallbacks) :RecyclerView.Adapter<ShowBloodPressureAdapter.MyViewHolder>() {

    var SECOND_MILLIS = 1000
    var MINUTE_MILLIS = 60 * SECOND_MILLIS
    var HOUR_MILLIS = 60 * MINUTE_MILLIS
    var DAY_MILLIS = 24 * HOUR_MILLIS

    var bloodPressureRecordList:ArrayList<BloodPressureTable> = ArrayList()
    set(value) {
        field = value
       notifyDataSetChanged()
    }




    class MyViewHolder(var binding: ItemShowRecordsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding:ItemShowRecordsLayoutBinding = ItemShowRecordsLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = bloodPressureRecordList[position]

        holder.binding.apply {
           tvTitle.text ="Blood Pressure (mmHg)"
          tvUnit.text = "(mmHg)"
           tvReadings.text = currentItem.systolic.toString() + "/" + currentItem.diaSystolic.toString()

            if (currentItem.systolic.toInt() in 0..90) {

              tvStatus.text = "Low"
               tvStatus.setTextColor(Color.parseColor("#E3B518"))
                tvStatus.background =
                    holder.itemView.context.getDrawable(R.drawable.status_bg_yellow)
            }
            if (currentItem.systolic.toInt() in 91..120) {
                tvStatus.text = "Normal"
                tvStatus.setTextColor(Color.parseColor("#2D9532"))
                tvStatus.background =
                    holder.itemView.context.getDrawable(R.drawable.status_bg_green)
            }
            if (currentItem.systolic.toInt() in 121..250) {
                tvStatus.text = "High"
                tvStatus.setTextColor(Color.parseColor("#FD1F35"))
                tvStatus.background =
                    holder.itemView.context.getDrawable(R.drawable.status_bg_red)
            }
        }




        holder.itemView.setOnClickListener {
            callBacks.OnItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return bloodPressureRecordList.size
    }







    fun getTimeAgo(givenDateString: String): String? {
        if (givenDateString.equals("", ignoreCase = true)) {
            return ""
        }
        var timeInMilliseconds: Long = 0
        val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
        try {
            val mDate = sdf.parse(givenDateString)
            timeInMilliseconds = mDate.time
            println("Date in milli :: $timeInMilliseconds")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val result = "now"
        val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
        val todayDate = formatter.format(Date())
        val calendar = Calendar.getInstance()
        val dayagolong = timeInMilliseconds
        calendar.timeInMillis = dayagolong
        val agoformater = formatter.format(calendar.time)
        var CurrentDate: Date? = null
        var CreateDate: Date? = null
        try {
            CurrentDate = formatter.parse(todayDate)
            CreateDate = formatter.parse(agoformater)
            var different = Math.abs(CurrentDate.time - CreateDate.time)
            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24
            val elapsedDays = different / daysInMilli
            different = different % daysInMilli
            val elapsedHours = different / hoursInMilli
            different = different % hoursInMilli
            val elapsedMinutes = different / minutesInMilli
            different = different % minutesInMilli
            val elapsedSeconds = different / secondsInMilli
            different = different % secondsInMilli
            if (elapsedDays == 0L) {
                if (elapsedHours == 0L) {
                    if (elapsedMinutes == 0L) {
                        if (elapsedSeconds < 0) {
                            return "0" + " s"
                        } else {
                            if (elapsedDays > 0 && elapsedSeconds < 59) {
                                return "now"
                            }
                        }
                    } else {
                        return elapsedMinutes.toString() + "mins ago"
                    }
                } else {
                    return elapsedHours.toString() + "hr ago"
                }
            } else {
                if (elapsedDays <= 29) {
                    return elapsedDays.toString() + "d ago"
                } else if (elapsedDays > 29 && elapsedDays <= 58) {
                    return "1Mth ago"
                }
                if (elapsedDays > 58 && elapsedDays <= 87) {
                    return "2Mth ago"
                }
                if (elapsedDays > 87 && elapsedDays <= 116) {
                    return "3Mth ago"
                }
                if (elapsedDays > 116 && elapsedDays <= 145) {
                    return "4Mth ago"
                }
                if (elapsedDays > 145 && elapsedDays <= 174) {
                    return "5Mth ago"
                }
                if (elapsedDays > 174 && elapsedDays <= 203) {
                    return "6Mth ago"
                }
                if (elapsedDays > 203 && elapsedDays <= 232) {
                    return "7Mth ago"
                }
                if (elapsedDays > 232 && elapsedDays <= 261) {
                    return "8Mth ago"
                }
                if (elapsedDays > 261 && elapsedDays <= 290) {
                    return "9Mth ago"
                }
                if (elapsedDays > 290 && elapsedDays <= 319) {
                    return "10Mth ago"
                }
                if (elapsedDays > 319 && elapsedDays <= 348) {
                    return "11Mth ago"
                }
                if (elapsedDays > 348 && elapsedDays <= 360) {
                    return "12Mth ago"
                }
                if (elapsedDays > 360 && elapsedDays <= 720) {
                    return "1 year ago"
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return result
    }
}