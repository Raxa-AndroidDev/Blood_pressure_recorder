package com.vboard.bp_recorder_app.ui.language_selection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.ActivityLanguageSelectionBinding
import com.vboard.bp_recorder_app.databinding.ActivityMainBinding
import com.vboard.bp_recorder_app.ui.language_selection.LanguageSelectionAdapter
import com.vboard.bp_recorder_app.utils.getLangsList

class LanguageSelectionActivity : AppCompatActivity() {
    lateinit var binding: ActivityLanguageSelectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (binding == null) {
            binding = ActivityLanguageSelectionBinding.inflate(layoutInflater)
        }
        setContentView(binding.root)


        initialization()
    }

    private fun initialization() {
       initRecView()
    }

    private fun initRecView() {
val langsList = getLangsList(this)
        with(binding) {
            languagesRcv.layoutManager = LinearLayoutManager(this@LanguageSelectionActivity)
            languagesRcv.adapter= LanguageSelectionAdapter(langsList)

        }

    }
}