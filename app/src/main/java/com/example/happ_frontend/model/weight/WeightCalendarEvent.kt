package com.example.happ_frontend.model.weight

import java.time.LocalDateTime

/**
 * Represents a weight measurement event in a calendar view.
 *
 * @param dateTime The date and time of the weight measurement.
 * @param value The mass value of the weight measurement.
 *
 * @constructor Creates a new instance of [WeightCalendarEvent].
 *
 * @see CalendarEvent
 * @author Vad1mChK
 */
data class WeightCalendarEvent(
    override val dateTime: LocalDateTime,
    override val value: Mass,
    val prediction: Boolean = false,
) : CalendarEvent<Mass>(dateTime, value)
