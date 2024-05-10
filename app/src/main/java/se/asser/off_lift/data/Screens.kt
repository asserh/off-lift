package se.asser.off_lift.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screens(val route: String) {
    data object Home : Screens("home")
    data object Settings : Screens("settings")
    data object Root : Screens("root")
    data object CreateWorkout : Screens("create_workout")
    data object Categories : Screens("categories")
    data object SelectExercise : Screens("select_exercise")
    data object LogExercise : Screens("log_exercise")
}

data class BottomNavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: String
) {
    companion object {
        fun bottomNavigationItems() = listOf(
            BottomNavigationItem("Home", Icons.Default.Home, Screens.Home.route),
            BottomNavigationItem("Settings", Icons.Default.Settings, Screens.Settings.route)
        )
    }
}