package com.example.happ_frontend.ui.domain.weight

data class Mass(val kg: Float) {
    fun kg(): Float = kg

    override fun toString(): String {
        return "$kg kg"
    }

    operator fun plus(other: Mass): Mass =
        Mass(this.kg + other.kg)

    operator fun minus(other: Mass): Mass =
        Mass(this.kg - other.kg)
}

val Number.kg get() = Mass(this.toFloat())