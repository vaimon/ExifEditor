package ru.mmcs.exifeditor.ui.editor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.mmcs.exifeditor.R
import ru.mmcs.exifeditor.ViewModelProvider
import ru.mmcs.exifeditor.navigation.NavigationDestination
import ru.mmcs.exifeditor.utils.FieldValidator
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.saveTagsToFile()
                        navigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.save)
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
        modifier = modifier.padding(4.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
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

        Spacer(Modifier.height(4.dp))

        Row(Modifier.fillMaxWidth()) {
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

        Spacer(Modifier.height(4.dp))

        TagDatePicker(
            value = uiState.creationDate,
            onValueChanged = { viewModel.onInputValueChanged(creationDate = it) })
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

private enum class PickerStatus {
    Inactive, DateSelection, TimeSelection,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagDatePicker(
    value: String,
    onValueChanged: (String) -> Unit,
) {
    var pickerStatus by remember { mutableStateOf(PickerStatus.Inactive) }
    val dateTimeFormat = remember { SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.ENGLISH) }
    val calendar = Calendar.getInstance().apply {
        if (value.isNotBlank()) {
            time = dateTimeFormat.parse(value)
        }
    }

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = calendar.timeInMillis)
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
    )

    OutlinedTextField(
        shape = RoundedCornerShape(32.0.dp),
        value = value,
        onValueChange = {},
        label = { Text(stringResource(R.string.date_time)) },
        singleLine = true,
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = stringResource(R.string.edit),
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        pickerStatus = PickerStatus.DateSelection
                    }
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusable(false)
    )

    if (pickerStatus != PickerStatus.Inactive) {
        Dialog(
            onDismissRequest = { pickerStatus = PickerStatus.Inactive },
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (pickerStatus == PickerStatus.DateSelection) {
                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                            .fillMaxWidth()
                    )
                } else {
                    TimePicker(
                        state = timePickerState,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                            .fillMaxWidth()
                    )
                }


                if (pickerStatus == PickerStatus.DateSelection) {
                    TextButton(
                        onClick = {
                            pickerStatus = PickerStatus.TimeSelection
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 8.dp)
                    ) {
                        Text(stringResource(R.string.next))
                    }
                } else {
                    TextButton(
                        onClick = {
                            pickerStatus = PickerStatus.Inactive
                            datePickerState.selectedDateMillis?.let {
                                onValueChanged(dateTimeFormat.format(Calendar.getInstance().apply {
                                    timeInMillis = it
                                    set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                    set(Calendar.MINUTE, timePickerState.minute)
                                }.time))
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 8.dp)
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                }
            }
        }
    }

}