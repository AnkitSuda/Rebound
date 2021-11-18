package com.ankitsuda.rebound

import com.ankitsuda.base.BaseApp
import com.ankitsuda.base.initializers.AppInitializers
import dagger.hilt.android.HiltAndroidApp
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