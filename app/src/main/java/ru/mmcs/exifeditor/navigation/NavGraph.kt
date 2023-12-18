package ru.mmcs.exifeditor.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.mmcs.exifeditor.ui.editor.ExifEditorDestination
import ru.mmcs.exifeditor.ui.editor.ExifEditorScreen
import ru.mmcs.exifeditor.ui.homescreen.HomeDestination
import ru.mmcs.exifeditor.ui.homescreen.HomeScreen

interface NavigationDestination {
    val route: String
    val titleResourceId: Int
}

@Composable
fun ExifEditorNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToEditor = {
                                   navController.navigate(ExifEditorDestination.route)
                },
            )
        }
        composable(route = ExifEditorDestination.route) {
            ExifEditorScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}