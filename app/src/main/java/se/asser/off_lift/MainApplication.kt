package se.asser.off_lift

import android.app.Application
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.singleton

class MainApplication : Application(), DIAware {
    override val di = DI.lazy {
        bind { singleton { ExcerciseRepository() } }
    }
}