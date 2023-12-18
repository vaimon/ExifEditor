package ru.mmcs.exifeditor.data

import android.content.Context
import android.net.Uri
import androidx.exifinterface.media.ExifInterface

interface ExifRepository {
    suspend fun getExifData(uri: Uri) : Map<String, String>

    suspend fun getEditableTags(uri: Uri) : Map<String, String>

    companion object{
        val TAG_DATETIME = ExifInterface.TAG_DATETIME
        val TAG_MAKE = ExifInterface.TAG_MAKE
        val TAG_MODEL = ExifInterface.TAG_MODEL
        val TAG_LATITUDE = "Latitude"
        val TAG_LONGITUDE = "Longitude"
    }

    suspend fun saveExifData(uri: Uri, tags: Map<String, String>)
}