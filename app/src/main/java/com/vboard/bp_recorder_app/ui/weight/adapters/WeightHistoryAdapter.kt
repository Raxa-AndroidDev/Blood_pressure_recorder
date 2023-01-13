package com.vboard.bp_recorder_app.ui.weight.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vboard.bp_recorder_app.data.database.db_tables.BloodPressureTable
import com.vboard.bp_recorder_app.data.database.db_tables.BodyWeightTable
import com.vboard.bp_recorder_app.databinding.ItemShowRecordsLayoutBinding
import com.vboard.bp_recorder_app.ui.weight.WeightAdapterCallbacks
import com.vboard.bp_recorder_app.utils.interfaces.ShowBPAdapterCallbacks

class WeightHistoryAdapter(var callBacks: WeightAdapterCallbacks) :
    RecyclerView.Adapter<WeightHistoryAdapter.MyViewHolder>() {

    var SECOND_MILLIS = 1000
    var MINUTE_MILLIS = 60 * SECOND_MILLIS
    var HOUR_MILLIS = 60 * MINUTE_MILLIS
    var DAY_MILLIS = 24 * HOUR_MILLIS

    var weightRecordList: ArrayList<BodyWeightTable> = ArrayList()
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
        val currentItem = weightRecordList[position]

        holder.binding.apply {
            tvSystolicBp.text = currentItem.weight.toString()
            tvDiastolicBp.visibility = View.INVISIBLE
            view1.visibility = View.INVISIBLE


            tvDatetime.text = currentItem.DateAndTime

            if (currentItem.weight in 0..90) {
                tvStatus.text = "Low"
              
            }
            if (currentItem.weight in 91..120) {
                tvStatus.text = "Normal"

            }
            if (currentItem.weight in 121..250) {
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
        return weightRecordList.size
    }


}