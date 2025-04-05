package com.example.happ_frontend.model.weight.data

class MyWeightHistoryRepository(
    private val dao: WeightHistoryDAO
): WeightHistoryRepository {
    override suspend fun getAllWeightHistoryEventsStream(): List<WeightHistoryEventEntity> = dao.getAll()

    override suspend fun insertAllWeightHistoryEvents(newEvents: List<WeightHistoryEventEntity>) = dao.insertAll(newEvents)

    override suspend fun insertWeightHistoryEvent(event: WeightHistoryEventEntity) = dao.insert(event)

    override suspend fun deleteAll() = dao.deleteAll()
}