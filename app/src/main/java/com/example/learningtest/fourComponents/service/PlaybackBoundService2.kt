package com.example.learningtest.fourComponents.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.learningtest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaybackBoundService2 : Service() {
    val binder = LocalBinder()
    lateinit var notificationManager: NotificationManager
    private val channelId = "playback_channel"
    private val channelName = "Playback Notification"

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> get() = _progress

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> get() = _isPlaying

    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()
        val channel =
            NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW,
            )
        notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        startForegroundServiceWithNotification()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = binder

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    inner class LocalBinder : Binder() {
        fun getService(): PlaybackBoundService2 = this@PlaybackBoundService2
    }

    fun play() {
        _isPlaying.value = true
        job?.cancel()

        updateNotification("Music is playing")

        job =
            CoroutineScope(Dispatchers.Default).launch {
                while (_progress.value < 60f && _isPlaying.value) {
                    delay(1000)
                    _progress.value += 1
                }
                _isPlaying.value = false
                updateNotification("Playback finished.")
            }
    }

    fun pause() {
        _isPlaying.value = false
        job?.cancel()
        updateNotification("Playback paused.")
    }

    private fun startForegroundServiceWithNotification() {
        val notification: Notification =
            NotificationCompat.Builder(this, channelId)
                .setContentTitle("Music Playback")
                .setContentText("Music is paused")
                .setSmallIcon(R.drawable.ic_play_arrow)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()

        startForeground(1, notification)
    }

    private fun updateNotification(contentText: String) {
        val notification =
            NotificationCompat.Builder(this, channelId)
                .setContentTitle("Music Playback")
                .setContentText(contentText)
                .setSmallIcon(R.drawable.ic_play_arrow)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()
        notificationManager.notify(1, notification)
    }
}
