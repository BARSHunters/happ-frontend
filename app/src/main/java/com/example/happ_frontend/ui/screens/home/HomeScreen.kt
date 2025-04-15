package com.example.happ_frontend.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import com.example.happ_frontend.R
import com.example.happ_frontend.ui.AppViewModelProvider
import com.example.happ_frontend.ui.domain.login_register.AuthViewModel

/**
 * Composable function that represents the main screen of the application.
 * It displays the main layout and content for the home screen.
 * @author Vad1mChK
 */
@Composable
fun HomeScreen(
    viewModel: AuthViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onUnauthorized: () -> Unit = {},
    onNavigateToWeightHistory: () -> Unit = {},
    onNavigateToNutrition: () -> Unit = {},
    onNavigateToActivity: () -> Unit = {},
    onNavigateToNotification: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToUserProfile: () -> Unit = {},
) {
    val profileState by viewModel.profileState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkAuth()
    }

    when (val state = profileState) {
        is AuthViewModel.ProfileState.Success -> {
            Log.d("HomeScreen", "Success")
            HomeScreenContent(
                viewModel,
                onNavigateToWeightHistory = onNavigateToWeightHistory,
                onNavigateToNutrition = onNavigateToNutrition,
                onNavigateToActivity = onNavigateToActivity,
                onNavigateToNotification = onNavigateToNotification,
                onNavigateToSettings = onNavigateToSettings,
                onNavigateToSearch = onNavigateToSearch,
                onNavigateToUserProfile = onNavigateToUserProfile,
            )
        }
        is AuthViewModel.ProfileState.Error -> {
            LaunchedEffect(state) {
                Log.d("HomeScreen", "Error loading home page: ${state.fallbackMessage}")
            }
            if (state.isUnauthorizedError) {
                onUnauthorized()
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        stringResource(
                            id = state.messageResId,
                            formatArgs = state.formatArgs.toTypedArray()
                        )
                    )
                    Button(
                        onClick = viewModel::checkAuth,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.dialog_misc_buttons_retry).uppercase())
                    }
                    Button(
                        onClick = viewModel::logoutUser,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text(stringResource(R.string.auth_button_logout).uppercase())
                    }
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
    viewModel: AuthViewModel,
    onNavigateToWeightHistory: () -> Unit,
    onNavigateToNutrition: () -> Unit,
    onNavigateToActivity: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToUserProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = PaddingValues(
                start = 32.dp,
                end = 32.dp,
                top = 32.dp,
            )),
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
                    onClick = onNavigateToNotification
                )
                HomeIconButton(
                    size = 40.dp,
                    cornerRadius = 12.dp,
                    imageVector = Icons.Outlined.Settings,
                    onClick = onNavigateToSettings
                )
                HomeIconButton(
                    size = 40.dp,
                    cornerRadius = 12.dp,
                    imageVector = Icons.Outlined.Search,
                    onClick = onNavigateToSearch
                )
            }
            ProfileChip(
                viewModel.username,
                viewModel.name,
                onClick = onNavigateToUserProfile
            )
        }

        Text(
            stringResource(R.string.health_category_title),
            fontWeight = FontWeight.Black,
        )

        HorizontalDivider()

        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            HealthCategoryWidget(
                stringResource(R.string.health_category_weight_title),
                stringResource(R.string.health_category_weight_description),
                Icons.Default.Accessibility,
                clickable = true,
                onClickSeeMore = {
                    Log.d("HomeScreen, HealthCategoryWidget (weight)", "onClickSeeMore")
                    onNavigateToWeightHistory()
                }
            )

            HealthCategoryWidget(
                stringResource(R.string.health_category_activity_title),
                stringResource(R.string.health_category_activity_description),
                Icons.Default.LocalFireDepartment,
                clickable = true,
                onClickSeeMore = {
                    Log.d("HomeScreen, HealthCategoryWidget (activity)", "onClickSeeMore")
                    onNavigateToActivity()
                }
            )

            HealthCategoryWidget(
                stringResource(R.string.health_category_nutrition_title),
                stringResource(R.string.health_category_nutrition_description),
                Icons.Default.SwapHoriz,
                clickable = true,
                onClickSeeMore = {
                    Log.d("HomeScreen, HealthCategoryWidget (nutrition)", "onClickSeeMore")
                    onNavigateToNutrition()
                }
            )
        }
    }
}