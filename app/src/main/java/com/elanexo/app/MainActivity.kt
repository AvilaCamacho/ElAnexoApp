package com.elanexo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.elanexo.app.api.RetrofitClient
import com.elanexo.app.repository.ItemRepository
import com.elanexo.app.ui.navigation.AppNavigation
import com.elanexo.app.ui.theme.ElAnexoAppTheme
import com.elanexo.app.viewmodel.ItemViewModel

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: ItemViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize repository and viewmodel
        val repository = ItemRepository(RetrofitClient.apiService)
        viewModel = ItemViewModel(repository)
        
        setContent {
            ElAnexoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
