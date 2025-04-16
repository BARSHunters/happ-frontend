package com.example.happ_frontend.model.weight

import java.time.LocalDateTime

/**
 * Abstract class representing a calendar event associated with a specific value.
 *
 * @param dateTime The date and time of the event.
 * @param value The value associated with the event. This can be of any non-null type.
 *
 * @constructor Creates a new instance of [CalendarEvent].
 * @author Vad1mChK
 */
abstract class CalendarEvent<T : Any>(
    open val dateTime: LocalDateTime,
    open val value: T
)