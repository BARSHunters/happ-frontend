package com.example.happ_frontend.ui.screens.weight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PageHeaderWithBackButton(
    title: String,
    onGoBack: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onGoBack) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
            )
        }

        Text(
            textAlign = TextAlign.Center,
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Black
        )

        Spacer(
            modifier = Modifier.size(48.dp)
        )
    }
}