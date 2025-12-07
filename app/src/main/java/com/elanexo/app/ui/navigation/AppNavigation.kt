package com.elanexo.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.elanexo.app.ui.screens.ItemCreateScreen
import com.elanexo.app.ui.screens.ItemDetailScreen
import com.elanexo.app.ui.screens.ItemEditScreen
import com.elanexo.app.ui.screens.ItemListScreen
import com.elanexo.app.viewmodel.ItemViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: ItemViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ItemList.route
    ) {
        composable(Screen.ItemList.route) {
            ItemListScreen(
                viewModel = viewModel,
                onItemClick = { itemId ->
                    navController.navigate(Screen.ItemDetail.createRoute(itemId))
                },
                onAddClick = {
                    navController.navigate(Screen.ItemCreate.route)
                }
            )
        }
        
        composable(
            route = Screen.ItemDetail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: return@composable
            ItemDetailScreen(
                viewModel = viewModel,
                itemId = itemId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { navController.navigate(Screen.ItemEdit.createRoute(itemId)) }
            )
        }
        
        composable(Screen.ItemCreate.route) {
            ItemCreateScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.ItemEdit.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: return@composable
            ItemEditScreen(
                viewModel = viewModel,
                itemId = itemId,
                onBackClick = { navController.popBackStack() },
                onSaveSuccess = { navController.popBackStack() }
            )
        }
    }
}
