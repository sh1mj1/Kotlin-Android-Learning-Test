package com.example.learningtest.fourComponents.service.bound

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
