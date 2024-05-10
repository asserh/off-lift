package se.asser.off_lift.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.kodein.di.compose.rememberDI
import org.kodein.di.compose.rememberFactory
import org.kodein.di.instance
import org.mongodb.kbson.ObjectId
import se.asser.off_lift.data.ExerciseMetrics
import se.asser.off_lift.data.MetricType

@Composable
fun LogExercise(
    exerciseId: ObjectId
) {
    val model: (ObjectId) -> ExerciseLoggerViewModel by rememberFactory()
    val viewModel: ExerciseLoggerViewModel by rememberDI {
        instance(arg = exerciseId)
    }
    val exercise = viewModel.exercise ?: return
    val metrics by remember {
        viewModel.metrics
    }


    Column(modifier = Modifier.padding(20.dp)) {
        Text(exercise.name)

        viewModel.entries.forEach {
            Text(it.toString())
        }
        Spacer(modifier = Modifier.weight(1f))

        exercise.supportedMetricTypes.forEach { metricType ->
            TextInputWithCounter(label = metricType.typeEnum.name, initalValue = 2.0) {
                when (metricType.typeEnum) {
                    MetricType.REPS -> metrics.reps = it.toInt()
                    MetricType.WEIGHT -> metrics.weight = it
                    MetricType.DISTANCE -> metrics.distance = it
                    MetricType.TIME -> metrics.duration = it.toLong()
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Row {
            Button(modifier = Modifier.weight(1f), onClick = { /*TODO*/ }) {
                Text("CLEAR")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                colors = ButtonDefaults.buttonColors().copy(containerColor = Color(0xFF049C0A)),
                modifier = Modifier.weight(1f),
                onClick = { viewModel.saveEntry() })
            {
                Text("SAVE")
            }
        }
    }
}

@Composable
fun TextInputWithCounter(
    label: String,
    initalValue: Double,
    onChange: (Double) -> Unit
) {
    var value by remember { mutableDoubleStateOf(initalValue) }
    Column {
        Text(label)
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {
                value -= 1
                onChange(value)
            }) {
                Text("-")
            }
            TextField(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .widthIn(20.dp),
                value = value.toString(),
                onValueChange = {
                    value = it.toDouble()
                    onChange(value)
                })
            Button(onClick = {
                value += 1
                onChange(value)
            }) {
                Text("+")
            }
        }
    }
}