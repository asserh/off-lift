package se.asser.off_lift.models

import java.util.Date

data class WorkoutLog(
    val id: String,
    val name: String,
    val entries: List<WorkoutLogEntry>,
    val startTime: Date,
    val endTime: Date? = null,
    val notes: String? = null,
    val rpe: Int? = null,
)

data class WorkoutLogEntry(
    val exercise: Exercise,
    val sets: List<WorkoutSet>,
    val rpe: Int? = null,
    val notes: String? = null
)

sealed interface ExcerciseMetric
data class Reps(val reps: Int): ExcerciseMetric
data class Weight(val load: Double): ExcerciseMetric
data class Distance(val reps: Double): ExcerciseMetric
data class Time(val duration: Long): ExcerciseMetric

data class WorkoutSet(val metrics: List<ExcerciseMetric>)

enum class MetricType {
    REPS,
    WEIGHT,
    DISTANCE,
    TIME
}

data class Exercise(
    val name: String,
    val targetMuscleGroups: Set<MuscleGroup>,
    val supportedMetricTypes: Set<MetricType>
)

data class MuscleGroup(
    val name: String
)
