package com.example.happ_frontend.model.activity.response

import com.google.gson.annotations.SerializedName

data class ActivityDTO(
    @SerializedName("duration")
    val duration: String, // format hh:mm:ss
    @SerializedName("name")
    val name: String,
    @SerializedName("datetime")
    val datetime: String, // format yyyy-MM-dd HH:mm:ss
    @SerializedName("heartRates")
    val heartRates: List<HeartRate>
)

data class HeartRate(
    @SerializedName("timestamp")
    val timestamp: Long,
    @SerializedName("heartRate")
    val heartRate: Int
)

data class TrainingData(
    @SerializedName("username")
    val username: String,
    @SerializedName("trainingName")
    val trainingName: String,
    @SerializedName("trainingDate")
    val trainingDate: String,
    @SerializedName("trainingDuration")
    val trainingDuration: Int,
    @SerializedName("avgHeartRate")
    val avgHeartRate: Double,
    @SerializedName("maxHeartRate")
    val maxHeartRate: Int,
    @SerializedName("caloriesBurned")
    val caloriesBurned: Double,
    @SerializedName("met")
    val met: Double,
    @SerializedName("intensityZones")
    val intensityZones: List<Int>,
    @SerializedName("recoveryTime")
    val recoveryTime: Int
) 