package com.vboard.bp_recorder_app.ui.fragments.info_module.titles

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.ItemInfoTitlesLayoutBinding
import com.vboard.bp_recorder_app.ui.fragments.info_module.details.InfoDetailsCallBack

class InfoTitlesAdapter(
    var context: Context,
    var infoDetailsCallBack: InfoDetailsCallBack,
    var infoTitlesList: ArrayList<InfoTitlesModelClass>
) : RecyclerView.Adapter<InfoTitlesAdapter.InfoTitlesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoTitlesViewHolder {

        val binding =
            ItemInfoTitlesLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InfoTitlesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InfoTitlesViewHolder, position: Int) {

        val listItem = infoTitlesList[position]

        if (listItem.bgColor == R.color.info_yellow_color) {
            holder.binding.tvInfoTitle.setTextColor(ContextCompat.getColor(context, R.color.black))
            holder.binding.imgNextscreenArrow.imageTintList =
                ContextCompat.getColorStateList(context, R.color.black)
        } else {
            holder.binding.imgNextscreenArrow.imageTintList =
                ContextCompat.getColorStateList(context, R.color.white)

            holder.binding.tvInfoTitle.setTextColor(ContextCompat.getColor(context, R.color.white))
        }

        holder.binding.imgInfoTitle.setImageResource(listItem.img)
        holder.binding.tvInfoTitle.text = listItem.title
        holder.binding.infoCardBg.backgroundTintList =
            ContextCompat.getColorStateList(context, listItem.bgColor)


        holder.itemView.setOnClickListener {
            infoDetailsCallBack.OnDetailClick(
                listItem.img,
                listItem.bgColor.toString(),
                listItem.title
            )
        }


    }

    override fun getItemCount(): Int {
        return infoTitlesList.size
    }


    inner class InfoTitlesViewHolder(var binding: ItemInfoTitlesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}