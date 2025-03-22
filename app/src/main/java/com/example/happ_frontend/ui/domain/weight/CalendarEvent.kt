package com.example.happ_frontend.ui.domain.weight

import kotlinx.datetime.LocalDateTime

abstract class CalendarEvent<T : Any>(
    open val dateTime: LocalDateTime,
    open val value: T
)