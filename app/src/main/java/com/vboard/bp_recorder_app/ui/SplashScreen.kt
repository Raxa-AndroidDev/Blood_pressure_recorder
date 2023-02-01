package com.vboard.bp_recorder_app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.ui.language_selection.LanguageSelectionActivity

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        Handler(Looper.getMainLooper()).postDelayed({
            val mainIntent = Intent(this, LanguageSelectionActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, 3000)
    }
}