package com.elanexo.app.ui.navigation

sealed class Screen(val route: String) {
    object ItemList : Screen("item_list")
    object ItemDetail : Screen("item_detail/{itemId}") {
        fun createRoute(itemId: Int) = "item_detail/$itemId"
    }
    object ItemCreate : Screen("item_create")
    object ItemEdit : Screen("item_edit/{itemId}") {
        fun createRoute(itemId: Int) = "item_edit/$itemId"
    }
}
