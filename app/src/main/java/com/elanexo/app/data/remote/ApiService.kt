package com.elanexo.app.data.remote

import com.elanexo.app.data.model.Product
import retrofit2.http.*

/**
 * API Service Interface
 * Defines all REST API endpoints using Retrofit
 * Using JSONPlaceholder Fake Store API for demonstration
 */
interface ApiService {
    
    /**
     * GET: Retrieve all products
     */
    @GET("products")
    suspend fun getProducts(): List<Product>
    
    /**
     * GET: Retrieve a single product by ID
     */
    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Product
    
    /**
     * POST: Create a new product
     */
    @POST("products")
    suspend fun createProduct(@Body product: Product): Product
    
    /**
     * PUT: Update an existing product
     */
    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Product
    
    /**
     * DELETE: Remove a product
     */
    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Product
}
