package com.ankitsuda.rebound

import android.app.Application
import com.ankitsuda.base.BaseApp
import com.ankitsuda.base.initializers.AppInitializers
import com.ankitsuda.rebound.data.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ReboundApplication: BaseApp() {

    @Inject
    lateinit var initializers: AppInitializers

    override fun onCreate() {
        super.onCreate()
        initializers.init(this)
    }

}