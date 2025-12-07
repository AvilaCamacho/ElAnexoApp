package com.elanexo.app.data.model

/**
 * Data Model: Location
 * Represents GPS location data
 */
data class Location(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val accuracy: Float = 0f,
    val timestamp: Long = System.currentTimeMillis()
)
