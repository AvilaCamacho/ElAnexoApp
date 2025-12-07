package com.elanexo.app.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data Model: Product
 * Represents a product item in the inventory system
 */
data class Product(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("title")
    val title: String = "",
    
    @SerializedName("price")
    val price: Double = 0.0,
    
    @SerializedName("description")
    val description: String = "",
    
    @SerializedName("category")
    val category: String = "",
    
    @SerializedName("image")
    val image: String = ""
)
