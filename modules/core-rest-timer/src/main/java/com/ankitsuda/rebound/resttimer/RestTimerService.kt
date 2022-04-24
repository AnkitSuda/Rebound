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

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.*
import android.speech.tts.TextToSpeech
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ankitsuda.rebound.coreRestTimer.R
import com.ankitsuda.rebound.resttimer.Constants.ACTION_CANCEL
import com.ankitsuda.rebound.resttimer.Constants.ACTION_CANCEL_AND_RESET
import com.ankitsuda.rebound.resttimer.Constants.ACTION_INITIALIZE_DATA
import com.ankitsuda.rebound.resttimer.Constants.ACTION_MUTE
import com.ankitsuda.rebound.resttimer.Constants.ACTION_PAUSE
import com.ankitsuda.rebound.resttimer.Constants.ACTION_RESUME
import com.ankitsuda.rebound.resttimer.Constants.ACTION_SOUND
import com.ankitsuda.rebound.resttimer.Constants.ACTION_START
import com.ankitsuda.rebound.resttimer.Constants.ACTION_VIBRATE
import com.ankitsuda.rebound.resttimer.Constants.EXTRA_TOTAL_TIME
import com.ankitsuda.rebound.resttimer.Constants.NOTIFICATION_CHANNEL_ID
import com.ankitsuda.rebound.resttimer.Constants.NOTIFICATION_CHANNEL_NAME
import com.ankitsuda.rebound.resttimer.Constants.TIMER_STARTING_IN_TIME
import com.ankitsuda.rebound.resttimer.Constants.TIMER_UPDATE_INTERVAL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class RestTimerService : LifecycleService(), TextToSpeech.OnInitListener {

    // notification builder
    @Inject
    lateinit var baseNotificationBuilder: NotificationCompat.Builder
    lateinit var currentNotificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    // pending intents for notification action-handling
    @ResumeActionPendingIntent
    @Inject
    lateinit var resumeActionPendingIntent: PendingIntent

    @PauseActionPendingIntent
    @Inject
    lateinit var pauseActionPendingIntent: PendingIntent

    @CancelActionPendingIntent
    @Inject
    lateinit var cancelActionPendingIntent: PendingIntent

    // service state
    private var isInitialized = false
    private var isKilled = true
    private var isBound = false

    // timer
    private var timer: CountDownTimer? = null
    private var millisToCompletion = 0L
    private var lastSecondTimestamp = 0L
    private var timerIndex = 0
    private var timerMaxRepetitions = 0

    // audio/tts
    @Inject
    lateinit var vibrator: Vibrator
    private var tts: TextToSpeech? = null

    // utility
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)
    private var wakeLock: PowerManager.WakeLock? = null

    companion object {
        // holds MutableLiveData for UI to observe
        val currentTimerState = MutableLiveData<TimerState>()
        val elapsedTimeInMillis = MutableLiveData<Long>()
        val elapsedTimeInMillisEverySecond = MutableLiveData<Long>()
        val totalTimeInMillis = MutableLiveData<Long>()
    }


    override fun onCreate() {
        super.onCreate()
        Timber.i("onCreate")
        // Initialize notificationBuilder & TTS class
        currentNotificationBuilder = baseNotificationBuilder
        tts = TextToSpeech(this, this)
        setupObservers()
    }

    override fun onInit(status: Int) {
        /* Initialize TTS here */
        Timber.i("onInit")
        initializeTTS(status = status)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        // Handle action from the activity
        intent?.let {
            when (it.action) {
                // Timer related actions
                ACTION_INITIALIZE_DATA -> {
                    /*Is called when navigating from ListScreen to DetailScreen, fetching data
                    * from database here -> data initialization*/
                    Timber.i("ACTION_INITIALIZE_DATA")
                    initializeData(it)
                }
                ACTION_START -> {
                    /*This is called when Start-Button is pressed, starting timer here and setting*/
                    Timber.i("ACTION_START")
                    val intentTime = it.extras?.getLong(EXTRA_TOTAL_TIME) ?: TIMER_STARTING_IN_TIME
                    startServiceTimer(intentTime)
                }
                ACTION_PAUSE -> {
                    /*Called when pause button is pressed, pause timer, set isTimerRunning = false*/
                    Timber.i("ACTION_PAUSE")
                    pauseTimer()
                }
                ACTION_RESUME -> {
                    /*Called when resume button is pressed, resume timer here, set isTimerRunning
                    * = true*/
                    Timber.i("ACTION_RESUME")
                    resumeTimer()
                }
                ACTION_CANCEL -> {
                    /*This is called when cancel button is pressed - resets the current timer to
                    * start state*/
                    Timber.i("ACTION_CANCEL")
                    cancelServiceTimer()
                }
                ACTION_CANCEL_AND_RESET -> {
                    /*Is called when navigating back to ListsScreen, resetting acquired data
                    * to null*/
                    Timber.i("ACTION_CANCEL_AND_RESET")
                    cancelServiceTimer()
                    resetData()
                }

                // Audio related actions
                ACTION_MUTE -> {
                    /*Sets current audio state to mute*/
//                    audioState = AudioState.MUTE
                }
                ACTION_VIBRATE -> {
                    /*Sets current audio state to vibrate*/
//                    audioState = AudioState.VIBRATE
                }
                ACTION_SOUND -> {
                    /*Sets current audio state to sound enabled*/
//                    audioState = AudioState.SOUND
                }
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // UI is visible, use service without being foreground
        Timber.i("onBind")
        isBound = true
        if (!isKilled) pushToBackground()
        return super.onBind(intent)
    }

    override fun onRebind(intent: Intent?) {
        // UI is visible again, push service to background -> notification are not visible
        Timber.i("onRebind")
        isBound = true
        if (!isKilled) pushToBackground()
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        // UI is not visible anymore, push service to foreground -> notifications visible
        Timber.i("onUnbind")
        isBound = false
        if (!isKilled) pushToForeground()
        // return true so onRebind is used if service is alive and client connects
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy")
        // cancel coroutine job and TTS
        serviceJob.cancel()
        tts?.stop()
        tts?.shutdown()
    }

    private fun startTimer(wasPaused: Boolean = false, newTotalTime: Long? = null) {
//        workout?.let {

        // time to count down
        val time = if (wasPaused) millisToCompletion else (newTotalTime ?: TIMER_STARTING_IN_TIME)
//            getTimeFromWorkoutState(wasPaused, workoutState, millisToCompletion, it)
//        Timber.i("Starting timer - time: $time - workoutState: ${workoutState.stateName}")

        // post start values
        elapsedTimeInMillisEverySecond.postValue(time)
        elapsedTimeInMillis.postValue(time)
        lastSecondTimestamp = time

        //initialize timer and start
        timer = object : CountDownTimer(time, TIMER_UPDATE_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                /*handle what happens on every tick with interval of TIMER_UPDATE_INTERVAL*/
                onTimerTick(millisUntilFinished)
            }

            override fun onFinish() {
                /*handle finishing of a timer
                * start new one if there are still repetition left*/
                Timber.i("onFinish")
                onTimerFinish()
            }
        }.start()

//        }
    }

    private fun onTimerTick(millisUntilFinished: Long) {
        millisToCompletion = millisUntilFinished
        elapsedTimeInMillis.postValue(millisUntilFinished)
        if (millisUntilFinished <= lastSecondTimestamp - 1000L) {
            lastSecondTimestamp -= 1000L
            //Timber.i("onTick - lastSecondTimestamp: $lastSecondTimestamp")
            elapsedTimeInMillisEverySecond.postValue(lastSecondTimestamp)

//            // if lastSecondTimestamp within 3 seconds of end, start counting/vibrating
//            if (lastSecondTimestamp <= 3000L)
//                speakOrVibrate(
//                    tts = tts,
//                    vibrator = vibrator,
//                    audioState = audioState,
//                    sayText = millisToSeconds(lastSecondTimestamp).toString(),
//                    vibrationLength = 200L
//                )
        }
    }

    private fun onTimerFinish() {
        // increase timerIndex
        timerIndex += 1
        Timber.i("onTimerFinish - timerIndex: $timerIndex - maxRep: $timerMaxRepetitions")
        cancelTimer()

    }

    private fun pauseTimer() {
        currentTimerState.postValue(TimerState.PAUSED)
        timer?.cancel()
    }

    private fun resumeTimer() {
        currentTimerState.postValue(TimerState.RUNNING)
        startTimer(wasPaused = true)
    }

    private fun cancelTimer() {
        timer?.cancel()
        resetTimer()
    }

    private fun resetTimer() {
        timerIndex = 0
//        timerMaxRepetitions = workout?.repetitions?.times(2)?.minus(2) ?: 0
//        workoutState = WorkoutState.STARTING
        postInitData()
    }

    private fun initializeData(intent: Intent) {
        if (!isInitialized) {
            intent.extras?.let {
//                val id = it.getLong(EXTRA_WORKOUT_ID)
//                if (id != -1L) {
                // id is valid
                currentNotificationBuilder
//                        .setContentIntent(buildMainActivityPendingIntentWithId(id, this))

                // launch coroutine, fetch workout from db & audiostate from data store
                serviceScope.launch {
//                        workout = workoutRepository.getWorkout(id).first()
//                        audioState = AudioState.valueOf(preferenceRepository.getCurrentSoundState())
                    isInitialized = true
                    postInitData()
                }
//                }
            }
        }
    }

    private fun initializeTTS(status: Int) {
        tts?.let {
            if (status == TextToSpeech.SUCCESS) {
                val result = it.setLanguage(Locale.US)
                it.voice = it.defaultVoice
                it.language = Locale.US

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Timber.d("The Language specified is not supported!")
                }
            } else {
                Timber.d("Initialization Failed!")
            }
        }
    }

    private fun postInitData() {
        /*Post current data to MutableLiveData*/
//        workout?.let {
        currentTimerState.postValue(TimerState.EXPIRED)
//            currentWorkout.postValue(it)
//            currentWorkoutState.postValue(WorkoutState.STARTING)
//            currentRepetition.postValue(1)
        elapsedTimeInMillis.postValue(TIMER_STARTING_IN_TIME)
        elapsedTimeInMillisEverySecond.postValue(TIMER_STARTING_IN_TIME)
//        }
    }

    private fun resetData() {
        isInitialized = false
//        workout = null
//        // set current workout to dummyWorkout
//        currentWorkout.postValue(dummyWorkout)
//        // -1 is an invalid value, therefore repString will reset to an empty string
//        currentRepetition.postValue(-1)
    }

    private fun startServiceTimer(newTotalTime: Long) {
        // get wakelock
        acquireWakelock()
        isKilled = false
        resetTimer()
        startTimer(newTotalTime = newTotalTime)
        totalTimeInMillis.postValue(newTotalTime)
        currentTimerState.postValue(TimerState.RUNNING)
    }

    private fun cancelServiceTimer() {
        releaseWakelock()
        cancelTimer()
        currentTimerState.postValue(TimerState.EXPIRED)
        isKilled = true
        stopForeground(true)
    }

    private fun acquireWakelock() {
        // acquire a wakelock
        wakeLock = (getSystemService(POWER_SERVICE) as PowerManager).run {
            newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK,
                "com.ankitsuda.rebound.resttimer:RestTimerService::lock"
            ).apply {
                acquire()
            }
        }
    }

    private fun releaseWakelock() {
        // release wakelock
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                    Timber.d("Released wakelock")
                }
            }
        } catch (e: Exception) {
            Timber.d("Wasn't able to release wakelock ${e.message}")
        }
    }

    private fun pushToForeground() {
        Timber.i("pushToForeground - isBound: $isBound")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            createNotificationChannel(notificationManager)
        startForeground(Constants.NOTIFICATION_ID, baseNotificationBuilder.build())
        currentTimerState.value?.let { updateNotificationActions(it) }
    }

    private fun pushToBackground() {
        Timber.i("pushToBackground - isBound: $isBound")
        stopForeground(true)
    }

    private fun setupObservers() {
        // observe timerState and update notification actions
        currentTimerState.observe(this, Observer {
            Timber.i("currentTimerState changed - ${it.stateName}")
            if (!isKilled && !isBound)
                updateNotificationActions(it)
        })

        // Observe timeInMillis and update notification
        elapsedTimeInMillisEverySecond.observe(this, Observer {
            if (!isKilled && !isBound) {
                // Only do something if timer is running and service in foreground
                val notification = currentNotificationBuilder
                    .setContentText(getFormattedStopWatchTime(it))
                notificationManager.notify(Constants.NOTIFICATION_ID, notification.build())
            }
        })
    }

    private fun updateNotificationActions(state: TimerState) {
        // Updates actions of current notification depending on TimerState
        val notificationActionText = if (state == TimerState.RUNNING) "Pause" else "Resume"

        // Build pendingIntent depending on TimerState
        val pendingIntent = if (state == TimerState.RUNNING) {
            pauseActionPendingIntent
        } else {
            resumeActionPendingIntent
        }

        // Clear current actions
        currentNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(currentNotificationBuilder, ArrayList<NotificationCompat.Action>())
        }

        // Set Action, icon seems irrelevant
        currentNotificationBuilder = baseNotificationBuilder
            .setContentTitle("Timer")
            .addAction(R.drawable.ic_alarm, notificationActionText, pendingIntent)
            .addAction(R.drawable.ic_alarm, "Cancel", cancelActionPendingIntent)
        notificationManager.notify(Constants.NOTIFICATION_ID, currentNotificationBuilder.build())
    }


    fun getFormattedStopWatchTime(ms: Long?): String {
        ms?.let {
            var milliseconds = ms

            // Convert to hours
            val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
            milliseconds -= TimeUnit.HOURS.toMillis(hours)

            // Convert to minutes
            val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
            milliseconds -= TimeUnit.MINUTES.toMillis(minutes)

            // Convert to seconds
            val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)

            // Build formatted String
            return "${if (hours < 10) "0" else ""}$hours : " +
                    "${if (minutes < 10) "0" else ""}$minutes : " +
                    "${if (seconds < 10) "0" else ""}$seconds"
        }
        return ""
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

}