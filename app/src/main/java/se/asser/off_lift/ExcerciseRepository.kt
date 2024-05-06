package se.asser.off_lift

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.query.find
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import se.asser.off_lift.models.ExerciseCategory
import se.asser.off_lift.models.ExerciseMetrics
import se.asser.off_lift.models.Exercise
import se.asser.off_lift.models.MetricTypeObject
import se.asser.off_lift.models.WorkoutLog
import se.asser.off_lift.models.WorkoutLogEntry
import java.time.LocalDate
import java.util.Date

private const val idQuery = "id == $0"

class ExcerciseRepository {
    private val config = RealmConfiguration.create(
        schema = setOf(
            WorkoutLog::class,
            WorkoutLogEntry::class,
            Exercise::class,
            ExerciseMetrics::class,
            ExerciseCategory::class,
            MetricTypeObject::class
        )
    )
    private val realm = Realm.open(config)

    val workoutLogs: RealmResults<WorkoutLog> = realm.query<WorkoutLog>().find()
    val exercises: RealmResults<Exercise> = realm.query<Exercise>().find()

    fun workoutLogsForDate(date: LocalDate): RealmResults<WorkoutLog> {
        Log.d("asser", "GETTING LOGS for $date")
        val dates = date.toRealmInstantRange()
        return realm.query<WorkoutLog>("startTime BETWEEN { $0, $1 }", dates.first, dates.second).find()
    }

    fun WorkoutLog.getEntries(): RealmList<WorkoutLogEntry> {
        return realm.query<WorkoutLog>(idQuery, this.id).find().first().entries
    }

    fun Exercise.getHistory(): RealmResults<WorkoutLog> {
        return realm.query<WorkoutLog>("entries.exercise.id == $0", this.id).find()
    }

    suspend fun <T : RealmObject> add(t: T) {
        val ta = t
        realm.write {
            copyToRealm(t)
        }
    }

    suspend fun <T : RealmObject> remove(t: T) {
        realm.write {
            delete(t)
        }
    }

    fun getCategories(): RealmResults<ExerciseCategory> {
        return realm.query<ExerciseCategory>().find()
    }

    suspend fun WorkoutLog.addEntry(entry: WorkoutLogEntry) {
        val workoutLog =
            realm.query<WorkoutLog>(idQuery, this.id).find().first()

        realm.write {
            findLatest(workoutLog)?.entries?.add(entry)
        }
    }

    suspend fun ExerciseCategory.addExcercise(exercise: Exercise) {
        val category =
            realm.query<ExerciseCategory>(idQuery, this.id).find().first()

        realm.write {
            findLatest(category)?.exercises?.add(exercise)
        }
    }
}