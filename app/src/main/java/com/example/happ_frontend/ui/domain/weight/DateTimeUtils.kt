package com.example.happ_frontend.ui.domain.weight

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime
import java.time.chrono.ChronoLocalDateTime
import kotlinx.datetime.Clock as KClock
import kotlinx.datetime.Instant as KInstant
import kotlinx.datetime.LocalDateTime as KLocalDateTime
import kotlinx.datetime.LocalTime as KLocalTime
import kotlinx.datetime.TimeZone as KTimeZone

fun KLocalTime.Companion.now(): KLocalTime {
    val currentMoment = KClock.System.now()
    return currentMoment
        .toLocalDateTime(KTimeZone.currentSystemDefault())
        .time
}

fun KLocalDateTime.toEpochMilliseconds(): Long {
    return this.toInstant(KTimeZone.currentSystemDefault()).toEpochMilliseconds()
}

fun KLocalDateTime.Companion.fromEpochMilliseconds(ms: Long): KLocalDateTime {
    return KInstant.fromEpochMilliseconds(ms).toLocalDateTime(KTimeZone.currentSystemDefault())
}

operator fun KLocalDateTime.plus(period: DateTimePeriod): KLocalDateTime {
    val currentZone = KTimeZone.currentSystemDefault()

    val instant = this.toInstant(currentZone)
    val updatedInstant = instant.plus(period, currentZone)
    return updatedInstant.toLocalDateTime(currentZone)
}

operator fun KLocalDateTime.minus(period: DateTimePeriod): KLocalDateTime {
    val currentZone = KTimeZone.currentSystemDefault()

    val instant = this.toInstant(currentZone)
    val updatedInstant = instant.minus(period, currentZone)
    return updatedInstant.toLocalDateTime(currentZone)
}

operator fun KLocalDateTime.minus(other: KLocalDateTime): DateTimePeriod =
    KTimeZone.currentSystemDefault().let { currentZone ->
        this.toInstant(currentZone).minus(other.toInstant(currentZone), currentZone)
}


operator fun DateTimePeriod.times(magnitude: Int): DateTimePeriod {
    return DateTimePeriod(
        this.years * magnitude,
        this.months * magnitude,
        this.days * magnitude,
        this.hours * magnitude,
        this.minutes * magnitude,
        this.seconds * magnitude,
        this.nanoseconds * magnitude.toLong()
    )
}

fun LocalDateTime.toChronoLocalDateTime(): ChronoLocalDateTime<*> {
    return this
}