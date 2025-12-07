package com.elanexo.app.ui.screens

import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.elanexo.app.ui.viewmodel.LocationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.text.SimpleDateFormat
import java.util.*

/**
 * View: LocationScreen
 * Displays GPS location data using device sensors
 * Part of the MVVM architecture - this is the View layer
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun LocationScreen(viewModel: LocationViewModel) {
    val location by viewModel.location
    val isLoading by viewModel.isLoading
    val error by viewModel.error
    
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ubicación GPS") },
                actions = {
                    if (locationPermissionsState.allPermissionsGranted) {
                        IconButton(onClick = { viewModel.getCurrentLocation() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Actualizar ubicación")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            when {
                !locationPermissionsState.allPermissionsGranted -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Se requiere permiso de ubicación para usar esta función",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
                            Text("Otorgar Permiso")
                        }
                    }
                }
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: $error")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.getCurrentLocation() }) {
                            Text("Reintentar")
                        }
                    }
                }
                location != null -> {
                    LocationInfo(location = location!!)
                }
                else -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Presiona el botón para obtener tu ubicación")
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.getCurrentLocation() }) {
                            Text("Obtener Ubicación")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocationInfo(location: com.elanexo.app.data.model.Location) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Tu Ubicación Actual",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            Divider()
            
            InfoRow(label = "Latitud", value = String.format("%.6f", location.latitude))
            InfoRow(label = "Longitud", value = String.format("%.6f", location.longitude))
            InfoRow(label = "Precisión", value = String.format("%.2f metros", location.accuracy))
            
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val timestamp = dateFormat.format(Date(location.timestamp))
            InfoRow(label = "Hora", value = timestamp)
            
            Divider()
            
            Text(
                text = "Coordenadas: ${location.latitude}, ${location.longitude}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
