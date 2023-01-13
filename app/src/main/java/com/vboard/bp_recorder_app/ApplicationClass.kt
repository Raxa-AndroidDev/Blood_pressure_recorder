package com.vboard.bp_recorder_app

import android.app.Application
import com.google.android.gms.ads.MobileAds

class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
    }
}