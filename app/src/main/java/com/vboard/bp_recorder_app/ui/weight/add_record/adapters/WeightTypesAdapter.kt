package com.vboard.bp_recorder_app.ui.weight.add_record.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vboard.bp_recorder_app.databinding.ItemBpTypesListBinding
import com.vboard.bp_recorder_app.ui.weight.WeightTypesModelClass

class WeightTypesAdapter(var context: Context, var list:ArrayList<WeightTypesModelClass>):RecyclerView.Adapter<WeightTypesAdapter.BPTypesViewHolder>() {

    inner class BPTypesViewHolder(var binding:ItemBpTypesListBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BPTypesViewHolder {
     val binding = ItemBpTypesListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  BPTypesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BPTypesViewHolder, position: Int) {

        holder.binding.imgHeart.setColorFilter(ContextCompat.getColor(context,list[position].color))
        holder.binding.tvBptype.text = list[position].weightType
        holder.binding.tvBpRange.text = list[position].typeRange
    }

    override fun getItemCount(): Int  = list.size


}