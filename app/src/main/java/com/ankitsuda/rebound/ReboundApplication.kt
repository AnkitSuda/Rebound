package com.ankitsuda.rebound

import android.app.Application
import com.ankitsuda.rebound.data.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ReboundApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }


    fun getDb() = AppDatabase.getDatabase(this)
}