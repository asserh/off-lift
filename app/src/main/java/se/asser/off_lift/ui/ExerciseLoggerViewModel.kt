package se.asser.off_lift.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import se.asser.off_lift.ExerciseRepository
import se.asser.off_lift.data.WorkoutLogEntry

class ExerciseLoggerViewModel(
    exerciseId: ObjectId,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    val entries: SnapshotStateList<WorkoutLogEntry> = mutableStateListOf()

    init {
        viewModelScope.launch {
            exerciseRepository.logEntriesForExercise(exerciseId).collect { result ->
                entries.clear()
                entries.addAll(result.list.map { it.entries.map { it } }.flatten())
            }
        }
    }

    suspend fun addEntry(entry: WorkoutLogEntry) {
        exerciseRepository.add(entry)
    }

    override fun onCleared() {
        print("Here we go!")
        super.onCleared()
    }
}