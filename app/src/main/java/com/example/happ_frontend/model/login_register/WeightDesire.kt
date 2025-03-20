package com.example.happ_frontend.model.login_register

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
enum class WeightDesire {
    /**
     * Indicates the user wants to lose weight.
     */
    LOSS,
    
    /**
     * Indicates the user wants to maintain their current weight.
     */
    REMAIN,
    
    /**
     * Indicates the user wants to gain weight.
     */
    GAIN,
}