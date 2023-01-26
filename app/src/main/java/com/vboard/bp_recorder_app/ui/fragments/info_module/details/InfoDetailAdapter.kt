package com.vboard.bp_recorder_app.ui.fragments.info_module.details

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.ItemInfoDetailLayoutBinding

class InfoDetailAdapter(var context:Context , var detail_list:ArrayList<InfoDetailModelClass>):RecyclerView.Adapter<InfoDetailAdapter.InfoDetailViewHolder>() {

    inner class InfoDetailViewHolder(var binding:ItemInfoDetailLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoDetailViewHolder {
        val binding = ItemInfoDetailLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return InfoDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoDetailViewHolder, position: Int) {




        // Setting the TextView typeface
        holder.binding.tvInfoDetailTitle.typeface =  ResourcesCompat.getFont(context,R.font.poppins_semibold)
        holder.binding.tvInfoDetailTitle.text = detail_list[position].title
        holder.binding.tvInfoDetailDesc.text = detail_list[position].description
    }

    override fun getItemCount(): Int {
        return detail_list.size
    }
}