package com.example.happ_frontend.model.user_info.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_info")
data class UserInfoEntity(
    @PrimaryKey
    val id: Int = 0,
    val username: String,
    val name: String,
    val birthDate: Long,
    val gender: String,
    val heightCm: Int,
    val weightKg: Float,
    val weightDesire: String,
    val friendsJson: String,
    val friendsCount: Int
)
