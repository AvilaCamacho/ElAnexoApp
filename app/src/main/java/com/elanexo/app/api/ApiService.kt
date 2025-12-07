package com.elanexo.app.api

import com.elanexo.app.model.CreateItemRequest
import com.elanexo.app.model.Item
import com.elanexo.app.model.UpdateItemRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // CREATE
    @POST("items")
    suspend fun createItem(@Body request: CreateItemRequest): Response<Item>
    
    // READ - Get all items
    @GET("items")
    suspend fun getAllItems(): Response<List<Item>>
    
    // READ - Get single item
    @GET("items/{id}")
    suspend fun getItem(@Path("id") id: Int): Response<Item>
    
    // UPDATE
    @PUT("items/{id}")
    suspend fun updateItem(
        @Path("id") id: Int,
        @Body request: UpdateItemRequest
    ): Response<Item>
    
    // DELETE
    @DELETE("items/{id}")
    suspend fun deleteItem(@Path("id") id: Int): Response<Unit>
}
