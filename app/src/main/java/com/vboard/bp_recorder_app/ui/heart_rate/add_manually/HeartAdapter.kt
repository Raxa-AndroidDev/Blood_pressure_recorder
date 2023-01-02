package com.example.bloodpressureapp.fragments.heartrate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vboard.bp_recorder_app.data.database.db_tables.HeartRateTable
import com.vboard.bp_recorder_app.databinding.ItemShowRecordsLayoutBinding

class HeartAdapter(private var itemClick: ItemClick) : RecyclerView.Adapter<HeartAdapter.MyViewHolder>() {

    private var heartdataList = emptyList<HeartRateTable>()

    class MyViewHolder(val binding: ItemShowRecordsLayoutBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding =
            ItemShowRecordsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = heartdataList[position]

        holder.binding.tvSystolicBp.text = currentItem.BPM
        holder.binding.tvDiastolicBp.visibility = View.INVISIBLE
        holder.binding.tvDatetime.text = currentItem.DateAndTime

        holder.itemView.setOnClickListener {
            itemClick.onClick(currentItem)
        }
        val bpm = currentItem.BPM.toInt()

        if (bpm <= 59) {
            holder.binding.tvStatus.text = "Low"

        }
        if (bpm in 60..100) {
            holder.binding.tvStatus.text = "Normal"

        }
        if (bpm in 110..130) {
            holder.binding.tvStatus.text = "Seek Advice from your GP"

        }
        if (bpm >= 131) {
            holder.binding.tvStatus.text = "High"

        }


    }

    override fun getItemCount(): Int {
        return heartdataList.size
    }

    fun setData(heartList: List<HeartRateTable>) {
        this.heartdataList = heartList
        notifyDataSetChanged()
    }

    interface ItemClick {
        fun onClick(item: HeartRateTable) {

        }

    }

}