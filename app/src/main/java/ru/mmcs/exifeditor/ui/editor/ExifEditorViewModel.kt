package ru.mmcs.exifeditor.ui.editor

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class ExifEditorViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val imageUri: Uri = Uri.parse(savedStateHandle[ExifEditorDestination.uriArg]!!)
    
}