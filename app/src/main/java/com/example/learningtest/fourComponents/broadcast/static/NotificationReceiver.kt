package com.example.learningtest.fourComponents.broadcast.static

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.learningtest.R

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent?,
    ) {
        val notificationManager: NotificationManager = notificationManager(context)
        val notification = notification(context)
        notificationManager.notify(1, notification)
    }

    private fun notificationManager(context: Context): NotificationManager {
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(
                "alarm_channel_id",
                "출고 알림",
                NotificationManager.IMPORTANCE_HIGH,
            )
        notificationManager.createNotificationChannel(channel)
        return notificationManager
    }

    private fun notification(context: Context): Notification {
        val activityIntent =
            Intent(context, AlarmActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        val pendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                activityIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
            )

        val notification =
            NotificationCompat.Builder(context, "alarm_channel_id")
                .setSmallIcon(R.drawable.ic_info)
                .setContentTitle("출고 예정 알림")
                .setContentText("출고 30분 전입니다. 확인해주세요.")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        return notification
    }
}
