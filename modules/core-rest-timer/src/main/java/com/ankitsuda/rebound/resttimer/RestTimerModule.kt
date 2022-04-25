/*
 * Copyright (c) 2022 Ankit Suda.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.ankitsuda.rebound.resttimer

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import com.ankitsuda.rebound.coreRestTimer.R
import com.ankitsuda.rebound.resttimer.Constants.ACTION_CANCEL
import com.ankitsuda.rebound.resttimer.Constants.ACTION_PAUSE
import com.ankitsuda.rebound.resttimer.Constants.ACTION_RESUME
import com.ankitsuda.rebound.resttimer.Constants.ACTION_SHOW_MAIN_ACTIVITY
import com.ankitsuda.rebound.resttimer.Constants.NOTIFICATION_CHANNEL_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object RestTimerModule {

    @ClickPendingIntent
    @ServiceScoped
    @Provides
    fun provideClickPendingIntent(
        @ApplicationContext app: Context
    ): PendingIntent = PendingIntent.getActivity(
        app,
        0,
        app.packageManager.getLaunchIntentForPackage(app.packageName),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    )

    @CancelActionPendingIntent
    @ServiceScoped
    @Provides
    fun provideCancelActionPendingIntent(
        @ApplicationContext app: Context
    ): PendingIntent = PendingIntent.getService(
        app,
        1,
        Intent(app, RestTimerService::class.java).also {
            it.action = ACTION_CANCEL
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    @ResumeActionPendingIntent
    @ServiceScoped
    @Provides
    fun provideResumeActionPendingIntent(
        @ApplicationContext app: Context
    ): PendingIntent = PendingIntent.getService(
        app,
        2,
        Intent(app, RestTimerService::class.java).also {
            it.action = ACTION_RESUME
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    @PauseActionPendingIntent
    @ServiceScoped
    @Provides
    fun providePauseActionPendingIntent(
        @ApplicationContext app: Context
    ): PendingIntent = PendingIntent.getService(
        app,
        3,
        Intent(app, RestTimerService::class.java).also {
            it.action = ACTION_PAUSE
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    @ServiceScoped
    @Provides
    fun provideBaseNotificationBuilder(
        @ApplicationContext app: Context,
        @ClickPendingIntent pendingIntent: PendingIntent
    ): NotificationCompat.Builder = NotificationCompat.Builder(app, NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.ic_alarm)
        .setContentTitle("INTime")
        .setContentText("00:00:00")
        .setContentIntent(pendingIntent)

    @ServiceScoped
    @Provides
    fun provideVibrator(
        @ApplicationContext app: Context
    ): Vibrator = app.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext app: Context
    ): NotificationManager = app.getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager

}