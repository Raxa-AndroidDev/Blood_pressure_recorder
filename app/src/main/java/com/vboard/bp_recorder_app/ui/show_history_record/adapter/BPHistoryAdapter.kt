package com.vboard.bp_recorder_app.ui.show_history_record.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.data.database.db_tables.BodyWeightTable
import com.vboard.bp_recorder_app.databinding.ItemShowRecordsLayoutBinding
import com.vboard.bp_recorder_app.utils.interfaces.ShowBPAdapterCallbacks

class BPHistoryAdapter(var callBacks: ShowBPAdapterCallbacks) :
    RecyclerView.Adapter<BPHistoryAdapter.MyViewHolder>() {

    var SECOND_MILLIS = 1000
    var MINUTE_MILLIS = 60 * SECOND_MILLIS
    var HOUR_MILLIS = 60 * MINUTE_MILLIS
    var DAY_MILLIS = 24 * HOUR_MILLIS

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

            tvDatetime.text = currentItem.DateAndTime

            if (currentItem.systolic in 0..90) {
                tvStatus.text = "Low"
              
            }
            if (currentItem.systolic in 91..120) {
                tvStatus.text = "Normal"

            }
            if (currentItem.systolic in 121..250) {
                tvStatus.text = "High"

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
        return bloodPressureRecordList.size
    }


}