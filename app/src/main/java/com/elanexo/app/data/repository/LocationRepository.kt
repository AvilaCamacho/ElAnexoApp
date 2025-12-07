package com.elanexo.app.data.repository

import android.annotation.SuppressLint
import android.content.Context
import com.elanexo.app.data.model.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.tasks.await

/**
 * Repository: LocationRepository
 * Handles GPS location data operations
 * Part of the MVVM architecture
 */
class LocationRepository(context: Context) {
    
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    
    /**
     * Get current GPS location
     * Requires location permissions to be granted
     */
    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Result<Location> {
        return try {
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).await()
            
            if (location != null) {
                Result.success(
                    Location(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        accuracy = location.accuracy,
                        timestamp = location.time
                    )
                )
            } else {
                Result.failure(Exception("Location unavailable"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
