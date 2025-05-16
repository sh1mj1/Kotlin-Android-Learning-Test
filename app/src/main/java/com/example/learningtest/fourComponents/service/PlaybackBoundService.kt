package com.example.learningtest.fourComponents.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaybackBoundService : Service() {
    private val binder = LocalBinder()
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> get() = _isPlaying

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> get() = _progress.asStateFlow()

    private var job: Job? = null

    inner class LocalBinder : Binder() {
        fun getService(): PlaybackBoundService = this@PlaybackBoundService
    }

    override fun onBind(intent: Intent?): IBinder = binder

    fun play() {
        _isPlaying.value = true
        job?.cancel()
        job =
            CoroutineScope(Dispatchers.Default).launch {
                while (_progress.value < 60f && _isPlaying.value) {
                    Log.d(TAG, "progress: ${_progress.value}")
                    delay(1000)
                    _progress.value += 1
                }
                _isPlaying.value = false
            }
    }

    fun pause() {
        _isPlaying.value = false
        job?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}

private const val TAG = "PlaybackBoundService"
