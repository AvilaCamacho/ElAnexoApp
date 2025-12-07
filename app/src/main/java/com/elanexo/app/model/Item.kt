package com.elanexo.app.model

data class Item(
    val id: Int? = null,
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
