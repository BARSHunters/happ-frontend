package com.example.happ_frontend.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.happ_frontend.R
import com.example.happ_frontend.ui.domain.login_register.AuthViewModel
import com.example.happ_frontend.ui.navigation.LoginDest
import com.example.happ_frontend.ui.navigation.NotificationDest
import com.example.happ_frontend.ui.navigation.WeightHistoryDest

/**
 * Composable function that represents the main screen of the application.
 * It displays the main layout and content for the home screen.
 * @author Vad1mChK
 */
@Composable
fun HomeScreen(
    navigationController: NavHostController? = null,
    viewModel: AuthViewModel = viewModel()
) {
    val profileState by viewModel.profileState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkAuth()
    }

    when (val state = profileState) {
        is AuthViewModel.ProfileState.Success -> {
            Log.d("HomeScreen", "Success")
            HomeScreenContent(state.username, navigationController)
        }
        is AuthViewModel.ProfileState.Error -> {
            LaunchedEffect(state) {
                Log.d("HomeScreen", "Error: ${state.message}")
                navigationController?.navigate(LoginDest.route) {
                    popUpTo(0)
                }
            }
        }
        is AuthViewModel.ProfileState.Loading -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 64.dp, horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    username: String,
    navigationController: NavHostController?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 64.dp, horizontal = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                HomeIconButton(
                    size = 40.dp,
                    cornerRadius = 12.dp,
                    imageVector = Icons.Outlined.Notifications,
                    onClick = { navigationController?.navigate(NotificationDest.route) }
                )
                HomeIconButton(
                    size = 40.dp,
                    cornerRadius = 12.dp,
                    imageVector = Icons.Outlined.Settings
                )
                HomeIconButton(
                    size = 40.dp,
                    cornerRadius = 12.dp,
                    imageVector = Icons.Outlined.Search
                )
            }
            ProfileChip(username, "Blaze the Cat") // TODO fetch userData from server or smth and place username here
        }

        Text(
            stringResource(R.string.health_category_title),
            fontWeight = FontWeight.Black,
        )

        HorizontalDivider()

        HealthCategoryWidget(
            stringResource(R.string.health_category_weight_title),
            stringResource(R.string.health_category_weight_description),
            Icons.Default.Accessibility,
            clickable = true,
            onClickSeeMore = {
                Log.d("HomeScreen, HealthCategoryWidget (weight)", "onClickSeeMore")
                navigationController?.navigate(WeightHistoryDest.route)
            }
        )

        HealthCategoryWidget(
            stringResource(R.string.health_category_activity_title),
            stringResource(R.string.health_category_activity_description),
            Icons.Default.LocalFireDepartment,
            clickable = true,
            onClickSeeMore = {
                Log.d("HomeScreen, HealthCategoryWidget (activity)", "onClickSeeMore")
            }
        )

        HealthCategoryWidget(
            stringResource(R.string.health_category_nutrition_title),
            stringResource(R.string.health_category_nutrition_description),
            Icons.Default.SwapHoriz,
            clickable = true,
            onClickSeeMore = {
                Log.d("HomeScreen, HealthCategoryWidget (nutrition)", "onClickSeeMore")
            }
        )
    }
}