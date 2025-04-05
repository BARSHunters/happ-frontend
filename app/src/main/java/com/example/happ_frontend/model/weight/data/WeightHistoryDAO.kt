package com.example.happ_frontend.model.weight.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeightHistoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weightHistoryEventEntity: WeightHistoryEventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<WeightHistoryEventEntity>)

    @Query("SELECT * FROM weight_history ORDER BY id ASC;")
    suspend fun getAll(): List<WeightHistoryEventEntity>

    @Query("DELETE FROM weight_history;")
    suspend fun deleteAll()
}