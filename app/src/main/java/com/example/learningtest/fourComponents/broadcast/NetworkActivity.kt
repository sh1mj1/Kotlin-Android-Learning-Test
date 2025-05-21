package com.example.learningtest.fourComponents.broadcast

import android.app.AlertDialog
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

class NetworkActivity : ComponentActivity() {
    private lateinit var networkReceiver: NetworkChangeReceiver
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkReceiver =
            NetworkChangeReceiver { isConnected ->
                if (!isConnected) {
                    showNetworkDialog()
                } else {
                    dialog?.dismiss()
                }
            }

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)

        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "sh1mj1",
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
    }

    private fun showNetworkDialog() {
        if (dialog?.isShowing == true) return

        dialog =
            AlertDialog.Builder(this)
                .setTitle("네트워크 연결이 끊어졌습니다")
                .setMessage("연결 후에 재시도 해주세요")
                .setCancelable(false)
                .setNegativeButton("취소", null)
                .setPositiveButton("재시도") { _, _ ->
                    if (isNetworkConnected()) {
                        dialog?.dismiss()
                    } else {
                        dialog?.dismiss()
                        showNetworkDialog()
                    }
                }
                .create()

        dialog?.show()
    }

    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}
