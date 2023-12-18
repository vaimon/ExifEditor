package ru.mmcs.exifeditor.ui.editor

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.mmcs.exifeditor.R
import ru.mmcs.exifeditor.ViewModelProvider
import ru.mmcs.exifeditor.navigation.NavigationDestination
import ru.mmcs.exifeditor.utils.FieldValidator

object ExifEditorDestination : NavigationDestination {
    override val route = "editor"
    override val titleResourceId: Int = R.string.edit_tags
    val uriArg = "uri"
    val fullRoute = "$route/{$uriArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExifEditorScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExifEditorViewModel = viewModel(factory = ViewModelProvider.Factory)
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(ExifEditorDestination.titleResourceId)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        onNavigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        EditorBody(
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorBody(
    viewModel: ExifEditorViewModel,
    modifier: Modifier
) {
    val uiState by viewModel.uiState

    Column(
        modifier = modifier
    ) {
        Row(Modifier.fillMaxWidth()){
            TagTextField(
                value = uiState.latitude,
                label = stringResource(R.string.latitude),
                onValueChanged = { viewModel.onInputValueChanged(latitude = it) },
                validator = FieldValidator::validateLatitude,
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f, true)
            )
            TagTextField(
                value = uiState.longitude,
                label = stringResource(R.string.longitude),
                onValueChanged = { viewModel.onInputValueChanged(longitude = it) },
                validator = FieldValidator::validateLongitude,
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f, true)
            )
        }

        Row(Modifier.fillMaxWidth()){
            TagTextField(
                value = uiState.deviceManufacturer,
                label = stringResource(R.string.device_brand),
                onValueChanged = { viewModel.onInputValueChanged(deviceManufacturer = it) },
                validator = FieldValidator::validateManufacturer,
                modifier = Modifier.weight(1f, true)
            )
            TagTextField(
                value = uiState.deviceModel,
                label = stringResource(R.string.device_model),
                onValueChanged = { viewModel.onInputValueChanged(deviceModel = it) },
                validator = FieldValidator::validateDeviceModel,
                modifier = Modifier.weight(1f, true)
            )
        }
        
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagTextField(
    value: String,
    label: String,
    onValueChanged: (String) -> Unit,
    validator: (String) -> Boolean,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        shape = RoundedCornerShape(32.0.dp),
        value = value,
        onValueChange = onValueChanged,
        label = { Text(label) },
        singleLine = true,
        isError = !(validator(value) || value.isEmpty()),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier.padding(4.dp)
    )
}

