package com.elanexo.app.repository

import com.elanexo.app.api.ApiService
import com.elanexo.app.model.CreateItemRequest
import com.elanexo.app.model.Item
import com.elanexo.app.model.UpdateItemRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

class ItemRepository(private val apiService: ApiService) {
    
    suspend fun getAllItems(): Result<List<Item>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllItems()
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error al cargar items: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Error de red: ${e.message}")
        }
    }
    
    suspend fun getItem(id: Int): Result<Item> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getItem(id)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error al cargar item: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Error de red: ${e.message}")
        }
    }
    
    suspend fun createItem(request: CreateItemRequest): Result<Item> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createItem(request)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error al crear item: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Error de red: ${e.message}")
        }
    }
    
    suspend fun updateItem(id: Int, request: UpdateItemRequest): Result<Item> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.updateItem(id, request)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error al actualizar item: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Error de red: ${e.message}")
        }
    }
    
    suspend fun deleteItem(id: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.deleteItem(id)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Error al eliminar item: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Error de red: ${e.message}")
        }
    }
}
