package com.example.happ_frontend.model.login_register

import kotlin.jvm.Throws

/**
 * Represents a user's weight management goal or desire.
 *
 * This enum defines the possible weight management goals that a user can have:
 * - Weight loss
 * - Maintaining current weight
 * - Weight gain
 *
 * @author Vad1mChK
 */
enum class WeightDesire(val weightControlWishString: String) {
    /**
     * Indicates the user wants to lose weight.
     */
    LOSS("lose"),

    /**
     * Indicates the user wants to maintain their current weight.
     */
    REMAIN("keep"),

    /**
     * Indicates the user wants to gain weight.
     */
    GAIN("gain");

    fun toWeightControlWishString(): String {
        return weightControlWishString
    }

    companion object {
        @JvmStatic
        @Throws(NoSuchElementException::class)
        fun fromWeightControlWishString(wishString: String): WeightDesire {
            return entries.first { it.weightControlWishString == wishString }
        }
    }
}