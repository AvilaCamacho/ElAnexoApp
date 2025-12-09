package com.elanexo.app.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit singleton para conectar con la API Flask
 * 
 * NOTA IMPORTANTE:
 * - Para emulador Android: usar http://10.0.2.2:5000/
 * - Para dispositivo físico: usar http://TU_IP_LOCAL:5000/
 * - En producción: usar la URL real del servidor
 */
object RetrofitClient {
    
    // URL base del servidor Flask
    // 10.0.2.2 es la dirección especial para localhost en emulador Android
    private const val BASE_URL = "http://10.0.2.2:5000/"
    
    /**
     * Interceptor para logging de peticiones HTTP (útil para debugging)
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    /**
     * Cliente OkHttp con configuración personalizada
     * - Timeouts aumentados para uploads de archivos grandes
     * - Logging interceptor para debug
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    /**
     * Instancia de Retrofit configurada con:
     * - Gson para serialización/deserialización JSON
     * - OkHttpClient personalizado
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    /**
     * Instancia del servicio API
     */
    val apiService: ApiService = retrofit.create(ApiService::class.java)
    
    /**
     * Método para cambiar la URL base dinámicamente
     * Útil cuando se conecta a diferentes servidores o dispositivos físicos
     * 
     * @param baseUrl Nueva URL base (ej: "http://192.168.1.100:5000/")
     * @return Nueva instancia del servicio API
     */
    fun createService(baseUrl: String): ApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
