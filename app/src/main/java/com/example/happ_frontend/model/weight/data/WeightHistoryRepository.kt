package com.example.happ_frontend.model.weight.data

interface WeightHistoryRepository {
    suspend fun getAllWeightHistoryEventsStream(): List<WeightHistoryEventEntity>
    suspend fun insertAllWeightHistoryEvents(newEvents: List<WeightHistoryEventEntity>)
    suspend fun insertWeightHistoryEvent(event: WeightHistoryEventEntity)
    suspend fun deleteAll()
}