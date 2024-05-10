package se.asser.off_lift.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import se.asser.off_lift.ExerciseRepository
import se.asser.off_lift.data.ExerciseCategory

class AddWorkoutViewModel(exerciseRepository: ExerciseRepository) : ViewModel() {
    private val categoriesFlow: Flow<ResultsChange<ExerciseCategory>> = exerciseRepository.categories
    val categories: SnapshotStateList<ExerciseCategory> = mutableStateListOf()

    init {
        viewModelScope.launch {
            categoriesFlow.collect { result ->
                Log.d("AddWorkoutViewModel", "Categories: $result")
                categories.clear()
                categories.addAll(result.list.map { it })
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}