package com.example.learningtest.fourComponents.service

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

class PlaybackBoundServiceActivity : ComponentActivity() {
    private lateinit var playbackService: PlaybackBoundService
    private var bound by mutableStateOf(false)

    val isPlaying = mutableStateOf(false)
    val progress = mutableFloatStateOf(0f)
    val currentSecond = derivedStateOf { progress.floatValue.toInt() }

    private val connection =
        object : ServiceConnection {
            override fun onServiceConnected(
                name: ComponentName?,
                binder: IBinder?,
            ) {
                playbackService = (binder as PlaybackBoundService.LocalBinder).getService()
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
                    progress.floatValue = value
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                playbackService.isPlaying.collect { value ->
                    isPlaying.value = value
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, PlaybackBoundService::class.java).also {
            bindService(it, connection, BIND_AUTO_CREATE)
        }
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

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PlaybackControlUI(
                        isBound = bound,
                        isPlaying = isPlaying.value,
                        progress = progress.floatValue,
                        currentSecond = currentSecond.value,
                        onPlay = { playbackService.play() },
                        onPause = { playbackService.pause() },
                    )
                }
            }
        }
    }
}

@Composable
fun PlaybackControlUI(
    isBound: Boolean,
    isPlaying: Boolean,
    progress: Float,
    currentSecond: Int,
    onPlay: () -> Unit,
    onPause: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .padding(24.dp)
                .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("서비스 연결 상태: ${if (isBound) "Connected" else "Disconnected"}")
        Text("재생 상태: ${if (isPlaying) "▶️ Playing" else "⏸️ Paused"}")

        LinearProgressIndicator(
            progress = { progress / 60f },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
        )
        Text("현재 재생 시간: ${currentSecond}초")

        Button(onClick = onPlay, enabled = isBound) {
            Text("Play")
        }

        Button(onClick = onPause, enabled = isBound) {
            Text("Pause")
        }
    }
}
