package ru.mmcs.exifeditor

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.mmcs.exifeditor.navigation.ExifEditorNavHost

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InventoryApp(navController: NavHostController = rememberNavController()) {
    ExifEditorNavHost(navController = navController)
}