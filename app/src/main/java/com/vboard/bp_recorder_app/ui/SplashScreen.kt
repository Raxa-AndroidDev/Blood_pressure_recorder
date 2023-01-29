package com.vboard.bp_recorder_app.ui

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.utils.setLocale
import com.vboard.bp_recorder_app.utils.showToast


@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        Handler(Looper.myLooper()!!).postDelayed({
            try {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } catch (exception: ActivityNotFoundException) {
                this.showToast(getString( R.string.no_activity_found_exception))
            }


        }, 3000)
    }
}