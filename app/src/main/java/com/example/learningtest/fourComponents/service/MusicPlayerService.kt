package com.example.learningtest.fourComponents.service

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
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
import androidx.media3.common.util.UnstableApi
import com.example.learningtest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MusicPlayerService : Service() {
    val binder = MusicBinder()

    private var mediaPlayer = MediaPlayer()

    private val currentTrack = MutableStateFlow(Track())
    private val maxDuration = MutableStateFlow(0f)
    private val currentDestination = MutableStateFlow(0f)
    private val scope = CoroutineScope(Dispatchers.Main)
    private var job: Job? = null
    private var musicList = mutableListOf<Track>()
    private val isPlaying = MutableStateFlow(false)

    inner class MusicBinder : Binder() {
        fun service(): MusicPlayerService = this@MusicPlayerService

        fun setMusics(list: List<Track>) {
            this@MusicPlayerService.musicList = list.toMutableList()
        }

        fun currentDuration(): MutableStateFlow<Float> = this@MusicPlayerService.currentDestination

        fun maxDuration(): MutableStateFlow<Float> = this@MusicPlayerService.maxDuration

        fun isPlaying(): MutableStateFlow<Boolean> = this@MusicPlayerService.isPlaying

        fun currentTrack(): MutableStateFlow<Track> = this@MusicPlayerService.currentTrack
    }

    override fun onBind(intent: Intent?): IBinder? = binder

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        intent?.let {
            when (intent.action) {
                PREV -> {
                    prev()
                }

                NEXT -> {
                    next()
                }

                PLAY_PAUSE -> {
                    playPause()
                }

                else -> {
                    currentTrack.update { songs[0] }
                    play(currentTrack.value)
                }
            }
        }

        return START_STICKY
    }

    @SuppressLint("RestrictedApi")
    @OptIn(UnstableApi::class)
    private fun sendNotification(track: Track) {
        val session = MediaSessionCompat(this, "music")

        isPlaying.update { mediaPlayer.isPlaying }

        val style =
            androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1, 2)
                .setMediaSession(session.sessionToken)

        val notification =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setStyle(style)
                .setContentTitle(track.name)
                .setContentText(track.desc)
                .addAction(R.drawable.ic_prev, "prev", prevPendingIntent())
                .addAction(
                    if (mediaPlayer.isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                    "play_pause",
                    playPausePendingIntent(),
                )
                .addAction(R.drawable.ic_play, "play_pause", playPausePendingIntent())
                .addAction(R.drawable.ic_next, "next", nextPendingIntent())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.ic_launcher_foreground,
                    ),
                )
                .build()

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

    fun play(track: Track) {
        mediaPlayer.reset()
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(this, rawUri(track.id))
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            sendNotification(track)
            updateDurations()
        }
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
        job?.cancel()
        mediaPlayer.reset()

        mediaPlayer = MediaPlayer()

        val index = musicList.indexOf(currentTrack.value)
        val prevIndex = if (index < 0) musicList.size.minus(1) else index.minus(1)
        val prevItem = musicList[prevIndex]

        currentTrack.update { prevItem }

        mediaPlayer.setDataSource(this, rawUri(currentTrack.value.id))
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            sendNotification(currentTrack.value)
            updateDurations()
        }
    }

    fun next() {
        job?.cancel()
        mediaPlayer.reset()

        mediaPlayer = MediaPlayer()
        val index = musicList.indexOf(currentTrack.value)
        val nextIndex = index.plus(1).mod(musicList.size)
        val nextItem = musicList.get(nextIndex)
        currentTrack.update { nextItem }
        mediaPlayer.setDataSource(this, rawUri(nextItem.id))
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            sendNotification(currentTrack.value)
            updateDurations()
        }
    }

    private fun rawUri(id: Int): Uri = "android.resource://$packageName/$id".toUri()

    fun prevPendingIntent(): PendingIntent {
        val intent =
            Intent(this, MusicPlayerService::class.java).apply {
                action = PREV
            }
        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    fun playPausePendingIntent(): PendingIntent {
        val intent =
            Intent(this, MusicPlayerService::class.java).apply {
                action = PLAY_PAUSE
            }
        return PendingIntent.getService(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )
    }

    fun nextPendingIntent(): PendingIntent {
        val intent =
            Intent(this, MusicPlayerService::class.java).apply {
                action = PLAY_PAUSE
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
            scope.launch {
                if (mediaPlayer.isPlaying.not()) return@launch

                maxDuration.update { mediaPlayer.duration.toFloat() }

                while (true) {
                    currentDestination.update { mediaPlayer.currentPosition.toFloat() }
                    delay(1000L)
                }
            }
    }

    companion object {
        const val PREV = "prev"
        const val NEXT = "next"
        const val PLAY_PAUSE = "play_pause"
    }
}
