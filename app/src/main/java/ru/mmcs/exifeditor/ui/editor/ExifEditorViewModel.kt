package ru.mmcs.exifeditor.ui.editor

import android.content.ContentResolver
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.mmcs.exifeditor.data.ExifRepository

class ExifEditorViewModel(
    savedStateHandle: SavedStateHandle,
    private val exifRepository: ExifRepository
) : ViewModel() {
    val imageUri: Uri = Uri.parse(savedStateHandle[ExifEditorDestination.uriArg]!!)

    val uiState = mutableStateOf(UiState())

    init {
        viewModelScope.launch {
           uiState.value = exifRepository.getEditableTags(imageUri).toUiState()
        }
    }

    fun onInputValueChanged(creationDate: String? = null,
                            latitude: String? = null,
                            longitude: String? = null,
                            deviceManufacturer: String? = null,
                            deviceModel: String? = null){
        creationDate?.let {
            uiState.value = uiState.value.copy(creationDate = creationDate)
        }
        latitude?.let {
            uiState.value = uiState.value.copy(latitude = latitude)
        }
        longitude?.let {
            uiState.value = uiState.value.copy(longitude = longitude)
        }
        deviceManufacturer?.let {
            uiState.value = uiState.value.copy(deviceManufacturer = deviceManufacturer)
        }
        deviceModel?.let {
            uiState.value = uiState.value.copy(deviceModel = deviceModel)
        }
    }


    data class UiState(
        val creationDate: String = "",
        val latitude: String = "",
        val longitude: String = "",
        val deviceManufacturer: String = "",
        val deviceModel: String = ""
    )
}

fun Map<String, String>.toUiState() : ExifEditorViewModel.UiState{
    return ExifEditorViewModel.UiState(
        this[ExifInterface.TAG_DATETIME] ?: "",
        this["Latitude"] ?: "",
        this["Longitude"] ?: "",
        this[ExifInterface.TAG_MAKE] ?: "",
        this[ExifInterface.TAG_MODEL] ?: ""
    )
}