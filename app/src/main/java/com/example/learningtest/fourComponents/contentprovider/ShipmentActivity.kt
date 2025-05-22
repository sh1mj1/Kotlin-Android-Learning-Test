package com.example.learningtest.fourComponents.contentprovider

import android.content.ContentResolver
import android.content.ContentValues
import android.os.Bundle
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.learningtest.fourComponents.contentprovider.ShipmentDbHelper.Companion.DESTINATION
import com.example.learningtest.fourComponents.contentprovider.ShipmentDbHelper.Companion.ITEM_NAME
import com.example.learningtest.fourComponents.contentprovider.ShipmentDbHelper.Companion.QUANTITY

class ShipmentActivity : ComponentActivity() {
    val shipmentUri = SHIPMENTS_URI.toUri()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        insertInitialShipments(contentResolver)

        setContent {
            MaterialTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
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
