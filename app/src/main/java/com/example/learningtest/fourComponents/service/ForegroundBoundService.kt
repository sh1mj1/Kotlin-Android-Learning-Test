package com.example.learningtest.fourComponents.service

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.media.app.NotificationCompat.MediaStyle
import androidx.media3.common.util.UnstableApi
import com.example.learningtest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForegroundBoundService : Service() {
    private val binder = MusicBinder()
    private val mediaPlayer = MediaPlayer()
    private lateinit var session: MediaSessionCompat
    private val currentTrack = MutableStateFlow(Track())
    private val maxDuration = MutableStateFlow(0f)
    private val currentDestination = MutableStateFlow(0f)
    private var job: Job? = null
    private var musicList = mutableListOf<Track>()
    private val isPlaying = MutableStateFlow(false)

    inner class MusicBinder : Binder() {
        fun service(): ForegroundBoundService = this@ForegroundBoundService

        fun setMusics(list: List<Track>) {
            this@ForegroundBoundService.musicList = list.toMutableList()
        }

        fun currentDuration(): MutableStateFlow<Float> = this@ForegroundBoundService.currentDestination

        fun maxDuration(): MutableStateFlow<Float> = this@ForegroundBoundService.maxDuration

        fun isPlaying(): MutableStateFlow<Boolean> = this@ForegroundBoundService.isPlaying

        fun currentTrack(): MutableStateFlow<Track> = this@ForegroundBoundService.currentTrack
    }

    override fun onCreate() {
        super.onCreate()
        session = MediaSessionCompat(this, "music")
    }

    override fun onBind(intent: Intent?): IBinder? = binder

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        intent?.let {
            when (intent.action) {
                PREV -> prev()
                NEXT -> next()
                PLAY_PAUSE -> playPause()
                else -> play(songs[0])
            }
        }

        return START_STICKY
    }

    @OptIn(UnstableApi::class)
    private fun sendNotification(track: Track) {
        isPlaying.update { mediaPlayer.isPlaying }

        val notification = notification(track)
        startForegroundWithPermissionCheck(notification)
    }

    fun playPause() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.start()
        }
        sendNotification(currentTrack.value)
    }

    fun prev() {
        val prevTrack: Track =
            musicList.run {
                indexOf(currentTrack.value)
                    .let { if (it < 0) musicList.size - 1 else it - 1 }
                    .let { musicList[it] }
            }
        play(prevTrack)
    }

    fun next() {
        val nextTrack: Track =
            musicList.run {
                indexOf(currentTrack.value)
                    .let { (it + 1) % size }
                    .let { this[it] }
            }
        play(nextTrack)
    }

    private fun play(track: Track) {
        job?.cancel()
        mediaPlayer.reset()
        currentTrack.update { track }

        with(mediaPlayer) {
            setDataSource(this@ForegroundBoundService, rawUri(track.id))
            prepareAsync()
            setOnPreparedListener {
                start()
                sendNotification(track)
                updateDurations()
            }
        }
    }

    private fun rawUri(id: Int): Uri = "android.resource://$packageName/$id".toUri()

    fun pendingIntent(intentAction: String): PendingIntent {
        val intent =
            Intent(this, ForegroundBoundService::class.java).apply {
                action = intentAction
            }
        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    fun updateDurations() {
        job =
            CoroutineScope(Dispatchers.Main).launch {
                if (mediaPlayer.isPlaying.not()) return@launch

                maxDuration.update { mediaPlayer.duration.toFloat() }

                while (true) {
                    currentDestination.update { mediaPlayer.currentPosition.toFloat() }
                    delay(1000L)
                }
            }
    }

    private fun notification(track: Track): Notification {
        val style =
            MediaStyle()
                .setShowActionsInCompactView(0, 1, 2)
                .setMediaSession(session.sessionToken)

        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setStyle(style)
                .setContentTitle(track.name)
                .setContentText(track.desc)
                .addAction(R.drawable.ic_prev, "prev", pendingIntent(PREV))
                .addAction(
                    if (mediaPlayer.isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                    "play_pause",
                    pendingIntent(PLAY_PAUSE),
                )
                .addAction(R.drawable.ic_next, "next", pendingIntent(NEXT))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.ic_launcher_foreground,
                    ),
                )
                .build()
        return notification
    }

    private fun startForegroundWithPermissionCheck(notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startForeground(1, notification)
            }
        } else {
            startForeground(1, notification)
        }
    }

    companion object {
        const val PREV = "prev"
        const val NEXT = "next"
        const val PLAY_PAUSE = "play_pause"
    }
}
