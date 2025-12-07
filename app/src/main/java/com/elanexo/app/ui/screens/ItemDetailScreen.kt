package com.elanexo.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.elanexo.app.R
import com.elanexo.app.viewmodel.ItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemDetailScreen(
    viewModel: ItemViewModel,
    itemId: Int,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(itemId) {
        viewModel.loadItem(itemId)
    }
    
    LaunchedEffect(uiState.operationSuccess) {
        if (uiState.operationSuccess) {
            viewModel.clearOperationSuccess()
            onBackClick()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.selectedItem?.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit_item))
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete_item))
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
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    Text(
                        text = uiState.error ?: stringResource(R.string.error_loading),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.selectedItem != null -> {
                    uiState.selectedItem.let { item ->
                        Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = stringResource(R.string.title),
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }
                        }
                        
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = stringResource(R.string.description),
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Text(
                                    text = item.description,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                        
                        if (item.latitude != null && item.longitude != null) {
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Ubicación",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${stringResource(R.string.latitude)}: ${item.latitude}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "${stringResource(R.string.longitude)}: ${item.longitude}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.delete_item)) },
            text = { Text(stringResource(R.string.confirm_delete)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteItem(itemId)
                        showDeleteDialog = false
                    }
                ) {
                    Text(stringResource(R.string.delete_item))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}
