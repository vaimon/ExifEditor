package ru.mmcs.exifeditor.data

import android.content.Context
import android.net.Uri

interface ExifRepository {
    suspend fun getExifData(uri: Uri) : Map<String, String>
}