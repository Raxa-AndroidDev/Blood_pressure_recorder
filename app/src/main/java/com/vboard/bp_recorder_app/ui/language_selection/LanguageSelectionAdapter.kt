package com.vboard.bp_recorder_app.ui.language_selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vboard.bp_recorder_app.databinding.ItemLangSelectionBinding

class LanguageSelectionAdapter(var langsList:ArrayList<LangSelectionModelClass>):RecyclerView.Adapter<LanguageSelectionAdapter.LangSelectionViewHolder>(){


    inner class LangSelectionViewHolder(var binding:ItemLangSelectionBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LangSelectionViewHolder {
        var binding = ItemLangSelectionBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LangSelectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LangSelectionViewHolder, position: Int) {
        holder.binding.tvLangName.text =   "${langsList[position].lang_name} (${langsList[position].lang_locale})"
    }

    override fun getItemCount(): Int {
       return langsList.size
    }
}