package com.example.happ_frontend.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.happ_frontend.R

/**
 * A composable function that represents a health category widget.
 * It displays a title, description, and an optional icon with a clickable "See More" button.
 *
 * @param title The title of the health category.
 * @param description The description of the health category.
 * @param iconVector An optional icon vector to be displayed in the widget.
 * @param clickable A boolean value indicating whether the "See More" button is clickable.
 * @param onClickSeeMore An optional lambda function that is called when the "See More" button is clicked.
 * @author Vad1mChK
 */
@Composable
fun HealthCategoryWidget(
    title: String,
    description: String,
    iconVector: ImageVector = Icons.Default.LocalFireDepartment,
    clickable: Boolean = false,
    onClickSeeMore: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
//                color = MaterialTheme.colorScheme.primaryContainer,
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite
                ),
                shape = MaterialTheme.shapes.medium
            )
            .padding(32.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.headlineLarge)
        Text(
            description,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth(0.75f)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.End),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (clickable) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .shadow(elevation = 8.dp, clip = false, shape = CircleShape),
                    onClick = onClickSeeMore,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(
                        text = stringResource(R.string.health_category_button_see_more),
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
            Icon(
                imageVector = iconVector,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}