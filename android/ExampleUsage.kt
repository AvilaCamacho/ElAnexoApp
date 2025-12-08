package com.elanexo.app.examples

import android.content.Context
import android.util.Log
import com.elanexo.app.data.remote.ApiService
import com.elanexo.app.data.remote.AudioMessage
import com.elanexo.app.data.remote.DeleteResponse
import com.elanexo.app.data.remote.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/**
 * Clase con ejemplos de uso de la API de AudioMessages
 * 
 * IMPORTANTE:
 * - Estos ejemplos usan Callbacks para manejo asíncrono
 * - En producción, considerar usar Coroutines + Flow
 * - Manejar permisos de almacenamiento para leer archivos
 * - Validar que los archivos existan antes de subirlos
 */
class ExampleUsage(private val context: Context) {
    
    private val apiService: ApiService = RetrofitClient.apiService
    private val TAG = "AudioMessageAPI"
    
    /**
     * Ejemplo 1: Obtener todos los mensajes
     */
    fun getAllMessages() {
        apiService.getMessages().enqueue(object : Callback<List<AudioMessage>> {
            override fun onResponse(
                call: Call<List<AudioMessage>>,
                response: Response<List<AudioMessage>>
            ) {
                if (response.isSuccessful) {
                    val messages = response.body()
                    Log.d(TAG, "Mensajes obtenidos: ${messages?.size}")
                    messages?.forEach { message ->
                        Log.d(TAG, "Mensaje: ${message.sender} -> ${message.recipient}")
                    }
                } else {
                    Log.e(TAG, "Error: ${response.code()} - ${response.message()}")
                }
            }
            
            override fun onFailure(call: Call<List<AudioMessage>>, t: Throwable) {
                Log.e(TAG, "Error de red: ${t.message}", t)
            }
        })
    }
    
    /**
     * Ejemplo 2: Obtener mensajes con filtros
     */
    fun getMessagesBySender(sender: String) {
        apiService.getMessages(sender = sender).enqueue(object : Callback<List<AudioMessage>> {
            override fun onResponse(
                call: Call<List<AudioMessage>>,
                response: Response<List<AudioMessage>>
            ) {
                if (response.isSuccessful) {
                    val messages = response.body()
                    Log.d(TAG, "Mensajes de $sender: ${messages?.size}")
                } else {
                    Log.e(TAG, "Error al filtrar mensajes: ${response.code()}")
                }
            }
            
            override fun onFailure(call: Call<List<AudioMessage>>, t: Throwable) {
                Log.e(TAG, "Error de red: ${t.message}", t)
            }
        })
    }
    
    /**
     * Ejemplo 3: Obtener un mensaje específico por ID
     */
    fun getMessageById(messageId: Int) {
        apiService.getMessage(messageId).enqueue(object : Callback<AudioMessage> {
            override fun onResponse(
                call: Call<AudioMessage>,
                response: Response<AudioMessage>
            ) {
                if (response.isSuccessful) {
                    val message = response.body()
                    Log.d(TAG, "Mensaje obtenido: ${message?.filename}")
                } else if (response.code() == 404) {
                    Log.e(TAG, "Mensaje no encontrado")
                } else {
                    Log.e(TAG, "Error: ${response.code()}")
                }
            }
            
            override fun onFailure(call: Call<AudioMessage>, t: Throwable) {
                Log.e(TAG, "Error de red: ${t.message}", t)
            }
        })
    }
    
    /**
     * Ejemplo 4: Crear un nuevo mensaje de audio (POST multipart)
     * 
     * @param audioFilePath Ruta al archivo de audio en el dispositivo
     * @param sender Nombre del remitente
     * @param recipient Nombre del destinatario
     * @param duration Duración en segundos (opcional)
     */
    fun createAudioMessage(
        audioFilePath: String,
        sender: String,
        recipient: String,
        duration: Float? = null
    ) {
        val audioFile = File(audioFilePath)
        
        // Validar que el archivo existe
        if (!audioFile.exists()) {
            Log.e(TAG, "El archivo no existe: $audioFilePath")
            return
        }
        
        // Crear RequestBody para el archivo
        val requestFile = audioFile.asRequestBody("audio/*".toMediaTypeOrNull())
        
        // Crear MultipartBody.Part para el archivo
        val audioPart = MultipartBody.Part.createFormData(
            "audio_file",
            audioFile.name,
            requestFile
        )
        
        // Crear RequestBody para campos de texto
        val senderBody = sender.toRequestBody("text/plain".toMediaTypeOrNull())
        val recipientBody = recipient.toRequestBody("text/plain".toMediaTypeOrNull())
        val durationBody = duration?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        
        // Realizar petición POST
        apiService.createMessage(
            sender = senderBody,
            recipient = recipientBody,
            audioFile = audioPart,
            duration = durationBody
        ).enqueue(object : Callback<AudioMessage> {
            override fun onResponse(
                call: Call<AudioMessage>,
                response: Response<AudioMessage>
            ) {
                if (response.isSuccessful) {
                    val message = response.body()
                    Log.d(TAG, "Mensaje creado exitosamente: ID ${message?.id}")
                    Log.d(TAG, "Archivo: ${message?.filename}, Tamaño: ${message?.file_size} bytes")
                } else {
                    Log.e(TAG, "Error al crear mensaje: ${response.code()} - ${response.errorBody()?.string()}")
                }
            }
            
            override fun onFailure(call: Call<AudioMessage>, t: Throwable) {
                Log.e(TAG, "Error de red al crear mensaje: ${t.message}", t)
            }
        })
    }
    
    /**
     * Ejemplo 5: Actualizar un mensaje existente (PUT multipart)
     * 
     * @param messageId ID del mensaje a actualizar
     * @param newAudioFilePath Ruta al nuevo archivo (opcional)
     * @param newSender Nuevo remitente (opcional)
     * @param newRecipient Nuevo destinatario (opcional)
     * @param newDuration Nueva duración (opcional)
     */
    fun updateAudioMessage(
        messageId: Int,
        newAudioFilePath: String? = null,
        newSender: String? = null,
        newRecipient: String? = null,
        newDuration: Float? = null
    ) {
        // Preparar archivo si se proporciona
        var audioPart: MultipartBody.Part? = null
        if (newAudioFilePath != null) {
            val audioFile = File(newAudioFilePath)
            if (audioFile.exists()) {
                val requestFile = audioFile.asRequestBody("audio/*".toMediaTypeOrNull())
                audioPart = MultipartBody.Part.createFormData(
                    "audio_file",
                    audioFile.name,
                    requestFile
                )
            }
        }
        
        // Preparar campos de texto opcionales
        val senderBody = newSender?.toRequestBody("text/plain".toMediaTypeOrNull())
        val recipientBody = newRecipient?.toRequestBody("text/plain".toMediaTypeOrNull())
        val durationBody = newDuration?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
        
        // Realizar petición PUT
        apiService.updateMessage(
            messageId = messageId,
            sender = senderBody,
            recipient = recipientBody,
            audioFile = audioPart,
            duration = durationBody
        ).enqueue(object : Callback<AudioMessage> {
            override fun onResponse(
                call: Call<AudioMessage>,
                response: Response<AudioMessage>
            ) {
                if (response.isSuccessful) {
                    val message = response.body()
                    Log.d(TAG, "Mensaje actualizado exitosamente: ${message?.filename}")
                } else if (response.code() == 404) {
                    Log.e(TAG, "Mensaje no encontrado")
                } else {
                    Log.e(TAG, "Error al actualizar: ${response.code()}")
                }
            }
            
            override fun onFailure(call: Call<AudioMessage>, t: Throwable) {
                Log.e(TAG, "Error de red al actualizar: ${t.message}", t)
            }
        })
    }
    
    /**
     * Ejemplo 6: Eliminar un mensaje (DELETE)
     * 
     * @param messageId ID del mensaje a eliminar
     */
    fun deleteAudioMessage(messageId: Int) {
        apiService.deleteMessage(messageId).enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>
            ) {
                if (response.isSuccessful) {
                    val deleteResponse = response.body()
                    Log.d(TAG, "Mensaje eliminado: ${deleteResponse?.message}")
                } else if (response.code() == 404) {
                    Log.e(TAG, "Mensaje no encontrado")
                } else {
                    Log.e(TAG, "Error al eliminar: ${response.code()}")
                }
            }
            
            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                Log.e(TAG, "Error de red al eliminar: ${t.message}", t)
            }
        })
    }
    
    /**
     * Ejemplo 7: Descargar un archivo de audio
     * 
     * @param filename Nombre del archivo en el servidor
     * @param destinationPath Ruta donde guardar el archivo descargado
     */
    fun downloadAudioFile(filename: String, destinationPath: String) {
        apiService.downloadAudio(filename).enqueue(object : Callback<okhttp3.ResponseBody> {
            override fun onResponse(
                call: Call<okhttp3.ResponseBody>,
                response: Response<okhttp3.ResponseBody>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        try {
                            // Guardar archivo
                            val destinationFile = File(destinationPath)
                            destinationFile.outputStream().use { output ->
                                body.byteStream().use { input ->
                                    input.copyTo(output)
                                }
                            }
                            Log.d(TAG, "Archivo descargado: $destinationPath")
                        } catch (e: Exception) {
                            Log.e(TAG, "Error al guardar archivo: ${e.message}", e)
                        }
                    }
                } else {
                    Log.e(TAG, "Error al descargar: ${response.code()}")
                }
            }
            
            override fun onFailure(call: Call<okhttp3.ResponseBody>, t: Throwable) {
                Log.e(TAG, "Error de red al descargar: ${t.message}", t)
            }
        })
    }
    
    /**
     * Ejemplo completo: Flujo de trabajo completo
     * 1. Crear mensaje
     * 2. Obtener el mensaje creado
     * 3. Actualizar el mensaje
     * 4. Eliminar el mensaje
     */
    fun completeWorkflowExample(audioFilePath: String) {
        Log.d(TAG, "=== Iniciando flujo de trabajo completo ===")
        
        // Paso 1: Crear mensaje
        createAudioMessage(
            audioFilePath = audioFilePath,
            sender = "Usuario Android",
            recipient = "Servidor",
            duration = 30.5f
        )
        
        // Nota: En una aplicación real, deberías esperar la respuesta del POST
        // y usar el ID devuelto para las operaciones siguientes.
        // Aquí usamos IDs de ejemplo para demostrar el uso.
        
        // Paso 2: Obtener mensaje (ejemplo con ID 1)
        // getMessageById(1)
        
        // Paso 3: Actualizar mensaje
        // updateAudioMessage(messageId = 1, newDuration = 35.0f)
        
        // Paso 4: Eliminar mensaje
        // deleteAudioMessage(1)
    }
}

/**
 * Ejemplo de uso desde una Activity o Fragment:
 * 
 * class MainActivity : AppCompatActivity() {
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         
 *         val exampleUsage = ExampleUsage(this)
 *         
 *         // Obtener todos los mensajes
 *         exampleUsage.getAllMessages()
 *         
 *         // Crear un mensaje con un archivo de audio
 *         val audioPath = "/storage/emulated/0/Download/audio.mp3"
 *         exampleUsage.createAudioMessage(
 *             audioFilePath = audioPath,
 *             sender = "Juan",
 *             recipient = "Maria",
 *             duration = 45.5f
 *         )
 *         
 *         // Eliminar un mensaje
 *         exampleUsage.deleteAudioMessage(1)
 *     }
 * }
 */
