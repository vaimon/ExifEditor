package ru.mmcs.exifeditor.data

import android.content.Context

interface AppContainer {
    val exifRepository: ExifRepository
}

class AppDataContainer(private val context: Context) : AppContainer{

    override val exifRepository: ExifRepository by lazy{
        ExifDataRepository(context)
    }
}