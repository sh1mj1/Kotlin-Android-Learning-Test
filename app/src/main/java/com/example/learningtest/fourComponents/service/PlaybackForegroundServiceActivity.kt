package com.example.learningtest.fourComponents.service

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class PlaybackForegroundServiceActivity : ComponentActivity() {
    private lateinit var playbackService: PlaybackBoundService2
    private var bound by mutableStateOf(false)
    private var isPlaying by mutableStateOf(false)
    private var progress by mutableFloatStateOf(0f)
    private val currentSecond = derivedStateOf { progress.toInt() }

    private val connection =
        object : ServiceConnection {
            override fun onServiceConnected(
                name: ComponentName?,
                service: IBinder?,
            ) {
                playbackService = (service as PlaybackBoundService2.LocalBinder).getService()
                bound = true
                observeServiceState()
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                bound = false
            }
        }

    private fun observeServiceState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playbackService.progress.collect { value ->
                    progress = value
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playbackService.isPlaying.collect { value ->
                    isPlaying = value
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, PlaybackBoundService2::class.java)

        startService(intent)
        bindService(intent, connection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (bound) {
            unbindService(connection)
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
        }

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PlaybackControlUI(
                        isBound = bound,
                        isPlaying = isPlaying,
                        progress = progress,
                        currentSecond = currentSecond.value,
                        onPlay = { playbackService.play() },
                        onPause = { playbackService.pause() },
                    )
                }
            }
        }
    }
}
