package ru.mmcs.exifeditor.ui.editor

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.mmcs.exifeditor.R
import ru.mmcs.exifeditor.ViewModelProvider
import ru.mmcs.exifeditor.navigation.NavigationDestination
import ru.mmcs.exifeditor.ui.homescreen.HomeViewModel

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
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun EditorBody(
    modifier: Modifier
) {
}
