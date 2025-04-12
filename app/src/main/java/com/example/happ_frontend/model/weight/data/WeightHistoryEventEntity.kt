package com.example.happ_frontend.model.weight.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_history")
data class WeightHistoryEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val prediction: Boolean,
    val dateTime: Long,
    val weight: Double
)
