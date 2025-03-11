package com.example.happ_frontend.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AuthFormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    censored: Boolean = false,
    leadingIconVector: ImageVector? = null,
    validationRegex: Regex? = null
) {
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
        isError = validationRegex != null && validationRegex.matchEntire(value) == null
    )
}