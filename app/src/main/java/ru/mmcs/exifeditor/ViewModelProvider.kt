package ru.mmcs.exifeditor

import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.mmcs.exifeditor.ui.editor.ExifEditorViewModel
import ru.mmcs.exifeditor.ui.homescreen.HomeViewModel

object ViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel()
        }

        initializer {
            ExifEditorViewModel()
        }
    }
}