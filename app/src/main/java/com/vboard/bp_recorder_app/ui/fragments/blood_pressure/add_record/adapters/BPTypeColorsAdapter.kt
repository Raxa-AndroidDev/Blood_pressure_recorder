package com.vboard.bp_recorder_app.ui.fragments.blood_pressure.add_record.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.ItemBptypeColorsLayoutBinding
import com.vboard.bp_recorder_app.ui.fragments.blood_pressure.model_classes.BPTypesModelClass


class BPTypeColorsAdapter(
    var context: Context,
    var width: Int,
    var list: ArrayList<BPTypesModelClass>
) :
    RecyclerView.Adapter<BPTypeColorsAdapter.BPColorTypeViewHolder>() {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BPColorTypeViewHolder {
        var binding = ItemBptypeColorsLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BPColorTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BPColorTypeViewHolder, position: Int) {


        holder.binding.imgBpTypeColor.setColorFilter(
            ContextCompat.getColor(
                context,
                list[position].color
            )
        )


        if (list[position].isSelected) {

            holder.binding.bpTypeColorLayout.backgroundTintList = ContextCompat.getColorStateList(context, R.color.tab_selected_color)
            holder.binding.imgColorIndicator.visibility = View.VISIBLE
        } else {

            holder.binding.bpTypeColorLayout.backgroundTintList =
                ContextCompat.getColorStateList(
                    context,
                    list[position].color
                )

            // holder.binding.bpTypeColorLayout.backgroundTintList = null


            holder.binding.imgColorIndicator.visibility = View.INVISIBLE
        }

    }

    override fun getItemCount(): Int = list.size

    inner class BPColorTypeViewHolder(var binding: ItemBptypeColorsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


}