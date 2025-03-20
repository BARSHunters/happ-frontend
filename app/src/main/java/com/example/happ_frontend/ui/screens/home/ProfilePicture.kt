package com.example.happ_frontend.ui.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.happ_frontend.R

private const val MARK_REGEX_STRING = "\\b(марк|mark)"
private const val MARK_ISU_NUMBER = 368164

/**
 * Displays a profile picture with customizable shape and size.
 *
 * This composable function renders an image that represents a user's profile picture.
 * The image can be displayed in different shapes such as circle, rounded rectangle, or rectangle.
 *
 * @param username The username associated with the profile picture, used for content description.
 * @param modifier A [Modifier] for this composable, allowing for customization of its appearance and layout.
 * @param backgroundColor The background color of the profile picture. Defaults to transparent.
 * @param size The size of the profile picture. Defaults to 64.dp.
 * @param cornerRadius The corner radius for rounded rectangle shapes. Not applied if `isRounded == false`.
 * @param imgRes The drawable resource ID of the image to be displayed. Defaults to an image chosen based on the username.
 * @param isCircle If true, the profile picture is displayed as a circle. Defaults to false.
 * @param isRounded If true, the profile picture is displayed as a rounded rectangle. Defaults to false.
 */
@Composable
fun ProfilePicture(
    username: String,
    @Suppress("UNUSED_PARAMETER") modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    size: Dp = 64.dp,
    cornerRadius: Dp = 8.dp,
    @DrawableRes imgRes: Int = chooseImageForUsername(username),
    isCircle: Boolean = false,
    isRounded: Boolean = false
) {
    val shape = if (isCircle) CircleShape
        else if (isRounded) RoundedCornerShape(cornerRadius)
        else RectangleShape
    Image(
        painter = painterResource(id = imgRes),
        contentDescription = username,
        modifier = Modifier
            .size(size)
            .background(backgroundColor, shape)
            .clip(shape)
    )
}

/**
 * Chooses an image resource ID based on the provided username.
 *
 * This function selects a specific image resource to represent a user based on their username.
 * If the username matches certain criteria, a special image is chosen; otherwise, a default
 * image is selected from a predefined set.
 *
 * @param username The username for which the image is to be selected.
 * @return The drawable resource ID of the selected image.
 */
@DrawableRes
private fun chooseImageForUsername(username: String): Int {
    val markRegex = Regex(MARK_REGEX_STRING, option = RegexOption.IGNORE_CASE)

    if (
        markRegex.containsMatchIn(username) ||
        username.hashCode() % MARK_ISU_NUMBER == 0
    ) return R.drawable.pfp_mark_transparent

    val catImages = arrayOf(
        R.drawable.pfp_cat0,
        R.drawable.pfp_cat1,
        R.drawable.pfp_cat2,
        R.drawable.pfp_cat3,
        R.drawable.pfp_cat4,
        R.drawable.pfp_cat5,
        R.drawable.pfp_cat6,
        R.drawable.pfp_cat7,
        R.drawable.pfp_cat8,
    )
    // Maybe not hardcode references to cat images in the future
    // But without hardcoding we'll have to get the resource id in runtime, which is not optimized

    // use mod instead of %, so that the quotient has the divisor's sign
    val imageIndex = username.hashCode().mod(catImages.size)

    return catImages[imageIndex]
}