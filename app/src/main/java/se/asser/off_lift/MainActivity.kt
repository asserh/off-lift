package se.asser.off_lift

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.compose.withDI
import se.asser.off_lift.data.AppBarState
import se.asser.off_lift.ui.RootNavigator
import se.asser.off_lift.ui.theme.OffLiftTheme

class MainActivity : ComponentActivity(), DIAware {
    override val di: DI by closestDI()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            withDI(di = di) {
                CompositionLocalProvider(
                    LocalNavController provides rememberNavController(),
                    LocalAppBarState provides remember { AppBarState() }
                ) {
                    OffLiftTheme {
                        RootNavigator()
                    }
                }
            }
        }

    }
}

val LocalAppBarState = compositionLocalOf { AppBarState() }

val LocalNavController = compositionLocalOf<NavHostController> {
    error("No NavController provided")
}
