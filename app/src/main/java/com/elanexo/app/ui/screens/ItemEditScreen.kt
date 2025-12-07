package com.elanexo.app.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.elanexo.app.R
import com.elanexo.app.model.UpdateItemRequest
import com.elanexo.app.utils.LocationHelper
import com.elanexo.app.utils.PermissionUtils
import com.elanexo.app.viewmodel.ItemViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemEditScreen(
    viewModel: ItemViewModel,
    itemId: Int,
    onBackClick: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var initialized by remember { mutableStateOf(false) }
    
    val locationHelper = remember { LocationHelper(context) }
    
    LaunchedEffect(itemId) {
        viewModel.loadItem(itemId)
    }
    
    LaunchedEffect(uiState.selectedItem) {
        if (!initialized && uiState.selectedItem != null) {
            val item = uiState.selectedItem!!
            title = item.title
            description = item.description
            latitude = item.latitude
            longitude = item.longitude
            initialized = true
        }
    }
    
    LaunchedEffect(uiState.operationSuccess) {
        if (uiState.operationSuccess) {
            viewModel.clearOperationSuccess()
            onSaveSuccess()
        }
    }
    
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            scope.launch {
                val location = locationHelper.getCurrentLocation()
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                }
            }
        }
    }
    
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            // Camera permission granted - in a real app, open camera here
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_item)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                !initialized && uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text(stringResource(R.string.title)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text(stringResource(R.string.description)) },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    if (PermissionUtils.hasCameraPermission(context)) {
                                        // Camera already permitted
                                    } else {
                                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(R.string.take_photo))
                            }
                            
                            OutlinedButton(
                                onClick = {
                                    if (PermissionUtils.hasLocationPermissions(context)) {
                                        scope.launch {
                                            val location = locationHelper.getCurrentLocation()
                                            if (location != null) {
                                                latitude = location.latitude
                                                longitude = location.longitude
                                            }
                                        }
                                    } else {
                                        locationPermissionLauncher.launch(PermissionUtils.LOCATION_PERMISSIONS)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.LocationOn, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(R.string.get_location))
                            }
                        }
                        
                        if (latitude != null && longitude != null) {
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Ubicación:",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Text(
                                        text = "${stringResource(R.string.latitude)}: $latitude",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "${stringResource(R.string.longitude)}: $longitude",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                        
                        if (uiState.error != null) {
                            Text(
                                text = uiState.error ?: stringResource(R.string.error_saving),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        Button(
                            onClick = {
                                val request = UpdateItemRequest(
                                    title = title,
                                    description = description,
                                    latitude = latitude,
                                    longitude = longitude
                                )
                                viewModel.updateItem(itemId, request)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = title.isNotBlank() && description.isNotBlank() && !uiState.isLoading
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text(stringResource(R.string.save))
                            }
                        }
                    }
                }
            }
        }
    }
}
