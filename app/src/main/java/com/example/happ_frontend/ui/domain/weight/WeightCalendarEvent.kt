package com.example.happ_frontend.ui.domain.weight

import kotlinx.datetime.LocalDateTime

data class WeightCalendarEvent(
    override val dateTime: LocalDateTime,
    override val value: Mass
) : CalendarEvent<Mass>(dateTime, value)
