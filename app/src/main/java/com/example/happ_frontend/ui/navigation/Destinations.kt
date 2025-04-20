package com.example.happ_frontend.ui.navigation

interface Destination {
    val route: String
}
object NotificationDest : Destination {
    override val route: String = "notificationDest"
}
object LoginDest : Destination {
    override val route: String = "loginDest"
}
object RegisterDest : Destination {
    override val route: String = "registerDest"
}
object HomeDest : Destination {
    override val route: String = "homeDest"
}
object WeightHistoryDest : Destination {
    override val route: String = "weightHistoryDest"
}
object SearchDest : Destination {
    override val route: String = "searchDest"
}
object NutritionDest : Destination {
    override val route: String = "nutritionDest"
}
object ActivityDest : Destination {
    override val route: String = "activityDest"
}