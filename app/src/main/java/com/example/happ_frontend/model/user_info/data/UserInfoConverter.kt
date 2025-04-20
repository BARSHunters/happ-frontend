package com.example.happ_frontend.model.user_info.data

import com.example.happ_frontend.model.user_info.request.FriendsListResponse
import com.example.happ_frontend.model.user_info.request.UserDataDTO
import com.google.gson.Gson
import java.time.LocalDate

object UserInfoConverter {
    private val gson = Gson()

    fun fromDto(user: UserDataDTO, friends: FriendsListResponse): UserInfoEntity {
        return UserInfoEntity(
            username = user.username,
            name = user.name,
            birthDate = user.birthDate.toEpochDay(),
            gender = user.gender.name,
            heightCm = user.heightCm,
            weightKg = user.weightKg,
            weightDesire = user.weightDesire.name,
            friendsJson = gson.toJson(friends.friends),
            friendsCount = friends.friendsCount
        )
    }

    fun toDto(entity: UserInfoEntity): UserDataDTO {
        return UserDataDTO(
            username = entity.username,
            name = entity.name,
            birthDate = LocalDate.ofEpochDay(entity.birthDate),
            gender = enumValueOf(entity.gender),
            heightCm = entity.heightCm,
            weightKg = entity.weightKg,
            weightDesire = enumValueOf(entity.weightDesire)
        )
    }
}
