package se.asser.off_lift.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.kodein.di.compose.rememberDI
import org.kodein.di.instance
import se.asser.off_lift.ExerciseRepository
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun WorkoutLogView(
    date: LocalDate
) {
    val exerciseRepository: ExerciseRepository by rememberDI { instance() }
    val logFlow = exerciseRepository.workoutLogsForDate(date).map { it.list.firstOrNull() }
    val entries by logFlow.flatMapLatest { log ->
        if (log != null) {
            exerciseRepository.getLogEntries(
                workoutLogId = log.id
            ).map { result -> result.list }
        } else {
            flowOf(emptyList())
        }
    }.collectAsState(emptyList())

    if (entries.isEmpty()) {
        Text("No logs found for this day. Go exercise you dummy!")
    } else {
        LazyColumn() {
            items(entries.size) { index ->
                Text(entries[index].exerciseId.toString())
            }
        }
    }
}