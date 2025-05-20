package com.example.learningtest.fourComponents.service

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager

const val CHANNEL_ID = "channel_id"
const val CHANNEL_NAME = "channel_name"

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

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
