package com.example.happ_frontend.model.weight

/**
 * A value class representing a mass in kilograms.
 *
 * @property kg The mass in kilograms.
 * @author Vad1mChK
 */
@JvmInline
value class Mass(val kg: Float) {
    /**
     * @return The mass in kilograms.
     * @author Vad1mChK
     */
    fun kg(): Float = kg

    /**
     * Returns a string representation of the mass in kilograms.
     *
     * @return A string representation of the mass in the format "[kg] kg".
     * @author Vad1mChK
     */
    override fun toString(): String {
        return "$kg kg"
    }

    /**
     * Adds two masses together.
     *
     * @param other The mass to add.
     * @return A new Mass instance representing the sum of the two masses.
     * @author Vad1mChK
     */
    operator fun plus(other: Mass): Mass =
        Mass(this.kg + other.kg)

    /**
     * Subtracts a mass from another mass.
     *
     * @param other The mass to subtract.
     * @return A new Mass instance representing the difference between the two masses.
     * @author Vad1mChK
     */
    operator fun minus(other: Mass): Mass =
        Mass(this.kg - other.kg)
}

/**
 * Extension property to convert a Number to a Mass instance.
 *
 * @receiver The Number to convert.
 * @return A new Mass instance representing the same mass as the Number in kilograms.
 * @author Vad1mChK
 */
val Number.kg get() = Mass(this.toFloat())