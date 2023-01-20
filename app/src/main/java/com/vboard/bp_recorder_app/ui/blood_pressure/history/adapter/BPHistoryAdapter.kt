package com.vboard.bp_recorder_app.ui.blood_pressure.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.databinding.ItemShowRecordsLayoutBinding
import com.vboard.bp_recorder_app.utils.Constansts
import com.vboard.bp_recorder_app.utils.getBPType
import com.vboard.bp_recorder_app.utils.interfaces.ShowBPAdapterCallbacks
import com.vboard.bp_recorder_app.utils.toDate
import com.vboard.bp_recorder_app.utils.tostring

class BPHistoryAdapter(var context: Context ,var callBacks: ShowBPAdapterCallbacks) :
    RecyclerView.Adapter<BPHistoryAdapter.MyViewHolder>() {

    var type:String = "history"
    var SECOND_MILLIS = 1000
    var MINUTE_MILLIS = 60 * SECOND_MILLIS
    var HOUR_MILLIS = 60 * MINUTE_MILLIS
    var DAY_MILLIS = 24 * HOUR_MILLIS

    var bptype:String = Constansts.bp_Normal

    var bloodPressureRecordList: ArrayList<BloodPressureTable> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    class MyViewHolder(var binding: ItemShowRecordsLayoutBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemShowRecordsLayoutBinding =
            ItemShowRecordsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = bloodPressureRecordList[position]

        holder.binding.apply {
            tvSystolicBp.text = currentItem.systolic.toString()
            tvDiastolicBp.text = currentItem.diaSystolic.toString()

            tvDatetime.text = "${currentItem.date.tostring()} ${currentItem.time}, ${currentItem.pulse} BPM"

           bptype =  getBPType(currentItem.systolic,currentItem.diaSystolic)
                tvStatus.text =bptype

            when (bptype) {
                Constansts.bp_hypotension -> {
                    holder.binding.circleImg.setColorFilter(ContextCompat.getColor(context,R.color.hypotension_bp_color))
                holder.binding.imgHeart.setColorFilter(ContextCompat.getColor(context,R.color.hypotension_bp_color))
                }
                Constansts.bp_Normal -> {
                    holder.binding.circleImg.setColorFilter(ContextCompat.getColor(context,R.color.normal_bp_color))
                    holder.binding.imgHeart.setColorFilter(ContextCompat.getColor(context,R.color.normal_bp_color))
                }
                Constansts.bp_Elevated -> {
                    holder.binding.circleImg.setColorFilter(ContextCompat.getColor(context,R.color.elevated_bp_color))
                    holder.binding.imgHeart.setColorFilter(ContextCompat.getColor(context,R.color.elevated_bp_color))
                }
                Constansts.bp_Hypertension1 -> {
                    holder.binding.circleImg.setColorFilter(ContextCompat.getColor(context,R.color.hyper_stage1_color))
                    holder.binding.imgHeart.setColorFilter(ContextCompat.getColor(context,R.color.hyper_stage1_color))
                }
            }



        }

        holder.binding.editBpRecord.setOnClickListener {
            callBacks.OnEditIconClick(currentItem)
        }




        holder.itemView.setOnClickListener {
            callBacks.OnItemClick(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return  bloodPressureRecordList.size

      /*  if (type == "history"){
            3
        } else {
            bloodPressureRecordList.size
        }*/




    }


}