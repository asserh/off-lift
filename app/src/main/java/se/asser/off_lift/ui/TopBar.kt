package se.asser.off_lift.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

import androidx.navigation.compose.currentBackStackEntryAsState
import org.kodein.di.compose.rememberDI
import org.kodein.di.instance
import se.asser.off_lift.LocalAppBarState
import se.asser.off_lift.LocalNavController
import se.asser.off_lift.data.AppBarState
import se.asser.off_lift.data.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    val navController = LocalNavController.current
    val backStackEntry by navController.currentBackStackEntryAsState()
    val appBarState: AppBarState by rememberDI {
        instance()
    }
    val title = appBarState.appBarTitle
    val actions = appBarState.actions
    val parentRoute = backStackEntry?.destination?.parent?.route

    TopAppBar(
        navigationIcon = {
            AnimatedVisibility(visible = parentRoute != Screens.Root.route) {
                IconButton(
                    onClick = {
                        navController.navigateUp()
                    }
                ) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Go back")
                }
            }
        },
        title = {
            Text(title)
        },
        actions = { actions() }
    )
}