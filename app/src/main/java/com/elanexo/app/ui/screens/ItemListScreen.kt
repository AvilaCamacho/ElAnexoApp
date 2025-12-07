package com.elanexo.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.elanexo.app.R
import com.elanexo.app.model.Item
import com.elanexo.app.viewmodel.ItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemListScreen(
    viewModel: ItemViewModel,
    onItemClick: (Int) -> Unit,
    onAddClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.items_title)) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_item))
            }
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
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.error ?: stringResource(R.string.error_loading),
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.loadItems() }) {
                            Text("Reintentar")
                        }
                    }
                }
                uiState.items.isEmpty() -> {
                    Text(
                        text = "No hay items. Toca + para agregar uno.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.items) { item ->
                            item.id?.let { itemId ->
                                ItemCard(
                                    item = item,
                                    onClick = { onItemClick(itemId) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCard(
    item: Item,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2
            )
            if (item.latitude != null && item.longitude != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "📍 ${item.latitude}, ${item.longitude}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
