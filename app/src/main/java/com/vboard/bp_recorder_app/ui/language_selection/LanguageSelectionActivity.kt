package com.vboard.bp_recorder_app.ui.language_selection

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.databinding.ActivityLanguageSelectionBinding
import com.vboard.bp_recorder_app.ui.MainActivity
import com.vboard.bp_recorder_app.utils.getLangsList

class LanguageSelectionActivity : AppCompatActivity() {
    lateinit var binding: ActivityLanguageSelectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_language_selection)



        initialization()
    }

    private fun initialization() {
        initRecView()
        binding.tvSkipLangSelection.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun initRecView() {
        val langsList = getLangsList(this)
        with(binding) {
            languagesRcv.layoutManager = LinearLayoutManager(this@LanguageSelectionActivity)
            languagesRcv.adapter =
                LanguageSelectionAdapter(this@LanguageSelectionActivity, langsList)

        }

    }
}