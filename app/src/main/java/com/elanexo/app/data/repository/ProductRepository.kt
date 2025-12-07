package com.elanexo.app.data.repository

import com.elanexo.app.data.model.Product
import com.elanexo.app.data.remote.RetrofitClient

/**
 * Repository: ProductRepository
 * Handles data operations and acts as a single source of truth
 * Part of the MVVM architecture
 */
class ProductRepository {
    
    private val apiService = RetrofitClient.apiService
    
    /**
     * GET: Fetch all products from API
     */
    suspend fun getProducts(): Result<List<Product>> {
        return try {
            val products = apiService.getProducts()
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * GET: Fetch a single product by ID
     */
    suspend fun getProduct(id: Int): Result<Product> {
        return try {
            val product = apiService.getProduct(id)
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * POST: Create a new product
     */
    suspend fun createProduct(product: Product): Result<Product> {
        return try {
            val createdProduct = apiService.createProduct(product)
            Result.success(createdProduct)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * PUT: Update an existing product
     */
    suspend fun updateProduct(id: Int, product: Product): Result<Product> {
        return try {
            val updatedProduct = apiService.updateProduct(id, product)
            Result.success(updatedProduct)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * DELETE: Remove a product
     */
    suspend fun deleteProduct(id: Int): Result<Product> {
        return try {
            val deletedProduct = apiService.deleteProduct(id)
            Result.success(deletedProduct)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
