package com.elanexo.app.data.remote

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Data class que representa un mensaje de audio
 */
data class AudioMessage(
    val id: Int,
    val sender: String,
    val recipient: String,
    val filename: String,
    val duration: Float?,
    val file_size: Int?,
    val created_at: String?
)

/**
 * Respuesta genérica para DELETE
 */
data class DeleteResponse(
    val message: String
)

/**
 * Interface Retrofit para la API de AudioMessages
 * 
 * Endpoints disponibles:
 * - GET /messages - Lista mensajes (con filtros opcionales)
 * - GET /messages/{id} - Obtiene mensaje específico
 * - POST /messages - Crea nuevo mensaje (multipart)
 * - PUT /messages/{id} - Actualiza mensaje (multipart)
 * - DELETE /messages/{id} - Elimina mensaje
 * - GET /media/{filename} - Descarga archivo de audio
 */
interface ApiService {
    
    /**
     * Obtiene lista de mensajes de audio
     * @param sender Filtro opcional por remitente
     * @param recipient Filtro opcional por destinatario
     */
    @GET("messages")
    fun getMessages(
        @Query("sender") sender: String? = null,
        @Query("recipient") recipient: String? = null
    ): Call<List<AudioMessage>>
    
    /**
     * Obtiene un mensaje específico por ID
     * @param messageId ID del mensaje
     */
    @GET("messages/{id}")
    fun getMessage(
        @Path("id") messageId: Int
    ): Call<AudioMessage>
    
    /**
     * Crea un nuevo mensaje de audio (multipart/form-data)
     * @param sender Nombre del remitente
     * @param recipient Nombre del destinatario
     * @param audioFile Archivo de audio
     * @param duration Duración en segundos (opcional)
     */
    @Multipart
    @POST("messages")
    fun createMessage(
        @Part("sender") sender: RequestBody,
        @Part("recipient") recipient: RequestBody,
        @Part audioFile: MultipartBody.Part,
        @Part("duration") duration: RequestBody? = null
    ): Call<AudioMessage>
    
    /**
     * Actualiza un mensaje existente (multipart/form-data)
     * Todos los parámetros son opcionales
     * @param messageId ID del mensaje a actualizar
     * @param sender Nuevo remitente (opcional)
     * @param recipient Nuevo destinatario (opcional)
     * @param audioFile Nuevo archivo de audio (opcional)
     * @param duration Nueva duración (opcional)
     */
    @Multipart
    @PUT("messages/{id}")
    fun updateMessage(
        @Path("id") messageId: Int,
        @Part("sender") sender: RequestBody? = null,
        @Part("recipient") recipient: RequestBody? = null,
        @Part audioFile: MultipartBody.Part? = null,
        @Part("duration") duration: RequestBody? = null
    ): Call<AudioMessage>
    
    /**
     * Elimina un mensaje de audio
     * @param messageId ID del mensaje a eliminar
     */
    @DELETE("messages/{id}")
    fun deleteMessage(
        @Path("id") messageId: Int
    ): Call<DeleteResponse>
    
    /**
     * Descarga un archivo de audio
     * @param filename Nombre del archivo
     */
    @GET("media/{filename}")
    @Streaming
    fun downloadAudio(
        @Path("filename") filename: String
    ): Call<okhttp3.ResponseBody>
}
