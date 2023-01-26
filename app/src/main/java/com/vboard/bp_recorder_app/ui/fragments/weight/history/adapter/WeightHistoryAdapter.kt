package com.vboard.bp_recorder_app.ui.fragments.weight.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.db_tables.BodyWeightTable
import com.vboard.bp_recorder_app.databinding.ItemShowRecordsLayoutBinding
import com.vboard.bp_recorder_app.ui.fragments.heart_rate.history.adapter.HRHistoryAdapter
import com.vboard.bp_recorder_app.utils.*
import com.vboard.bp_recorder_app.utils.interfaces.ShowWeightAdapterCallbacks

class WeightHistoryAdapter(var context: Context, var callBacks: ShowWeightAdapterCallbacks) :
    RecyclerView.Adapter<WeightHistoryAdapter.MyViewHolder>() {

    var type: String = "history"


    var weighttype: String = Constansts.normalWeight

    var weightRecordList: ArrayList<BodyWeightTable> = ArrayList()
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
        val currentItem = weightRecordList[position]

        holder.binding.apply {
            tvSystolicBp.text = currentItem.weight.toString()
            tvDiastolicBp.text = currentItem.height.toString()

            tvDatetime.text = "${ currentItem.date.tostring()},${currentItem.time}"

            weighttype = getWeightType(calculateBMI(currentItem.weight,currentItem.height))
            tvStatus.text = weighttype

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