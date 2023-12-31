package ru.mmcs.exifeditor.ui.homescreen

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toFile
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mmcs.exifeditor.data.ExifRepository
import java.io.InputStream

class HomeViewModel(
    private val exifRepository: ExifRepository
) : ViewModel(), DefaultLifecycleObserver {
    val uiState = mutableStateOf(UiState())

    fun onImageChosen(uri: Uri) {
        uiState.value = uiState.value.copy(imgSource = uri)
    }

    fun updateExifData(){
        viewModelScope.launch {
            uiState.value.imgSource?.let {
                val exifData = exifRepository.getExifData(it)
                uiState.value = uiState.value.copy(exifTags = exifData)
            }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        uiState.value.imgSource?.run{
            updateExifData()
        }
    }

    data class UiState(
        val imgSource: Uri? = null,
        val exifTags: Map<String,String> = mapOf()
    )
}