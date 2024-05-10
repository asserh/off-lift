package se.asser.off_lift.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import se.asser.off_lift.ExerciseRepository
import se.asser.off_lift.data.ExerciseMetrics
import se.asser.off_lift.data.WorkoutLogEntry

class ExerciseLoggerViewModel(
    exerciseId: ObjectId,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    val entries: SnapshotStateList<WorkoutLogEntry> = mutableStateListOf()
    val exercise by lazy {
        exerciseRepository.getExercise(exerciseId)
    }
    var metrics = mutableStateOf(ExerciseMetrics())

    init {
        viewModelScope.launch {
            exerciseRepository.logEntriesForExercise(exerciseId).collect { result ->
                entries.clear()
                entries.addAll(result.list.map { it.entries.map { it } }.flatten())
            }
        }
    }

    suspend fun addMetrics() {
        // TODO: Add metrics to existing log entry
    }

    fun saveEntry() {
        val entry = WorkoutLogEntry().apply {
            exercise = this@ExerciseLoggerViewModel.exercise
            metrics = realmListOf(this@ExerciseLoggerViewModel.metrics.value)
        }
        viewModelScope.launch {
            exerciseRepository.add(entry)
        }
    }

    override fun onCleared() {
        print("Here we go!")
        super.onCleared()
    }
}