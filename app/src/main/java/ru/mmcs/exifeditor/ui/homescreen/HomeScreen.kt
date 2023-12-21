package ru.mmcs.exifeditor.ui.homescreen

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import ru.mmcs.exifeditor.R
import ru.mmcs.exifeditor.ViewModelProvider
import ru.mmcs.exifeditor.navigation.NavigationDestination
import ru.mmcs.exifeditor.utils.composables.observeLifecycle


object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleResourceId: Int = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToEditor: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = ViewModelProvider.Factory)
) {
    viewModel.observeLifecycle(LocalLifecycleOwner.current.lifecycle)

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val intent = remember {
        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            type = "image/*"
        }
    }
    val imgProviderLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data?.let {
                viewModel.onImageChosen(it)
                viewModel.updateExifData()
            }
        }

    val uiState: HomeViewModel.UiState by viewModel.uiState

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(HomeDestination.titleResourceId)) },
                modifier = modifier,
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = {
                        imgProviderLauncher.launch(intent)
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.upload),
                            contentDescription = stringResource(R.string.upload)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (uiState.imgSource != null) {
                FloatingActionButton(onClick = {
                    navigateToEditor(Uri.encode(uiState.imgSource!!.toString()))
                }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = stringResource(R.string.edit)
                    )
                }
            }
        }
    ) { innerPadding ->
        HomeBody(
            imgSource = uiState.imgSource,
            tagsList = uiState.exifTags.toList(),
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun HomeBody(
    imgSource: Uri?,
    tagsList: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
) {
    if (imgSource == null) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize()
        ) {
            Text(
                text = "Choose an image to edit its data",
                fontSize = 20.0.sp
            )
        }
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = imgSource,
                contentDescription = stringResource(R.string.image),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, false)
                    .background(color = Color.LightGray, RectangleShape)
            )
            LazyColumn(
                contentPadding = PaddingValues(8.0.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, true)
            ) {
                items(items = tagsList) {
                    Row() {
                        Text(
                            text = it.first,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.0.dp))
                        Text(text = it.second)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewBody() {
    HomeBody(imgSource = Uri.EMPTY, listOf())
}
