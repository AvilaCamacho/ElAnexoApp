package com.elanexo.app.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.elanexo.app.data.model.Location
import com.elanexo.app.data.repository.LocationRepository
import kotlinx.coroutines.launch

/**
 * ViewModel: LocationViewModel
 * Manages UI-related data for Location/GPS functionality
 * Part of the MVVM architecture
 */
class LocationViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = LocationRepository(application.applicationContext)
    
    private val _location = mutableStateOf<Location?>(null)
    val location: State<Location?> = _location
    
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading
    
    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error
    
    /**
     * Get current GPS location
     */
    fun getCurrentLocation() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.getCurrentLocation().fold(
                onSuccess = { currentLocation ->
                    _location.value = currentLocation
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message
                    _isLoading.value = false
                }
            )
        }
    }
}
