package com.example.happ_frontend.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.happ_frontend.R

/**
 * A composable function that displays a user's profile chip, including their name and profile picture.
 *
 * @param username The username, used to fetch the profile picture.
 * @param name The display name of the user, shown in the profile chip.
 * @param circleProfilePicture Whether the profile picture should be displayed in a circular shape.
 * @param roundedProfilePicture Whether the profile picture should be displayed with rounded corners.
 * @author Vad1mChK
 */
@Composable
fun ProfileChip(
    username: String,
    name: String,
    circleProfilePicture: Boolean = false,
    roundedProfilePicture: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.wrapContentSize()
    ) {
        Column(
            horizontalAlignment = Alignment.End,
        ) {
            Text(
                stringResource(R.string.user_profile_hi),
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                name,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodySmall
            )
        }
        ProfilePicture(
            username,
            size = 48.dp,
            isCircle = circleProfilePicture,
            isRounded = roundedProfilePicture
        )
    }
}