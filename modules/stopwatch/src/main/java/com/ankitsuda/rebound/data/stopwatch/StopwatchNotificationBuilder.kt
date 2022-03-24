/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ankitsuda.rebound.data.stopwatch
import android.app.Notification
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.SystemClock
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Action
import androidx.core.app.NotificationCompat.Builder
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.ankitsuda.rebound.stopwatchData.R


/**
 * Builds notification to reflect the latest state of the stopwatch and recorded laps.
 */
internal class StopwatchNotificationBuilder {
    fun buildChannel(context: Context, notificationManager: NotificationManagerCompat) {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                    STOPWATCH_NOTIFICATION_CHANNEL_ID,
//                    context.getString(R.string.default_label),
//                    NotificationManagerCompat.IMPORTANCE_DEFAULT)
//            notificationManager.createNotificationChannel(channel)
//        }
    }

//    fun build(context: Context, nm: NotificationModel, stopwatch: Stopwatch?): Notification {
//        @StringRes val eventLabel: Int = R.string.label_notification
//
//        // Intent to load the app when the notification is tapped.
//        val showApp: Intent = Intent(context, StopwatchService::class.java)
//                .setAction(StopwatchService.ACTION_SHOW_STOPWATCH)
//                .putExtra(Events.EXTRA_EVENT_LABEL, eventLabel)
//
//        val pendingShowApp: PendingIntent = PendingIntent.getService(context, 0, showApp,
//                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_UPDATE_CURRENT)
//
//        // Compute some values required below.
//        val running = stopwatch!!.isRunning
//        val pname: String = context.getPackageName()
//        val res: Resources = context.getResources()
//        val base: Long = SystemClock.elapsedRealtime() - stopwatch.totalTime
//
//        val content = RemoteViews(pname, R.layout.chronometer_notif_content)
//        content.setChronometer(R.id.chronometer, base, null, running)
//
//        val actions: MutableList<Action> = ArrayList<Action>(2)
//
//        if (running) {
//            // Left button: Pause
//            val pause: Intent = Intent(context, StopwatchService::class.java)
//                    .setAction(StopwatchService.ACTION_PAUSE_STOPWATCH)
//                    .putExtra(Events.EXTRA_EVENT_LABEL, eventLabel)
//
//            @DrawableRes val icon1: Int = R.drawable.ic_pause_24dp
//            val title1: CharSequence = res.getText(R.string.sw_pause_button)
//            val intent1: PendingIntent = Utils.pendingServiceIntent(context, pause)
//            actions.add(Action.Builder(icon1, title1, intent1).build())
//
//            // Right button: Add Lap
//            if (DataModel.dataModel.canAddMoreLaps()) {
//                val lap: Intent = Intent(context, StopwatchService::class.java)
//                        .setAction(StopwatchService.ACTION_LAP_STOPWATCH)
//                        .putExtra(Events.EXTRA_EVENT_LABEL, eventLabel)
//
//                @DrawableRes val icon2: Int = R.drawable.ic_sw_lap_24dp
//                val title2: CharSequence = res.getText(R.string.sw_lap_button)
//                val intent2: PendingIntent = Utils.pendingServiceIntent(context, lap)
//                actions.add(Action.Builder(icon2, title2, intent2).build())
//            }
//
//            // Show the current lap number if any laps have been recorded.
//            val lapCount = DataModel.dataModel.laps.size
//            if (lapCount > 0) {
//                val lapNumber = lapCount + 1
//                val lap: String = res.getString(R.string.sw_notification_lap_number, lapNumber)
//                content.setTextViewText(R.id.state, lap)
//                content.setViewVisibility(R.id.state, VISIBLE)
//            } else {
//                content.setViewVisibility(R.id.state, GONE)
//            }
//        } else {
//            // Left button: Start
//            val start: Intent = Intent(context, StopwatchService::class.java)
//                    .setAction(StopwatchService.ACTION_START_STOPWATCH)
//                    .putExtra(Events.EXTRA_EVENT_LABEL, eventLabel)
//
//            @DrawableRes val icon1: Int = R.drawable.ic_start_24dp
//            val title1: CharSequence = res.getText(R.string.sw_start_button)
//            val intent1: PendingIntent = Utils.pendingServiceIntent(context, start)
//            actions.add(Action.Builder(icon1, title1, intent1).build())
//
//            // Right button: Reset (dismisses notification and resets stopwatch)
//            val reset: Intent = Intent(context, StopwatchService::class.java)
//                    .setAction(StopwatchService.ACTION_RESET_STOPWATCH)
//                    .putExtra(Events.EXTRA_EVENT_LABEL, eventLabel)
//
//            @DrawableRes val icon2: Int = R.drawable.ic_reset_24dp
//            val title2: CharSequence = res.getText(R.string.sw_reset_button)
//            val intent2: PendingIntent = Utils.pendingServiceIntent(context, reset)
//            actions.add(Action.Builder(icon2, title2, intent2).build())
//
//            // Indicate the stopwatch is paused.
//            content.setTextViewText(R.id.state, res.getString(R.string.swn_paused))
//            content.setViewVisibility(R.id.state, VISIBLE)
//        }
//        val notification: Builder = Builder(
//                context, STOPWATCH_NOTIFICATION_CHANNEL_ID)
//                .setLocalOnly(true)
//                .setOngoing(running)
//                .setCustomContentView(content)
//                .setContentIntent(pendingShowApp)
//                .setAutoCancel(stopwatch.isPaused)
//                .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
//                .setSmallIcon(R.drawable.stat_notify_stopwatch)
//                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
//                .setColor(ContextCompat.getColor(context, R.color.default_background))
//
//        if (Utils.isNOrLater) {
//            notification.setGroup(nm.stopwatchNotificationGroupKey)
//        }
//
//        for (action in actions) {
//            notification.addAction(action)
//        }
//
//        return notification.build()
//    }

    companion object {
        /**
         * Notification channel containing all stopwatch notifications.
         */
        private const val STOPWATCH_NOTIFICATION_CHANNEL_ID = "StopwatchNotification"
    }
}