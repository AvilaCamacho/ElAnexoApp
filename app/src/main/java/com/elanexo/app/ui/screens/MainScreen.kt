package com.elanexo.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elanexo.app.ui.viewmodel.LocationViewModel
import com.elanexo.app.ui.viewmodel.ProductViewModel

/**
 * MainScreen
 * Main navigation screen with tabs for Products and Location
 */
@Composable
fun MainScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    
    val productViewModel: ProductViewModel = viewModel()
    val locationViewModel: LocationViewModel = viewModel()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Productos") },
                    label = { Text("Productos") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.LocationOn, contentDescription = "Ubicación") },
                    label = { Text("Ubicación") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> ProductsScreen(viewModel = productViewModel)
                1 -> LocationScreen(viewModel = locationViewModel)
            }
        }
    }
}
