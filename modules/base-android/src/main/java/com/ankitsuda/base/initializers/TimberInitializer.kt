package com.ankitsuda.base.initializers

import android.app.Application
import com.ankitsuda.baseAndroid.BuildConfig
import javax.inject.Inject
import timber.log.Timber

class TimberInitializer @Inject constructor() : AppInitializer {
    override fun init(application: Application) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
