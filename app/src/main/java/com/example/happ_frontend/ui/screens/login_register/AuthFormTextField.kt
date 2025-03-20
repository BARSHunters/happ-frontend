package com.example.happ_frontend.ui.screens.login_register

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.happ_frontend.ui.domain.login_register.LoginRegisterValidationResult
import com.example.happ_frontend.ui.domain.login_register.LoginRegisterValidator

/**
 * Composable function that represents a form field for inputting text.
 * It includes features such as a label, leading icon, trailing icon for censoring, and validation.
 *
 * @param value The current value of the text field.
 * @param onValueChange A lambda function that is called when the value of the text field changes.
 * @param labelText The text displayed as the label for the text field.
 * @param censorable A boolean value that determines whether the text field has a trailing icon for censoring.
 * @param censored A boolean value that determines whether the text in the text field is censored.
 * @param onCensoredChange A lambda function that is called when the state of censoring changes.
 * @param leadingIconVector An optional vector image that is displayed as the leading icon in the text field.
 * @param validator An optional validator that validates the input value and provides a validation result.
 * @author Vad1mChK
 */
@Composable
internal fun AuthFormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    censorable: Boolean = false,
    censored: Boolean = false,
    onCensoredChange: (Boolean) -> Unit = { _ -> },
    leadingIconVector: ImageVector? = null,
    validator: LoginRegisterValidator<String>? = null
) {
    val validationResult = validator?.validate(value)
    val validationErrorMessage = if (validationResult is LoginRegisterValidationResult.Failure) {
        stringResource(validationResult.errorResId, *validationResult.formatArgs)
    } else null

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                labelText,
                color = MaterialTheme.colorScheme.primary
            )
        },
        leadingIcon = leadingIconVector?.let {
            {
                Icon(leadingIconVector, contentDescription = null)
            }
        },
        trailingIcon = {
            if (censorable) {
                Icon(
                    imageVector = if (censored) {
                        Icons.Filled.Visibility
                    } else {
                        Icons.Filled.VisibilityOff
                    },
                    contentDescription = null,
                    modifier = Modifier.clickable { onCensoredChange(!censored) }
                )
            }
        },
        visualTransformation = if (censored) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
        ),
        isError = validationResult is LoginRegisterValidationResult.Failure,
        supportingText = {
            if (validationErrorMessage != null) {
                Text(validationErrorMessage, color = MaterialTheme.colorScheme.error)
            }
        }
    )
}