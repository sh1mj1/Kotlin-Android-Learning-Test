package com.example.learningtest.fourComponents.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ForegroundBoundMusicActivity : ComponentActivity() {
    private var isPlaying = MutableStateFlow(false)
    private val maxDuration = MutableStateFlow(0f)
    private val currentDuration = MutableStateFlow(0f)
    private val currentTrack = MutableStateFlow(Track())
    private lateinit var service: ForegroundBoundService
    private var isBound = false

    val connection =
        object : ServiceConnection {
            override fun onServiceConnected(
                p0: ComponentName?,
                binder: IBinder?,
            ) {
                service = (binder as ForegroundBoundService.MusicBinder).service()
                binder.setMusics(songs)
                lifecycleScope.launch {
                    binder.isPlaying().collectLatest {
                        isPlaying.value = it
                    }
                }

                lifecycleScope.launch {
                    binder.maxDuration().collectLatest {
                        maxDuration.value = it
                    }
                }
                lifecycleScope.launch {
                    binder.currentDuration().collectLatest {
                        currentDuration.value = it
                    }
                }

                lifecycleScope.launch {
                    binder.currentTrack().collectLatest {
                        currentTrack.value = it
                    }
                }
                isBound = true
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                isBound = false
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        createNotificationChannel()

        setContent {
            val track by currentTrack.collectAsState()
            val max by maxDuration.collectAsState()
            val current by currentDuration.collectAsState()
            val playing by isPlaying.collectAsState()

            MusicPlayerScreen(
                currentTrack = track,
                maxDuration = max,
                currentDuration = current,
                isPlaying = playing,
                onStartService = { if (!isBound) startBindService() },
                onStopService = { if (isBound) stopUnbindService() },
                onPrev = { if (isBound) service.prev() },
                onPlayPause = {
                    if (isBound) {
                        service.playPause()
                    } else {
                        startBindService()
                    }
                },
                onNext = { if (isBound) service.next() },
            )
        }
    }

    private fun stopUnbindService() {
        val intent = Intent(this@ForegroundBoundMusicActivity, ForegroundBoundService::class.java)
        stopService(intent)
        unbindService(connection)
    }

    private fun startBindService() {
        val intent =
            Intent(this@ForegroundBoundMusicActivity, ForegroundBoundService::class.java)
        startService(intent)
        bindService(intent, connection, BIND_AUTO_CREATE)
    }

    private fun createNotificationChannel() {
        val channel =
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT,
            )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

const val CHANNEL_ID = "channel_id"
const val CHANNEL_NAME = "channel_name"
