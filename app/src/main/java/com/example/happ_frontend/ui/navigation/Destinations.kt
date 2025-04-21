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

object UserInfoDest : Destination{
    override val route: String = "userInfoDest"
}

object UserInfoOtherPersonDest: Destination {
    override val route: String = "userInfoOtherPersonDest"
    val usernameArgument : String = "usernameArgument"
}

object NutritionDest : Destination {
    override val route: String = "nutritionDest"
}

object ActivityDest : Destination {
    override val route: String = "activityDest"
}
