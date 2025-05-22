package com.example.learningtest.fourComponents.contentprovider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ShipmentDbHelper(context: Context) : SQLiteOpenHelper(context, SHIPMENT_DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE shipments (
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                item_name TEXT NOT NULL,
                quantity INTEGER NOT NULL,
                destination TEXT,
                timestamp INTEGER
            )
            """.trimIndent(),
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int,
    ) {
        db.execSQL("DROP TABLE IF EXISTS shipments")
        onCreate(db)
    }

    companion object {
        private const val SHIPMENT_DATABASE_NAME = "shipment.db"
    }
}
