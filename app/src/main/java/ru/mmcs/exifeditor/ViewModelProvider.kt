package ru.mmcs.exifeditor

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.mmcs.exifeditor.ui.editor.ExifEditorViewModel
import ru.mmcs.exifeditor.ui.homescreen.HomeViewModel

object ViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
               inventoryApplication().container.exifRepository
            )
        }

        initializer {
            ExifEditorViewModel(
                this.createSavedStateHandle(),
                inventoryApplication().container.exifRepository
            )
        }
    }
}

fun CreationExtras.inventoryApplication(): ExifEditorApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ExifEditorApplication)