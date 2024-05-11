package se.asser.off_lift.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import se.asser.off_lift.ExerciseRepository
import se.asser.off_lift.data.ExerciseMetrics
import se.asser.off_lift.data.WorkoutLog
import se.asser.off_lift.data.WorkoutLogEntry
import java.time.LocalDate

data class LoggerArgs(
    val exerciseId: ObjectId,
    val date: LocalDate
)

class ExerciseLoggerViewModel(
    private val args: LoggerArgs,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    val exercise by lazy { exerciseRepository.getExercise(args.exerciseId) }

    private val logFlow = exerciseRepository.workoutLogsForDate(args.date)
        .map { it.list.firstOrNull() }

    @OptIn(ExperimentalCoroutinesApi::class)
    val entriesFlow = logFlow.flatMapLatest { log ->
        if (log != null) {
            exerciseRepository.getWorkoutLog(
                workoutLogId = log.id,
            ).map { result ->
                result.list.map { log ->
                    log.entries.filter { entry ->
                        entry.exerciseId == exercise?.id
                    }
                }.flatten()
            }
        } else {
            flowOf(emptyList())
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    var metrics = mutableStateOf(ExerciseMetrics())

    suspend fun addMetrics() {
        // TODO: Add metrics to existing log entry
    }

    suspend fun saveEntry() {
        viewModelScope.launch {
            val log = logFlow.firstOrNull() ?: createAndRetrieveLog()
            val entry = WorkoutLogEntry().apply {
                exerciseId = this@ExerciseLoggerViewModel.exercise?.id ?: args.exerciseId
                metrics = realmListOf(this@ExerciseLoggerViewModel.metrics.value)
            }
            exerciseRepository.addEntry(log.id, entry)
        }
    }

    private suspend fun createAndRetrieveLog(): WorkoutLog {
        val newLog = WorkoutLog()
        exerciseRepository.add(WorkoutLog())
        return newLog
    }
}