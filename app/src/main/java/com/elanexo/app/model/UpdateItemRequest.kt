package com.elanexo.app.model

data class UpdateItemRequest(
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
