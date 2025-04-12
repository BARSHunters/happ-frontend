package com.example.happ_frontend.ui.screens.weight

import AuthFormDatePicker
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
import com.example.happ_frontend.ui.domain.login_register.AuthValidator
import com.example.happ_frontend.ui.domain.weight.WeightHistoryFormData
import com.example.happ_frontend.ui.domain.weight.WeightHistoryViewModel
import com.example.happ_frontend.ui.screens.login_register.AuthFormTextField
import com.example.happ_frontend.ui.screens.login_register.AuthFormTimePicker

/**
 * A composable function that displays a dialog for adding a new weight entry.
 *
 * The dialog collects and validates user input for date, time, and weight. It uses the provided [onCancel] and [onSubmit]
 * lambda functions to handle user interactions. The [viewModel] parameter is used to access the dialog's state and logic.
 *
 * The dialog is built using the provided [AddEventDialog] composable, which takes care of the common dialog layout.
 *
 * The dialog's content includes a title, date picker, time picker, and weight input field.
 *
 * The dialog's submit button is enabled only when the `submitEnabledCondition` lambda function returns true.
 *
 * @param onCancel A lambda function to be invoked when the user cancels the dialog.
 * @param onSubmit A lambda function to be invoked when the user submits the dialog.
 * @param viewModel The [WeightHistoryViewModel] instance to be used for managing the dialog's state and logic.
 * @author Vad1mChK
 */
@Composable
fun WeightAddEventDialog(
    onCancel: () -> Unit = {},
    onSubmit: () -> Unit = {},
    viewModel: WeightHistoryViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    val weightValidator by remember {
        mutableStateOf(AuthValidator.NumberInputValidator(
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
            validator = AuthValidator.NumberInputValidator(
                min = WeightHistoryFormData.MIN_WEIGHT_KG,
                max = WeightHistoryFormData.MAX_WEIGHT_KG,
                precision = 1
            )
        )
    }
}