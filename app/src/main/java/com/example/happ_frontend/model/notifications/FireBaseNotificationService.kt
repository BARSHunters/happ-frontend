package com.example.happ_frontend.model.notifications

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.time.LocalDate
import java.time.LocalDateTime

class FireBaseNotificationService : FirebaseMessagingService() {
    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful){
                Log.d("FCM", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("FCM", "Saved token: $token")
        })
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")
        //TODO отправить этот токен на сервер

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("Receive", "From: ${message.from}")
        Log.d("New message data:", "${message.data}")
        val notification = parseNotification(message)
        println(notification)
        // Сохраняем нотификацию (пока локально)
        localNotification.notifications.add(notification)
    }

    object localNotification{
        val notifications = mutableListOf<NotificationData>()
    }

    object notificationTypes {
        val types : Map<String, List<String>> =
            mapOf(
                "Activity" to emptyList(),
                "FriendRequest" to listOf("username"),
                "Achievement" to listOf("achievement_name")
            )
    }

    private fun parseNotification(message: RemoteMessage) : NotificationData {
        val data = message.data
        if (!data.containsKey("type")) {
            Log.d("Notification error", "Data does not contains key type")
            return NotificationData("", "", LocalDateTime.now())
        }
        val type = data["type"]
        if (!notificationTypes.types.containsKey(type)) {
            Log.d("Notification error", "Unknown type of message: $type")
            return NotificationData("", "", LocalDateTime.now())
        }
        for(requiredParam in notificationTypes.types[type]!!){
            if(!data.containsKey(requiredParam)){
                Log.d("Notification error", "Message of type $type does not have a required param $requiredParam")
                return NotificationData("", "", LocalDateTime.now())
            }
        }
        //TODO Если параметров больше 1?
        val notData = "" + notificationTypes.types[type]?.map { currentString ->
            data[currentString]
        }?.joinToString("")
        return NotificationData(type!!, notData, LocalDateTime.now())
    }
}