package com.example.happ_frontend.ui.navigation

interface Destination {
    val route: String
}
object NotificationDest : Destination {
    override val route: String = "notificationDest"
}
object ActivityDest : Destination {
    override val route: String = "activityDest"
}