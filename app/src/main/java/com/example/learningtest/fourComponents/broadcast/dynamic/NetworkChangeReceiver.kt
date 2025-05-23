package com.example.learningtest.fourComponents.broadcast.dynamic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NetworkChangeReceiver(private val onNetworkChanged: () -> Unit) : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        onNetworkChanged()
    }
}
