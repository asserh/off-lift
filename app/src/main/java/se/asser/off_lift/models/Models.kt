package se.asser.off_lift.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmSet
import io.realm.kotlin.types.annotations.PersistedName
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.Date

class WorkoutLog : RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var entries: RealmList<WorkoutLogEntry> = realmListOf()
    var startTime: RealmInstant = RealmInstant.now()
    var endTime: RealmInstant? = null
    var name: String? = null
    var notes: String? = null
    var rpe: Int? = null
}

class WorkoutLogEntry : RealmObject {
    var exercise: Exercise? = null
    var metrics: RealmList<ExerciseMetrics> = realmListOf()
    var rpe: Int? = null
    var notes: String? = null
}

class ExerciseMetrics : RealmObject {
    var reps: Int? = null
    var weight: Double? = null
    var distance: Double? = null
    var duration: Long? = null
}

enum class MetricType {
    REPS,
    WEIGHT,
    DISTANCE,
    TIME
}

class MetricTypeObject : RealmObject {
    private var type: String = MetricType.REPS.name
    var typeEnum: MetricType
        get() = MetricType.valueOf(type)
        set(value) {
            type = value.name
        }
}

class Exercise : RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var name: String = ""
    var supportedMetricTypes: RealmSet<MetricTypeObject> = realmSetOf()
}

class ExerciseCategory : RealmObject {
    @PrimaryKey
    var id: ObjectId = ObjectId()
    var name: String = ""
    var exercises: RealmList<Exercise> = realmListOf()
}
