package se.asser.off_lift.data

import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.ext.toRealmList

val defaultExercises = listOf(
    "Squat",
    "Bench Press",
    "Deadlift",
    "Overhead Press",
    "Barbell Row",
    "Pullups",
    "Dips",
    "Curls",
    "Tricep Extensions",
    "Leg Press",
    "Leg Curls",
    "Leg Extensions",
    "Calf Raises",
    "Lat Pulldown",
).map {
    Exercise().apply {
        name = it
        supportedMetricTypes = realmSetOf(
            MetricTypeObject().apply { typeEnum = MetricType.REPS },
            MetricTypeObject().apply { typeEnum = MetricType.WEIGHT }
        )
    }
}.toRealmList()

val defaultCategories = listOf(
    "Legs" to 0xFFEA0000,
    "Chest" to 0xFF33EA00,
    "Back" to 0xFFEA00AB,
    "Shoulders" to 0xFFEAEEAB,
    "Arms" to 0xFFEA343B,
    "Core" to 0xFF4F00AB,
    "Cardio" to 0xFFA42DDB,
).map {
    ExerciseCategory().apply {
        name = it.first
        colorHex = it.second
    }
}
