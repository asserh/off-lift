package se.asser.off_lift.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

import androidx.navigation.compose.currentBackStackEntryAsState
import se.asser.off_lift.LocalNavController
import se.asser.off_lift.data.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    val navController = LocalNavController.current
    val backStackEntry by navController.currentBackStackEntryAsState()

    TopAppBar(
        navigationIcon = {
            if (backStackEntry?.destination?.parent?.route == Screens.Root.route) return@TopAppBar
            IconButton(
                onClick = {
                    navController.navigateUp()
                }
            ) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Go back")
            }
        },
        title = {
            Text(backStackEntry?.destination?.route ?: "Off Lift")
        },
        actions = {
            IconButton(
                onClick = {
                    // TODO: go to monthly calendar view
                }
            ) {
                Icon(Icons.Default.DateRange, contentDescription = "Menu")
            }
        }
    )
}