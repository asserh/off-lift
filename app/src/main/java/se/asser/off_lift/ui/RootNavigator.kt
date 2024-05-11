package se.asser.off_lift.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import org.kodein.di.compose.rememberDI
import org.kodein.di.compose.rememberViewModel
import se.asser.off_lift.ExerciseRepository
import se.asser.off_lift.LocalNavController
import se.asser.off_lift.data.BottomNavigationItem.Companion.bottomNavigationItems
import se.asser.off_lift.data.Screens
import org.kodein.di.instance
import org.mongodb.kbson.ObjectId
import se.asser.off_lift.data.AppBarState
import se.asser.off_lift.viewmodels.AddWorkoutViewModel

@Composable
fun RootNavigator() {
    val floatingActionButtonCallback: MutableState<(() -> Unit)?> = remember { mutableStateOf({}) }
    val appBarState: AppBarState by rememberDI { instance() }
    val exerciseRepository: ExerciseRepository by rememberDI { instance() }

    val navController = LocalNavController.current

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
            modifier = Modifier.padding(innerPadding),
            route = Screens.Root.route
        ) {
            composable(Screens.Home.route) {
                appBarState.appBarTitle = Screens.Home.title
                HomeScreen(floatingActionButtonCallback)
            }
            composable(Screens.Settings.route) {
                appBarState.appBarTitle = Screens.Home.title
                Text("Settings!")
            }
            navigation(
                startDestination = Screens.Categories.route,
                route = Screens.CreateWorkout.route
            ) {
                composable(Screens.Categories.route) {
                    appBarState.appBarTitle = Screens.Categories.title
                    WithTitle(Screens.Categories.title) {
                        val viewModel: AddWorkoutViewModel by rememberViewModel()
                        val items = viewModel.categories.map {
                            ExerciseListItem(it.name, it.colorHex) {
                                navController.navigate("${Screens.SelectExercise.route}/${it.id.toHexString()}")
                            }
                        }
                        ExerciseList(items)
                    }
                }
                composable("${Screens.SelectExercise.route}/{categoryId}") { backStackEntry ->
                    appBarState.appBarTitle = Screens.SelectExercise.title
                    WithTitle(title = Screens.SelectExercise.title) {
                        val id =
                            backStackEntry.arguments?.getString("categoryId")?.let { ObjectId(it) }
                                ?: return@WithTitle
                        val items = exerciseRepository.getExercisesForCategory(id).map {
                            ExerciseListItem(it.name) {
                                navController.navigate("${Screens.LogExercise.route}/${it.id.toHexString()}")
                            }
                        }
                        ExerciseList(items)
                    }
                }
                composable("${Screens.LogExercise.route}/{exerciseId}") { backStackEntry ->
                    val id =
                        backStackEntry.arguments?.getString("exerciseId")?.let { ObjectId(it) }
                            ?: return@composable
                    floatingActionButtonCallback.value = null
                    LogExercise(exerciseId = id)

                }
            }
        }

    }
}

@Composable
private fun WithTitle(title: String, content: @Composable () -> Unit) {
    val appBarState: AppBarState by rememberDI { instance() }
    LaunchedEffect(Unit) {
        appBarState.appBarTitle = title
        appBarState.actions = { }
    }
    content()
}

@Composable
private fun NavBar(
    navController: NavHostController
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    if (backStackEntry?.destination?.parent?.route != Screens.Root.route) return

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
