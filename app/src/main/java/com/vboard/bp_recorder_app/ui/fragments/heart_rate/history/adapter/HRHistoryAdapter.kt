package com.vboard.bp_recorder_app.ui.fragments.heart_rate.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.vboard.bp_recorder_app.data.database.db_tables.HeartRateTable
import com.vboard.bp_recorder_app.databinding.ItemShowRecordsLayoutBinding
import com.vboard.bp_recorder_app.utils.*
import com.vboard.bp_recorder_app.utils.interfaces.ShowHRAdapterCallbacks

class HRHistoryAdapter(var context: Context, var callBacks: ShowHRAdapterCallbacks) :
    RecyclerView.Adapter<HRHistoryAdapter.MyViewHolder>() {

    var type: String = "history"


    var weighttype: String = Constansts.normalWeight

    var heartrateList: ArrayList<HeartRateTable> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    class MyViewHolder(var binding: ItemShowRecordsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemShowRecordsLayoutBinding =
            ItemShowRecordsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = heartrateList[position]

        holder.binding.apply {
            tvSystolicBp.text = currentItem.BPM
            view1.visibility = View.GONE
            tvDiastolicBp.text = "BPM"

            tvDatetime.text = "${ currentItem.date},${currentItem.time}"


           /* tvStatus.text = weighttype

            when (weighttype) {
                Constansts.verySeverlyUnderWeight -> {
                    holder.binding.circleImg.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.very_severly_uw
                        )
                    )
                    holder.binding.imgHeart.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.very_severly_uw
                        )
                    )
                }

                Constansts.severlyUnderWeight -> {
                    holder.binding.circleImg.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.severly_uw
                        )
                    )
                    holder.binding.imgHeart.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.severly_uw
                        )
                    )
                }

                Constansts.underWeight -> {
                    holder.binding.circleImg.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.underweight
                        )
                    )
                    holder.binding.imgHeart.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.underweight
                        )
                    )
                }
                Constansts.normalWeight -> {
                    holder.binding.circleImg.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.normalweight
                        )
                    )
                    holder.binding.imgHeart.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.normalweight
                        )
                    )
                }

                Constansts.overWeight -> {
                    holder.binding.circleImg.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.overweight
                        )
                    )
                    holder.binding.imgHeart.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.overweight
                        )
                    )
                }

                Constansts.obeseClass1 -> {
                    holder.binding.circleImg.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.obesestage1
                        )
                    )
                    holder.binding.imgHeart.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.obesestage1
                        )
                    )
                }

                Constansts.obeseClass2 -> {
                    holder.binding.circleImg.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.obesestage2
                        )
                    )
                    holder.binding.imgHeart.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.obesestage2
                        )
                    )
                }
                Constansts.obeseClass3 -> {
                    holder.binding.circleImg.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.obesestage3
                        )
                    )
                    holder.binding.imgHeart.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.obesestage3
                        )
                    )
                }

            }*/


        }

        holder.binding.editBpRecord.setOnClickListener {
            callBacks.OnEditIconClick(currentItem)
        }




        holder.itemView.setOnClickListener {
            callBacks.OnItemClick(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return heartrateList.size




    }


}