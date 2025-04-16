package com.example.happ_frontend.ui.screens.login_register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.enums.enumEntries

@OptIn(ExperimentalLayoutApi::class)
@Composable
inline fun <reified E : Enum<E>> AuthFormEnumChipSelect(
    value: E,
    crossinline onValueChange: (E) -> Unit,
    labelText: String,
    nameMap: Map<E, String>,
    enabled: Boolean = true
) {
    Column {
        Text(labelText)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            enumEntries<E>().forEach { entry ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    InputChip(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        selected = value == entry,
                        onClick = { onValueChange(entry) },
                        label = {
                            Text(
                                text = nameMap[entry] ?: entry.name,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxWidth()
                            )
                        },
                        enabled = enabled
                    )
                }

            }
        }
    }
}