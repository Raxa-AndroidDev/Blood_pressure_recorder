package com.vboard.bp_recorder_app.ui.weight.add_record.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.ItemBptypeColorsLayoutBinding
import com.vboard.bp_recorder_app.ui.blood_pressure.model_classes.BPTypesModelClass
import com.vboard.bp_recorder_app.ui.weight.WeightTypesModelClass


class WeightTypeColorsAdapter(
    var context: Context,
    var width: Int,
    var list: ArrayList<WeightTypesModelClass>
) :
    RecyclerView.Adapter<WeightTypeColorsAdapter.WeightColorTypeViewHolder>() {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightColorTypeViewHolder {
        var binding = ItemBptypeColorsLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WeightColorTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeightColorTypeViewHolder, position: Int) {


        holder.binding.imgBpTypeColor.setColorFilter(
            ContextCompat.getColor(
                context,
                list[position].color
            )
        )


        if (list[position].isSelected) {
            holder.binding.bpTypeColorLayout.setBackgroundResource(R.drawable.bp_type_colored_bg)
            holder.binding.imgColorIndicator.visibility = View.VISIBLE
        } else {
            holder.binding.bpTypeColorLayout.background =
                ContextCompat.getDrawable(context, R.drawable.bp_type_white_bg)
            holder.binding.imgColorIndicator.visibility = View.INVISIBLE
        }

    }

    override fun getItemCount(): Int = list.size

    inner class WeightColorTypeViewHolder(var binding: ItemBptypeColorsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


}