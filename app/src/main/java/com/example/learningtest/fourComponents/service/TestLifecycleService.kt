package com.example.learningtest.fourComponents.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class TestLifecycleService : Service() {
    companion object {
        val log = mutableListOf<String>()
    }

    override fun onCreate() {
        super.onCreate()
        log.add("onCreate")
    }

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int,
    ): Int {
        log.add("onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        log.add("onDestroy")
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
