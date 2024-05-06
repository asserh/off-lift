package se.asser.off_lift.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import se.asser.off_lift.models.BottomNavigationItem.Companion.bottomNavigationItems
import se.asser.off_lift.models.Screens
import se.asser.off_lift.ui.theme.OffLiftTheme

@Composable
fun RootNavigator() {
    val floatingActionButtonCallback: MutableState<(() -> Unit)?> = remember { mutableStateOf({}) }
    val navController = rememberNavController()

    OffLiftTheme {
        Scaffold(
            topBar = { TopBar() },
            bottomBar = { NavBar(navController) },
            floatingActionButton = {
                if (floatingActionButtonCallback.value != null) {
                    FloatingActionButton(
                        onClick = {
                            floatingActionButtonCallback.value?.invoke()
                        }) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screens.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screens.Home.route) {
                    HomeScreen(floatingActionButtonCallback)
                }
                composable(Screens.Settings.route) {
                    Text("Settings!")
                }
            }
        }
    }
}

@Composable
private fun NavBar(
    navController: NavHostController
) {
    var navigationSelectedItem by remember {
        mutableIntStateOf(0)
    }

    NavigationBar {
        bottomNavigationItems().forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(item.icon, contentDescription = item.label)
                },
                label = { Text(item.label) },
                selected = index == navigationSelectedItem,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    navigationSelectedItem = index
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text("OFF LIFT")
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
