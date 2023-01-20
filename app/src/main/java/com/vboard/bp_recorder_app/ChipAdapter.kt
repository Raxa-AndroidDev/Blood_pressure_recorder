package com.vboard.bp_recorder_app

import android.content.Context
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


    inner class ChipViewHolder(var binding: CustomChipBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val binding = CustomChipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {

        holder.binding.chipText.text = chipsList[position]

        holder.binding.flexLayout.setOnClickListener {
                 it.background = (ContextCompat.getDrawable(context, R.drawable.chip_selected_bg))
            chipListCallBacks.onChipSelected(chipsList[position])
        }


    }

    override fun getItemCount(): Int = chipsList.size
}