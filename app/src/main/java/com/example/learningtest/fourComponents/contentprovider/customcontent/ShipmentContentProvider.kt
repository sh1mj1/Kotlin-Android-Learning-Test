package com.example.learningtest.fourComponents.contentprovider.customcontent

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.core.net.toUri

class ShipmentContentProvider : ContentProvider() {
    private lateinit var dbHelper: ShipmentDbHelper

    override fun onCreate(): Boolean {
        dbHelper = ShipmentDbHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor? {
        val db = dbHelper.readableDatabase
        return when (uriMatcher.match(uri)) {
            SHIPMENTS -> db.query(SHIPMENT_TABLE, projection, selection, selectionArgs, null, null, sortOrder)
            SHIPMENT_ID -> {
                val id = ContentUris.parseId(uri)
                db.query(SHIPMENT_TABLE, projection, "_id=?", arrayOf(id.toString()), null, null, sortOrder)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(
        uri: Uri,
        values: ContentValues?,
    ): Uri? {
        val db = dbHelper.writableDatabase
        val id = db.insert(SHIPMENT_TABLE, null, values)
        if (id == -1L) return null
        return ContentUris.withAppendedId(CONTENT_URI, id)
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int {
        val db = dbHelper.writableDatabase
        return db.update(SHIPMENT_TABLE, values, selection, selectionArgs)
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int {
        val db = dbHelper.writableDatabase
        return db.delete(SHIPMENT_TABLE, selection, selectionArgs)
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            SHIPMENTS -> "vnd.android.cursor.dir/vnd.$AUTHORITY.shipments"
            SHIPMENT_ID -> "vnd.android.cursor.item/vnd.$AUTHORITY.shipments"
            else -> null
        }
    }

    companion object {
        const val AUTHORITY = "com.other.shipment.provider"
        val CONTENT_URI: Uri = "content://$AUTHORITY/shipments".toUri()
        const val SHIPMENT_TABLE = "shipments"
        private const val SHIPMENTS = 1
        private const val SHIPMENT_ID = 2

        val uriMatcher =
            UriMatcher(UriMatcher.NO_MATCH).apply {
                addURI(AUTHORITY, "shipments", SHIPMENTS)
                addURI(AUTHORITY, "shipments/#", SHIPMENT_ID)
            }
    }
}
