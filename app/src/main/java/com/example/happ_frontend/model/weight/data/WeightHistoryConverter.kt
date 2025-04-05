package com.example.happ_frontend.model.weight.data

import com.example.happ_frontend.model.weight.WeightCalendarEvent
import com.example.happ_frontend.model.weight.kg
import java.time.LocalDateTime
import java.time.ZoneOffset

object WeightHistoryConverter {
    fun fromDatabase(entity: WeightHistoryEventEntity): WeightCalendarEvent =
        WeightCalendarEvent(
            value = entity.weight.kg,
            prediction = entity.prediction,
            dateTime = LocalDateTime.ofEpochSecond(entity.dateTime, 0, ZoneOffset.UTC)
        )

    fun toDatabase(weightHistory: WeightCalendarEvent): WeightHistoryEventEntity =
        WeightHistoryEventEntity(
            weight = weightHistory.value.kg.toDouble(),
            prediction = weightHistory.prediction,
            dateTime = weightHistory.dateTime.toEpochSecond(ZoneOffset.UTC)
        )
}