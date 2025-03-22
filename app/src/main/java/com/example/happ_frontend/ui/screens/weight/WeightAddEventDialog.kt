package com.example.happ_frontend.ui.screens.weight

import AuthFormDatePicker
import AuthFormNumberField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.happ_frontend.R
import com.example.happ_frontend.ui.domain.login_register.LoginRegisterValidator
import com.example.happ_frontend.ui.domain.login_register.LoginRegisterViewModel
import com.example.happ_frontend.ui.domain.weight.WeightHistoryFormData
import com.example.happ_frontend.ui.domain.weight.WeightHistoryViewModel
import com.example.happ_frontend.ui.screens.login_register.AuthFormTextField
import com.example.happ_frontend.ui.screens.login_register.AuthFormTimePicker

@Composable
fun WeightAddEventDialog(
    onCancel: () -> Unit = {},
    onSubmit: () -> Unit = {},
    viewModel: WeightHistoryViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    val weightValidator by remember {
        mutableStateOf(LoginRegisterValidator.NumberInputValidator(
            min = WeightHistoryFormData.MIN_WEIGHT_KG,
            max = WeightHistoryFormData.MAX_WEIGHT_KG,
            precision = 1
        ))
    }

    AddEventDialog(
        onCancel = onCancel,
        onSubmit = onSubmit,
        submitEnabledCondition = { viewModel.validateAddEventDialog() }
    ) {
        Text(
            stringResource(R.string.health_category_weight_dialog_title),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Black
        )

        AuthFormDatePicker(
            labelText = stringResource(R.string.health_category_weight_dialog_field_date),
            value = uiState.entryDate,
            onValueChange = {
                it?.let { newDate -> viewModel.entryDate = newDate }
            },
        )

        AuthFormTimePicker(
            labelText = stringResource(R.string.health_category_weight_dialog_field_time),
            value = uiState.entryTime,
            onValueChange = {
                it?.let { newTime -> viewModel.entryTime = newTime }
            },
        )

        AuthFormTextField(
            labelText = stringResource(R.string.health_category_weight_dialog_field_weight),
            value = uiState.entryWeightKgString,
            onValueChange = {
                viewModel.entryWeightKgString = it
            },
            validator = LoginRegisterValidator.NumberInputValidator(
                min = WeightHistoryFormData.MIN_WEIGHT_KG,
                max = WeightHistoryFormData.MAX_WEIGHT_KG,
                precision = 1
            )
        )
    }
}