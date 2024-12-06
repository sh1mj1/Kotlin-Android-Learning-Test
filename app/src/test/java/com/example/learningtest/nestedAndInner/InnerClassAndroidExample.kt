package com.example.learningtest.nestedAndInner

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import androidx.activity.ComponentActivity

/**
 * The Binder often needs direct access to the outer Service instance to allow clients to call the service’s public methods.
 *
 * Making the Binder an inner class ensures it retains a reference to the Service (the outer class).
 *
 */
class MyMusicService : Service() {
    private val musicPlayer = MusicPlayer()

    inner class LocalBinder : Binder() {
        fun getService(): MyMusicService = this@MyMusicService
    }

    private val binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder = binder

    fun playTrack(trackId: String) {
        musicPlayer.play(trackId)
    }

    fun pause() {
        musicPlayer.pause()
    }
}

class PlayerActivity : ComponentActivity() {
    private var musicService: MyMusicService? = null
    private var bound: Boolean = false

    private val connection =
        object : ServiceConnection {
            override fun onServiceConnected(
                name: ComponentName,
                service: IBinder,
            ) {
                val binder = service as MyMusicService.LocalBinder
                musicService = binder.getService()
                bound = true
                musicService?.playTrack("my_track_id")
            }

            override fun onServiceDisconnected(name: ComponentName) {
                bound = false
                musicService = null
            }
        }

    override fun onStart() {
        super.onStart()
        Intent(this, MyMusicService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (bound) {
            unbindService(connection)
            bound = false
        }
    }
}

class MusicPlayer {
    private var currentTrackId: String? = null
    private var _isPlaying: Boolean = false
    val isPlaying: Boolean get() = _isPlaying

    fun play(trackId: String) {
        currentTrackId = trackId
        _isPlaying = true
        println("Playing track: $trackId")
    }

    fun pause() {
        if (_isPlaying) {
            _isPlaying = false
            println("Pausing track: $currentTrackId")
        }
    }

    fun stop() {
        if (_isPlaying) {
            _isPlaying = false
            println("Stopping track: $currentTrackId")
        }
        currentTrackId = null
    }
}
