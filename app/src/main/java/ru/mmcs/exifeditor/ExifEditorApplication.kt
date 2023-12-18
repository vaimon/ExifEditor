package ru.mmcs.exifeditor

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.mmcs.exifeditor.data.AppContainer
import ru.mmcs.exifeditor.data.AppDataContainer
import ru.mmcs.exifeditor.navigation.ExifEditorNavHost

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ExifEditorApp(navController: NavHostController = rememberNavController()) {
    ExifEditorNavHost(navController = navController)
}

class ExifEditorApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}