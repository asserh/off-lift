package se.asser.off_lift

import android.app.Application
import android.system.Os.bind
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.provider
import org.kodein.di.singleton
import org.mongodb.kbson.ObjectId
import se.asser.off_lift.ui.ExerciseLoggerViewModel
import se.asser.off_lift.viewmodels.AddWorkoutViewModel

class MainApplication : Application(), DIAware {
    override val di = DI.lazy {
        bind<ExerciseRepository> { singleton { ExerciseRepository() } }
        bind<AddWorkoutViewModel> { provider { AddWorkoutViewModel(instance()) } }
        bind<ExerciseLoggerViewModel> { factory { id: ObjectId -> ExerciseLoggerViewModel(id, instance()) } }
    }

    override fun onCreate() {
        val excerciseRepository: ExerciseRepository by di.instance()

        // TODO: Just for testing
//        if (false) {
//            GlobalScope.launch {
//                defaultCategories.forEach { excerciseRepository.add(it) }
//                val categories = excerciseRepository.categories.map { it.id }.toRealmSet()
//                defaultExercises
//                    .map { it.apply { categories.addAll(categories) } }
//                    .forEach { excerciseRepository.add(it) }
//            }
//        }

        super.onCreate()
    }
}