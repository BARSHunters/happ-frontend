package com.example.happ_frontend.model.user_info.request


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.happ_frontend.model.user_info.data.UserInfoEntity

@Dao
interface UserInfoDAO {
    @Query("SELECT * FROM user_info LIMIT 1")
    suspend fun getUserInfo(): UserInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(entity: UserInfoEntity)
}
