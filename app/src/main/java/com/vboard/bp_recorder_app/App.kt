package com.vboard.bp_recorder_app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {


    companion object {
        const val CHANNEL_ID = "ALARM_SERVICE_CHANNEL"
    }


    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate() {
        super.onCreate()
        createNotificationChannnel()
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val dn = sharedPreferences.getBoolean(getString(R.string.dayNightTheme), true)
        if (dn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

    }



    private fun createNotificationChannnel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.app_name) + "Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }


}