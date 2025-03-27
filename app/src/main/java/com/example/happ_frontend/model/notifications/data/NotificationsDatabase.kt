package com.example.happ_frontend.model.notifications.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [NotificationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NotificationsDatabase : RoomDatabase(){
    abstract fun notificationDAO() : NotificationDAO
    companion object {
        @Volatile
        private var Instance : NotificationsDatabase? = null

        fun getDatabase(context: Context) : NotificationsDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(
                    context,
                    NotificationsDatabase::class.java,
                    "notifications_database"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { Instance = it }
                return Instance!!
            }
        }
    }
}