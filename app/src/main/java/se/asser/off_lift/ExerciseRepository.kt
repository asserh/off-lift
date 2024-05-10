package se.asser.off_lift

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId
import se.asser.off_lift.data.ExerciseCategory
import se.asser.off_lift.data.ExerciseMetrics
import se.asser.off_lift.data.Exercise
import se.asser.off_lift.data.MetricTypeObject
import se.asser.off_lift.data.WorkoutLog
import se.asser.off_lift.data.WorkoutLogEntry
import java.nio.file.Files.find
import java.time.LocalDate

private const val idQuery = "id == $0"

class ExerciseRepository {
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
    val categories: Flow<ResultsChange<ExerciseCategory>> =
        realm.query<ExerciseCategory>().asFlow()

    fun workoutLogsForDate(date: LocalDate): RealmResults<WorkoutLog> {
        val dates = date.toRealmInstantRange()
        return realm.query<WorkoutLog>("startTime BETWEEN { $0, $1 }", dates.first, dates.second)
            .find()
    }

    fun logEntriesForExercise(exerciseId: ObjectId, date: LocalDate? = null): Flow<ResultsChange<WorkoutLog>> {
        return realm.query<WorkoutLog>("entries.exercise.id == $0", exerciseId).asFlow()
    }

    fun getExercisesForCategory(categoryId: ObjectId): RealmResults<Exercise> {
        return realm.query<Exercise>("ALL categories == $0", categoryId).find()
    }

    fun WorkoutLog.getEntries(): RealmList<WorkoutLogEntry> {
        return realm.query<WorkoutLog>(idQuery, this.id).find().first().entries
    }

    fun Exercise.getHistory(): RealmResults<WorkoutLog> {
        return realm.query<WorkoutLog>("entries.exercise.id == $0", this.id).find()
    }

    suspend fun <T : RealmObject> add(t: T) {
        realm.write {
            copyToRealm(t)
        }
    }

    suspend fun <T : RealmObject> remove(t: T) {
        realm.write {
            delete(t)
        }
    }

    fun getExercise(exerciseId: ObjectId): Exercise? {
        return realm.query<Exercise>(idQuery, exerciseId).find().firstOrNull()
    }
    suspend fun WorkoutLog.addEntry(entry: WorkoutLogEntry) {
        val workoutLog =
            realm.query<WorkoutLog>(idQuery, this.id).find().first()

        realm.write {
            findLatest(workoutLog)?.entries?.add(entry)
        }
    }

    suspend fun Exercise.addCategory(categoryId: ObjectId) {
        val exercise =
            realm.query<Exercise>(idQuery, this.id).find().first()

        realm.write {
            findLatest(exercise)?.categories?.add(categoryId)
        }
    }
}