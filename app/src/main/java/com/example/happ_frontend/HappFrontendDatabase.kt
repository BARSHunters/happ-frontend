package com.example.happ_frontend

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.happ_frontend.model.notifications.data.NotificationDAO
import com.example.happ_frontend.model.notifications.data.NotificationEntity

@Database(
    entities = [NotificationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HappFrontendDatabase : RoomDatabase(){
    abstract fun notificationDAO() : NotificationDAO
    companion object {
        @Volatile
        private var Instance : HappFrontendDatabase? = null

        fun getDatabase(context: Context) : HappFrontendDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(
                    context,
                    HappFrontendDatabase::class.java,
                    "happ_frontend_database"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { Instance = it }
                return Instance!!
            }
        }
    }
}