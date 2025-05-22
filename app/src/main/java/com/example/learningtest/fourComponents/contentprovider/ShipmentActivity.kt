package com.example.learningtest.fourComponents.contentprovider

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.learningtest.fourComponents.contentprovider.ShipmentDbHelper.Companion.DESTINATION
import com.example.learningtest.fourComponents.contentprovider.ShipmentDbHelper.Companion.ITEM_NAME
import com.example.learningtest.fourComponents.contentprovider.ShipmentDbHelper.Companion.QUANTITY
import kotlinx.coroutines.launch

class ShipmentActivity : ComponentActivity() {
    val shipmentUri = SHIPMENTS_URI.toUri()
    val snackbarHostState = SnackbarHostState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        insertInitialShipments(contentResolver)
        logContacts()

        setContent {
            MaterialTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                ) { innerPadding ->
                    ShipmentList(
                        shipments = shipments(),
                        modifier =
                            Modifier
                                .padding(innerPadding)
                                .padding(16.dp),
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray,
        deviceId: Int,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == REQUEST_CONTACT_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                lifecycleScope.launch {
                    snackbarHostState.showSnackbar("권한이 허용되었습니다. 연락처에 접근할 수 있습니다.")
                }
            } else {
                lifecycleScope.launch {
                    snackbarHostState.showSnackbar("권한이 거부되었습니다. 설정에 가서 권한을 허용해주세요..")
                }
            }
        }
    }

    private fun logContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                REQUEST_CONTACT_PERMISSION_CODE,
            )
        }

        val cursor =
            contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                arrayOf(ContactsContract.Contacts.DISPLAY_NAME),
                null,
                null,
                null,
            )

        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(0)
                Log.d("Contacts", "이름: $name")
            }
        }
    }

    private fun insertInitialShipments(resolver: ContentResolver) {
        resolver.delete(shipmentUri, null, null)
        val initialData =
            listOf(
                Triple("박스 테이프", 50, "창고 A"),
                Triple("택배 박스", 30, "지점 B"),
                Triple("에어캡", 70, "본사 창고"),
            )

        initialData.forEach { (itemName, quantity, destination) ->
            val values =
                ContentValues().apply {
                    put("item_name", itemName)
                    put("quantity", quantity)
                    put("destination", destination)
                    put("timestamp", System.currentTimeMillis())
                }
            resolver.insert(shipmentUri, values)
        }
    }

    private fun shipments(): List<String> {
        val cursor = contentResolver.query(shipmentUri, null, null, null, null)
        val results = mutableListOf<String>()
        cursor?.use { cursor ->
            val itemNameIndex = cursor.getColumnIndexOrThrow(ITEM_NAME)
            val quantityIndex = cursor.getColumnIndexOrThrow(QUANTITY)
            val destinationIndex = cursor.getColumnIndexOrThrow(DESTINATION)

            while (cursor.moveToNext()) {
                val name = cursor.getString(itemNameIndex)
                val qty = cursor.getInt(quantityIndex)
                val dest = cursor.getString(destinationIndex)
                results.add("[$name] $qty 개 → $dest")
            }
        }
        return results
    }

    companion object {
        private const val REQUEST_CONTACT_PERMISSION_CODE = 1001
    }
}

@Composable
fun ShipmentList(
    shipments: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "물품 목록",
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (shipments.isEmpty()) {
            Text("출고 이력이 없습니다.")
        } else {
            shipments.forEach {
                Text(it)
            }
        }
    }
}

private const val SHIPMENTS_URI = "content://${ShipmentContentProvider.AUTHORITY}/shipments"
