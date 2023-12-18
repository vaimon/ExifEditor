package ru.mmcs.exifeditor.ui.editor

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.mmcs.exifeditor.data.ExifRepository

class ExifEditorViewModel(
    savedStateHandle: SavedStateHandle,
    exifRepository: ExifRepository
) : ViewModel() {
    val imageUri: Uri = Uri.parse(savedStateHandle[ExifEditorDestination.uriArg]!!)

}