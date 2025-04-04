package com.example.happ_frontend.ui.domain.login_register

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant as KInstant
import kotlinx.datetime.LocalDate as KLocalDate
import kotlinx.datetime.LocalDateTime as KLocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Converts a [Float] number to string using the specified precision.
 * @param precision the number of fractional digits
 * @return the string representation of the number
 * @author Vad1mChK
 */
fun Float.format(precision: Int): String =
    try {
        BigDecimal(this.toDouble())
            .setScale(precision, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
    } catch (e: Exception) {
        "0.0"
    }

/**
 * Retrieves the current date as a [LocalDate] object.
 *
 * Provides the same functionality as `java.time.LocalDate.now()`, in case the latter is unavailable.
 *
 * @return The current date as a [LocalDate] object.
 * @author Vad1mChK
 */
fun KLocalDate.Companion.now(): KLocalDate {
    return KLocalDateTime.now().date
}

/**
 * Retrieves the current datetime as a [LocalDateTime] object.
 *
 * Provides the same functionality as `java.time.LocalDateTime.now()`, in case the latter is unavailable.
 *
 * @return The current date as a [LocalDateTime] object.
 * @author Vad1mChK
 */
fun KLocalDateTime.Companion.now(): KLocalDateTime {
    val now = Clock.System.now()
    val tz = TimeZone.currentSystemDefault()
    return now.toLocalDateTime(tz)
}

// Extension functions for date conversion
fun KLocalDate.toEpochMilliseconds() = this.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()

fun fromEpochMilliseconds(milliseconds: Long): KLocalDate =
    KInstant.fromEpochMilliseconds(milliseconds).toLocalDateTime(TimeZone.UTC).date

/**
 * Creates a map of enum values to their corresponding localized string resources.
 *
 * This function generates a map where each key is an enum value and the corresponding value
 * is either a localized string from the resources (if found) or the enum name itself.
 *
 * @param E The enum type for which to create the name map.
 * @param context The Android context used to access string resources.
 * @param prefix The prefix to be added to the resource name. By default, it's the enum class name
 *               converted from PascalCase to snake_case.
 * @param separator The separator used between the prefix and the enum name in the resource name.
 *                  Default is "_".
 * @return A map where keys are enum values and values are their corresponding localized strings
 *         or enum names if no matching resource is found.
 *
 * @suppress("DiscouragedApi") This function uses discouraged API `Resources.getIdentifier`
 *         (may be fixed in the future).
 * @author Vad1mChK
 */
@SuppressLint("DiscouragedApi")
inline fun <reified E : Enum<E>> getNameMap(
    context: Context,
    prefix: String = E::class.simpleName?.convertNamingConvention(
        NamingConvention.PASCAL_CASE, NamingConvention.SNAKE_CASE
    ) ?: "",
    separator: String = "_"
): Map<E, String> {
    return enumValues<E>().associateWith { enum ->
        val resName = "${prefix}${separator}${enum.name.lowercase()}"
        val resId = context.resources.getIdentifier(
            resName,
            "string",
            context.packageName
        ) // Perhaps a better solution for getting a resource by name
        if (resId != 0) context.getString(resId) else enum.name
    }
}


/**
 * Represents different naming conventions used in programming.
 *
 * This enum class defines various common naming conventions,
 * allowing for easy reference and conversion between different styles.
 * @author Vad1mChK
 */
enum class NamingConvention {
    /**
     * Represents the snake_case naming convention.
     * Words are separated by underscores and all characters are lowercase.
     * Example: "hello_world"
     * @author Vad1mChK
     */
    SNAKE_CASE,

    /**
     * Represents the camelCase naming convention.
     * Words are joined without separators, with the first word in lowercase and subsequent words capitalized.
     * Example: "helloWorld"
     * @author Vad1mChK
     */
    CAMEL_CASE,

    /**
     * Represents the PascalCase naming convention.
     * Words are joined without separators, with each word capitalized.
     * Example: "HelloWorld"
     * @author Vad1mChK
     */
    PASCAL_CASE,

    /**
     * Represents the SCREAMING_SNAKE_CASE naming convention.
     * Words are separated by underscores and all characters are uppercase.
     * Example: "HELLO_WORLD"
     * @author Vad1mChK
     */
    SCREAMING_SNAKE_CASE
}

/**
 * Converts a string from one naming convention to another.
 *
 * This function takes a string in a specified naming convention and converts it to another
 * specified naming convention.
 *
 * @param from The [NamingConvention] of the input string.
 * @param to The target [NamingConvention] to convert the string to.
 * @return A new string converted to the target naming convention.
 * @author Vad1mChK
 */
fun String.convertNamingConvention(from: NamingConvention, to: NamingConvention): String {
    val words = when (from) {
        NamingConvention.SNAKE_CASE, NamingConvention.SCREAMING_SNAKE_CASE -> this.split('_')
        NamingConvention.CAMEL_CASE, NamingConvention.PASCAL_CASE ->
            this.split("(?<!^)(?=[A-Z])".toRegex())
    }

    return when (to) {
        NamingConvention.SNAKE_CASE ->
            words.joinToString("_") { it.lowercase() }

        NamingConvention.SCREAMING_SNAKE_CASE ->
            words.joinToString("_") { it.uppercase() }

        NamingConvention.CAMEL_CASE ->
            words.mapIndexed { index, word ->
                if (index == 0) word.lowercase()
                else word.lowercase().capitalize()
            }.joinToString("")

        NamingConvention.PASCAL_CASE ->
            words.joinToString("") { it.lowercase().capitalize() }
    }
}

/**
 * Capitalizes the first letter of the given string using the [uppercase] method and returns
 * the transformed string.
 * @returns the capitalized string.
 * @author Vad1mChK
 */
fun String.capitalize(): String {
    return if (this.isNotEmpty()) this[0].uppercase() + this.substring(1) else this
}