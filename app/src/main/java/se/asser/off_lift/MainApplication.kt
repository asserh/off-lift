package se.asser.off_lift

import android.app.Application
import android.system.Os.bind
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import io.realm.kotlin.ext.toRealmSet
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import se.asser.off_lift.data.AppBarState
import se.asser.off_lift.data.defaultCategories
import se.asser.off_lift.data.defaultExercises
import se.asser.off_lift.ui.ExerciseLoggerViewModel
import se.asser.off_lift.ui.LoggerArgs
import se.asser.off_lift.viewmodels.AddWorkoutViewModel

class MainApplication : Application(), DIAware {
    override val di = DI.lazy {
        bind<ExerciseRepository> { singleton { ExerciseRepository() } }
        bind<AddWorkoutViewModel> { provider { AddWorkoutViewModel(instance()) } }
        bind<AppBarState> { singleton { AppBarState() } }
        bind<ExerciseLoggerViewModel> { factory { args: LoggerArgs -> ExerciseLoggerViewModel(args, instance()) } }
    }

    override fun onCreate() {
        val excerciseRepository: ExerciseRepository by di.instance()

        // TODO: Just for testing
        if (true) {
            GlobalScope.launch {
                defaultCategories.forEach { excerciseRepository.add(it) }
                val categories = excerciseRepository.categories.find().map { it.id }.toRealmSet()
                defaultExercises
                    .map { it.apply { categories.addAll(categories) } }
                    .forEach { excerciseRepository.add(it) }
            }
        }

        super.onCreate()
    }
}