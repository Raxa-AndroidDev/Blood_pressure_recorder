package com.vboard.bp_recorder_app

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vboard.bp_recorder_app.databinding.CustomChipBinding
import com.vboard.bp_recorder_app.utils.interfaces.ChipListCallBacks

class ChipAdapter(
    var context: Context,
    private val chipsList: ArrayList<String>,
    private var chipListCallBacks: ChipListCallBacks
) :
    RecyclerView.Adapter<ChipAdapter.ChipViewHolder>() {


    inner class ChipViewHolder(var binding: com.vboard.bp_recorder_app.databinding.CustomChipBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val binding = CustomChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {

        holder.binding.chipText.text = chipsList[position]


        if (position == 0){
            holder.binding.chipText.text = "Add"
            holder.binding.flexLayout.background = (ContextCompat.getDrawable(context, R.drawable.add_chip_stroke))
        }

        holder.binding.flexLayout.setOnClickListener {

            if (position == 0) {
                Log.e("TAG", "onBindViewHolder: $position",)
                chipListCallBacks.onChipSelected(chipsList[position], false, position)
            } else{


                if (it.isSelected) {
                    it.isSelected = false
                    it.background = (ContextCompat.getDrawable(context, R.drawable.chip_bg))
                    chipListCallBacks.onChipSelected(chipsList[position], true, position)
                } else {
                    it.isSelected = true
                    it.background =
                        (ContextCompat.getDrawable(context, R.drawable.chip_selected_bg))
                    chipListCallBacks.onChipSelected(chipsList[position], false, position)
                }
        }

        }


    }

    override fun getItemCount(): Int = chipsList.size
}