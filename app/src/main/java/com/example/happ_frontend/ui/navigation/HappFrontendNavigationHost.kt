package com.example.happ_frontend.ui.navigation

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesProvider
import com.example.happ_frontend.ui.screens.activity.ActivityScreen
import com.example.happ_frontend.ui.screens.home.HomeScreen
import com.example.happ_frontend.ui.screens.login_register.LoginScreen
import com.example.happ_frontend.ui.screens.login_register.RegisterScreen
import com.example.happ_frontend.ui.screens.notifications.NotificationScreen
import com.example.happ_frontend.ui.screens.nutrition.NutritionScreen
import com.example.happ_frontend.ui.screens.search.SearchScreen
import com.example.happ_frontend.ui.screens.user_info.UserInfoScreen
import com.example.happ_frontend.ui.screens.weight.WeightHistoryScreen
import com.example.happ_frontend.ui.screens.nutrition.NutritionScreen
import com.example.happ_frontend.ui.screens.activity.ActivityScreen
import com.example.happ_frontend.ui.screens.user_info.UserInfoOtherPersonScreen

@Composable
fun HappFrontendNavigationHost(
    navigationController: NavHostController,
    modifier: Modifier = Modifier
) {
    navigationController.addOnDestinationChangedListener { controller, dest, bundle ->
        Log.d("HappFrontendNavigationHost", "Navigated to ${dest.route}")
    }

    Scaffold { innerPadding ->
        NavHost(
            modifier = modifier.padding(innerPadding),
            navController = navigationController,
            startDestination = HomeDest.route
        ) {
            composable(route = NotificationDest.route) {
                NotificationScreen()
            }
            composable(route = NutritionDest.route) {
                NutritionScreen(onBackClick = { navigationController.popBackStack() })
            }
            composable(route = ActivityDest.route) {
                ActivityScreen(onBackClick = { navigationController.popBackStack() })
            }
            composable(route = LoginDest.route) {
                LoginScreen(
                    onNavigateToRegister = { navigationController.navigate(RegisterDest.route) },
                    onNavigateToHome = { navigationController.navigateAndClear(HomeDest.route) }
                )
            }
            composable(route = RegisterDest.route) {
                RegisterScreen(
                    onNavigateToHome = { navigationController.navigateAndClear(HomeDest.route) },
                    onNavigateToLogin = { navigationController.navigate(LoginDest.route) }
                )
            }
            composable(route = HomeDest.route) {
                HomeScreen(
                    onUnauthorized = {
                        navigationController.navigateAndClear(LoginDest.route)
                    },
                    onNavigateToWeightHistory = {
                        navigationController.navigate(WeightHistoryDest.route)
                    },
                    onNavigateToNutrition = {
                        navigationController.navigate(NutritionDest.route)
                    },
                    onNavigateToActivity = {
                        navigationController.navigate(ActivityDest.route)
                    },
                    onNavigateToNotification = {
                        navigationController.navigate(NotificationDest.route)
                    },
                    onNavigateToSettings = { /* TODO */ },
                    onNavigateToSearch = {
                        navigationController.navigate(SearchDest.route)
                    },
                    onNavigateToUserProfile = {
                        navigationController.navigate(UserInfoDest.route)
                    }
                )
            }
            composable(route = WeightHistoryDest.route) {
                WeightHistoryScreen(
                    onGoBack = {
                        navigationController.popBackStack(
                            route = WeightHistoryDest.route,
                            inclusive = true
                        )
                    },
                    onUnauthorized = {
                        navigationController.navigateAndClear(LoginDest.route)
                    }
                )
            }
            composable(route = SearchDest.route) {
                SearchScreen(
                    onUserClick = { username ->
                        Log.d("Click on search user", username)
                        navigationController.navigate("${UserInfoOtherPersonDest.route}/$username")
                    },
                    onBackClick = {
                        navigationController.popBackStack()
                    },
                    onUnAuth = {
                        Log.d("Unauthorized (from Search Screen)", "JWT expired")
                    }
                )
            }

           composable(route = UserInfoDest.route) {
                UserInfoScreen(
                    onExit = {
                        AuthSharedPreferencesProvider.editor?.clearUserData()
                        navigationController.popBackStack()
                    },
                    onGoBack = {
                    navigationController.popBackStack()}
                  )
           }

            composable(
                route = "${UserInfoOtherPersonDest.route}/{${UserInfoOtherPersonDest.usernameArgument}}",
                arguments = listOf(
                    navArgument(UserInfoOtherPersonDest.usernameArgument) { type = NavType.StringType }
                )
            ) { navBackStackEntry ->
                val username =
                    navBackStackEntry.arguments?.getString(UserInfoOtherPersonDest.usernameArgument) ?: ""
                UserInfoOtherPersonScreen(
                    username,
                    onGoBack = {
                        navigationController.popBackStack()
                    }
                )
            }

           composable(route = NutritionDest.route) {
                NutritionScreen(
                    onBackClick = {
                        navigationController.popBackStack()
                    }
                )
            }
            composable(route = ActivityDest.route) {
                ActivityScreen(
                    onBackClick = {
                        navigationController.popBackStack()
                    }
                )
            }
         }
    }
}

/**
 * Navigates to the destination route and attempts to pop back stack up to the current route
 * (inclusive).
 *
 * @param destinationRoute Destination route to navigate to
 * @author Vad1mChK
 */
fun NavHostController.navigateAndClear(destinationRoute: String) {
    val currentRoute = this.currentDestination?.route
    this.navigate(destinationRoute) {
        currentRoute?.let {
            popUpTo(it) {
                inclusive = true
            }
        }
    }
}