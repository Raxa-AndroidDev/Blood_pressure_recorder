package com.vboard.bp_recorder_app.ui.fragments.weight.add_record.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.ItemBptypeColorsLayoutBinding
import com.vboard.bp_recorder_app.ui.fragments.weight.WeightTypesModelClass


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

        holder.binding.parentLayout.layoutParams.width = width/9
        holder.binding.parentLayout.layoutParams.height = LayoutParams.WRAP_CONTENT

       holder.binding.imgBpTypeColor.setColorFilter(
            ContextCompat.getColor(
                context,
                list[position].color
            )
        )


        if (list[position].isSelected) {

         //   holder.binding.bpTypeColorLayout.backgroundTintList = ContextCompat.getColorStateList(context, R.color.tab_selected_color)
            holder.binding.imgColorIndicator.visibility = View.VISIBLE
        } else {

           /* holder.binding.bpTypeColorLayout.backgroundTintList =
                ContextCompat.getColorStateList(
                    context,
                    list[position].color
                )*/

           // holder.binding.bpTypeColorLayout.backgroundTintList = null


            holder.binding.imgColorIndicator.visibility = View.INVISIBLE
        }

    }

    override fun getItemCount(): Int = list.size

    inner class WeightColorTypeViewHolder(var binding: ItemBptypeColorsLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


}