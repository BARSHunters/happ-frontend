package com.example.happ_frontend.ui.screens.weight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.happ_frontend.R

/**
 * A dialog for adding a new weight event to the calendar.
 *
 * @param onCancel A function to be invoked when the user cancels the dialog.
 * @param onSubmit A function to be invoked when the user submits the new weight event.
 * @param submitEnabledCondition A function that returns whether the submit button should be enabled.
 * @param content The content of the dialog.
 *
 * The dialog displays two buttons: "OK" and "Cancel". When the "OK" button is clicked, the [onSubmit] function is invoked.
 * When the "Cancel" button is clicked, the [onCancel] function is invoked. The "OK" button is enabled only when the
 * [submitEnabledCondition] function returns true.
 * @author Vad1mChK
 */
@Composable
fun AddEventDialog(
    onCancel: () -> Unit = {},
    onSubmit: () -> Unit = {},
    submitEnabledCondition: () -> Boolean = { true },
    content: @Composable () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.large
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                content()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = onSubmit,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        enabled = submitEnabledCondition()
                    ) {
                        Text(stringResource(R.string.dialog_misc_buttons_ok).uppercase())
                    }

                    Button(
                        onClick = onCancel,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Text(stringResource(R.string.dialog_misc_buttons_cancel).uppercase())
                    }
                }
            }
        }
    }
}