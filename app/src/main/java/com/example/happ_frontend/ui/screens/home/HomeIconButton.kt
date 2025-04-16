package com.example.happ_frontend.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalAirport
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A composable function that creates a customizable icon button.
 *
 * @param size The size of the icon button.
 * @param cornerRadius The corner radius of the icon button.
 * @param iconSize The size of the icon. If not provided, it defaults to the size of the icon button.
 * @param imageVector The image vector to be displayed in the icon button.
 * @param onClick The onClick function to be triggered when the icon button is clicked.
 *
 * @author Vad1mChK
 */
@Composable
fun HomeIconButton(
    size: Dp = 64.dp,
    cornerRadius: Dp = 8.dp,
    iconSize: Dp? = null,
    imageVector: ImageVector = Icons.Outlined.LocalAirport,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                imageVector,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = iconSize?.let { Modifier.size(it) } ?: Modifier
            )
        },
        modifier = Modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
            )
    )
}