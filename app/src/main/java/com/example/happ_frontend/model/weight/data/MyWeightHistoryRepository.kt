package com.example.happ_frontend.model.weight.data

import android.util.Log

class MyWeightHistoryRepository(
    private val dao: WeightHistoryDAO
): WeightHistoryRepository {
    init {
        Log.d("MyWeightHistoryRepository", "MyWeightHistoryRepository initialized")
    } // TODO remove

    override suspend fun getAllWeightHistoryEventsStream(): List<WeightHistoryEventEntity> = dao.getAll()

    override suspend fun insertAllWeightHistoryEvents(newEvents: List<WeightHistoryEventEntity>) = dao.insertAll(newEvents)

    override suspend fun insertWeightHistoryEvent(event: WeightHistoryEventEntity) = dao.insert(event)

    override suspend fun deleteAll() = dao.deleteAll()
}