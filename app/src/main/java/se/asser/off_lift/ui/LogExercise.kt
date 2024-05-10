package se.asser.off_lift.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.kodein.di.compose.rememberDI
import org.kodein.di.compose.rememberFactory
import org.kodein.di.compose.rememberViewModel
import org.kodein.di.instance
import org.mongodb.kbson.ObjectId

@Composable
fun LogExercise(
    exerciseId: ObjectId
) {
    val model: (ObjectId) -> ExerciseLoggerViewModel by rememberFactory()
    val viewModel: ExerciseLoggerViewModel by rememberDI {
        instance(arg = exerciseId)
    }

    Text("Exercise log")
    viewModel.entries.forEach {
        Text(it.toString())
    }
}