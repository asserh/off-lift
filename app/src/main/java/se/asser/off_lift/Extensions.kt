package se.asser.off_lift

import io.realm.kotlin.types.RealmInstant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

fun LocalDate.toDate(): Date {
    return Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
}

fun LocalDate.toRealmInstantRange(): Pair<RealmInstant, RealmInstant> {
    val startTime = this.atStartOfDay(ZoneId.systemDefault()).toInstant()
    val endTime = this.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()

    return RealmInstant.from(
        startTime.epochSecond,
        startTime.nano
    ) to RealmInstant.from(endTime.epochSecond, endTime.nano)
}