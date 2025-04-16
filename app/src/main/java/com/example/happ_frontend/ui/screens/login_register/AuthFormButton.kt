package com.example.happ_frontend.ui.screens.login_register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.happ_frontend.ui.theme.Typography

@Composable
internal fun AuthFormButton(
    text: String,
    onClick: () -> Unit = {},
    // enabledCondition: () -> Boolean = { true }
    enabled: Boolean = true,
    loading: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(6.dp),
        // enabled = enabledCondition()
        enabled = enabled
    ) {
        Column {
            if (loading) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = text.uppercase(),
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    style = Typography.bodyLarge
                )
            }
        }
    }
}