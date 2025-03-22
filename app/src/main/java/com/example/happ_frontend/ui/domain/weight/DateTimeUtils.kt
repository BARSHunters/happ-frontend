package com.example.happ_frontend.ui.domain.weight

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun LocalTime.Companion.now(): LocalTime {
    val currentMoment = Clock.System.now()
    return currentMoment
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .time
}

fun LocalDateTime.toEpochMilliseconds(): Long {
    return this.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

fun LocalDateTime.Companion.fromEpochMilliseconds(ms: Long): LocalDateTime {
    return Instant.fromEpochMilliseconds(ms).toLocalDateTime(TimeZone.currentSystemDefault())
}

operator fun LocalDateTime.plus(period: DateTimePeriod): LocalDateTime {
    val currentZone = TimeZone.currentSystemDefault()

    val instant = this.toInstant(currentZone)
    val updatedInstant = instant.plus(period, currentZone)
    return updatedInstant.toLocalDateTime(currentZone)
}

operator fun LocalDateTime.minus(period: DateTimePeriod): LocalDateTime {
    val currentZone = TimeZone.currentSystemDefault()

    val instant = this.toInstant(currentZone)
    val updatedInstant = instant.minus(period, currentZone)
    return updatedInstant.toLocalDateTime(currentZone)
}

operator fun LocalDateTime.minus(other: LocalDateTime): DateTimePeriod =
    TimeZone.currentSystemDefault().let { currentZone ->
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