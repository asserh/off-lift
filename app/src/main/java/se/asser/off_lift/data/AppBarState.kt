package se.asser.off_lift.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class AppBarState(
    appBarTitle: String = "Off-Lift",
    actions: @Composable () -> Unit = {}
) {
    var appBarTitle by mutableStateOf(appBarTitle)
    var actions by mutableStateOf(actions)
}