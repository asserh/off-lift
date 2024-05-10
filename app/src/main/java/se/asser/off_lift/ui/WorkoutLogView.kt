package se.asser.off_lift.composables

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.kodein.di.compose.rememberDI
import org.kodein.di.instance
import se.asser.off_lift.ExerciseRepository
import java.time.LocalDate

@Composable
fun WorkoutLogView(
    date: LocalDate
) {
    val exerciseRepository: ExerciseRepository by rememberDI { instance() }

    val logs = remember { exerciseRepository.workoutLogsForDate(date) }

    LazyColumn() {
        items(logs.size) { index ->
            Text(logs[index].name.toString())
        }
    }
}