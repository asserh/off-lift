package se.asser.off_lift.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import org.kodein.di.compose.rememberDI
import org.kodein.di.instance
import se.asser.off_lift.ExerciseRepository
import se.asser.off_lift.data.WorkoutLogEntry
import java.time.LocalDate

@Composable
fun WorkoutLogView(
    date: LocalDate
) {
    val exerciseRepository: ExerciseRepository by rememberDI { instance() }
    val logFlow = exerciseRepository.workoutLogsForDate(date).map { it.list.firstOrNull() }
    val log by logFlow.collectAsState(initial = null)

    val localLog = log
    if (localLog == null) {
        Text("No logs found for this day. Go exercise you dummy!")
    } else {
        LazyColumn() {
            items(localLog.entries.size) { index ->
                LogEntryCard(entry = localLog.entries[index])
            }
        }
    }
}

@Composable
fun LogEntryCard(entry: WorkoutLogEntry) {
    // TODO: Create fukken viewmodel here
    val exerciseRepository: ExerciseRepository by rememberDI { instance() }
    val exercise by remember { mutableStateOf(exerciseRepository.getExercise(entry.exerciseId)) }

    Card(onClick = { /*TODO*/ }) {
        Text(exercise?.name.orEmpty())
        Row {
            entry.metrics.forEach {
                Text("${it.reps} reps")
            }
        }
    }
}