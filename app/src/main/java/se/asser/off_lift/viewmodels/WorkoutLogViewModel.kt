package se.asser.off_lift.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import se.asser.off_lift.ExerciseRepository
import java.time.LocalDate

class WorkoutLogViewModel(date: LocalDate, exerciseRepository: ExerciseRepository) : ViewModel() {
    private val logFlow = exerciseRepository.workoutLogsForDate(date).map { it.list.firstOrNull() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}