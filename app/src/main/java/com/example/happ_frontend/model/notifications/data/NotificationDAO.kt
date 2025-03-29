package com.example.happ_frontend.model.notifications.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notificationEntity: NotificationEntity)

    @Query("SELECT * FROM notifications ORDER BY id DESC;")
    suspend fun getAllNotifications() : List<NotificationEntity>

    @Query("DELETE FROM notifications;")
    suspend fun deleteAllNotifications()
}