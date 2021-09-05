package com.ankitsuda.rebound

import android.app.Application
import timber.log.Timber

class ReboundApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}