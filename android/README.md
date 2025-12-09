# Ejemplos de Cliente Android para API AudioMessages

Esta carpeta contiene ejemplos de código Kotlin para integrar la API de AudioMessages en una aplicación Android usando Retrofit.

## 📁 Archivos incluidos

### 1. `ApiService.kt`
Interface Retrofit que define todos los endpoints de la API:
- `getMessages()` - GET lista de mensajes
- `getMessage(id)` - GET mensaje específico
- `createMessage()` - POST nuevo mensaje (multipart)
- `updateMessage(id)` - PUT actualizar mensaje (multipart)
- `deleteMessage(id)` - DELETE eliminar mensaje
- `downloadAudio()` - GET descargar archivo de audio

También incluye las data classes:
- `AudioMessage` - Modelo de datos del mensaje
- `DeleteResponse` - Respuesta de eliminación

### 2. `RetrofitClient.kt`
Cliente Retrofit singleton configurado con:
- URL base apuntando a `http://10.0.2.2:5000/` (para emulador)
- Logging interceptor para debugging
- Timeouts personalizados para uploads grandes
- Método para cambiar URL dinámicamente

### 3. `ExampleUsage.kt`
Clase con ejemplos completos de uso:
- Obtener todos los mensajes
- Filtrar mensajes por sender/recipient
- Crear mensaje con archivo de audio (POST multipart)
- Actualizar mensaje (PUT multipart)
- Eliminar mensaje (DELETE)
- Descargar archivo de audio
- Flujo de trabajo completo

## 🚀 Integración en tu app Android

### Paso 1: Agregar dependencias a `build.gradle.kts`

```kotlin
dependencies {
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // OkHttp (para logging)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    
    // Gson
    implementation("com.google.code.gson:gson:2.10.1")
}
```

### Paso 2: Agregar permisos al `AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

<!-- Para Android 13+ (API 33+) -->
<uses-permission android:name="android.permission.READ_MEDIA_AUDIO" />
```

### Paso 3: Configuración de red en `AndroidManifest.xml`

Para permitir tráfico HTTP (no HTTPS) en desarrollo:

```xml
<application
    android:usesCleartextTraffic="true"
    ...>
```

### Paso 4: Copiar archivos a tu proyecto

Copia los archivos a las rutas apropiadas en tu proyecto:

```
app/src/main/java/com/elanexo/app/
├── data/
│   └── remote/
│       ├── ApiService.kt         # Copiar aquí
│       └── RetrofitClient.kt     # Copiar aquí
└── examples/
    └── ExampleUsage.kt           # Copiar aquí (opcional)
```

**Nota:** Ajusta los paquetes (`package`) al inicio de cada archivo según la estructura de tu proyecto.

### Paso 5: Usar en tu código

#### Ejemplo básico en una Activity:

```kotlin
class MainActivity : AppCompatActivity() {
    
    private val apiService = RetrofitClient.apiService
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Obtener todos los mensajes
        apiService.getMessages().enqueue(object : Callback<List<AudioMessage>> {
            override fun onResponse(
                call: Call<List<AudioMessage>>,
                response: Response<List<AudioMessage>>
            ) {
                if (response.isSuccessful) {
                    val messages = response.body()
                    // Procesar mensajes
                    messages?.forEach { message ->
                        Log.d("API", "Mensaje: ${message.sender} -> ${message.recipient}")
                    }
                }
            }
            
            override fun onFailure(call: Call<List<AudioMessage>>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
            }
        })
    }
}
```

#### Ejemplo con Jetpack Compose y ViewModel:

```kotlin
class AudioMessageViewModel : ViewModel() {
    
    private val apiService = RetrofitClient.apiService
    
    private val _messages = MutableStateFlow<List<AudioMessage>>(emptyList())
    val messages: StateFlow<List<AudioMessage>> = _messages.asStateFlow()
    
    fun loadMessages() {
        apiService.getMessages().enqueue(object : Callback<List<AudioMessage>> {
            override fun onResponse(
                call: Call<List<AudioMessage>>,
                response: Response<List<AudioMessage>>
            ) {
                if (response.isSuccessful) {
                    _messages.value = response.body() ?: emptyList()
                }
            }
            
            override fun onFailure(call: Call<List<AudioMessage>>, t: Throwable) {
                // Manejar error
            }
        })
    }
}
```

## 📱 Conexión con el servidor Flask

### Para Emulador Android:
El código ya está configurado para usar `http://10.0.2.2:5000/`

**Importante:** `10.0.2.2` es la IP especial que el emulador usa para acceder a `localhost` en tu computadora.

### Para Dispositivo Físico:
1. Encuentra tu IP local:
   ```bash
   # Linux/macOS
   ifconfig | grep "inet "
   
   # Windows
   ipconfig
   ```

2. Cambia la URL en `RetrofitClient.kt`:
   ```kotlin
   private const val BASE_URL = "http://TU_IP_LOCAL:5000/"
   ```

3. Asegúrate de que el dispositivo esté en la misma red WiFi que tu computadora.

## 🎯 Ejemplo de subida multipart

```kotlin
fun uploadAudioMessage(audioFile: File) {
    // Preparar archivo
    val requestFile = audioFile.asRequestBody("audio/*".toMediaTypeOrNull())
    val audioPart = MultipartBody.Part.createFormData(
        "audio_file",
        audioFile.name,
        requestFile
    )
    
    // Preparar campos
    val sender = "Juan".toRequestBody("text/plain".toMediaTypeOrNull())
    val recipient = "Maria".toRequestBody("text/plain".toMediaTypeOrNull())
    val duration = "30.5".toRequestBody("text/plain".toMediaTypeOrNull())
    
    // Enviar
    apiService.createMessage(sender, recipient, audioPart, duration)
        .enqueue(object : Callback<AudioMessage> {
            override fun onResponse(
                call: Call<AudioMessage>,
                response: Response<AudioMessage>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Mensaje creado!", Toast.LENGTH_SHORT).show()
                }
            }
            
            override fun onFailure(call: Call<AudioMessage>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
}
```

## 🔒 Permisos de almacenamiento

Para leer archivos de audio, necesitas solicitar permisos en runtime:

```kotlin
// Para Android 13+ (API 33+)
val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    Manifest.permission.READ_MEDIA_AUDIO
} else {
    Manifest.permission.READ_EXTERNAL_STORAGE
}

if (ContextCompat.checkSelfPermission(this, permission) 
    != PackageManager.PERMISSION_GRANTED) {
    ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE)
}
```

## 📝 Notas importantes

1. **Threading:** Los callbacks de Retrofit se ejecutan en el hilo principal, pero las operaciones de red se realizan en hilos de fondo automáticamente.

2. **Coroutines (Recomendado):** Para código más limpio, considera usar Coroutines en lugar de Callbacks:
   ```kotlin
   // En ApiService.kt, cambiar:
   @GET("messages")
   suspend fun getMessages(): Response<List<AudioMessage>>
   
   // Uso en ViewModel:
   viewModelScope.launch {
       val response = apiService.getMessages()
       if (response.isSuccessful) {
           _messages.value = response.body() ?: emptyList()
       }
   }
   ```

3. **Manejo de errores:** Siempre verifica `response.isSuccessful` antes de usar `response.body()`.

4. **Logging:** En producción, desactiva el logging interceptor para mejorar el rendimiento.

## 🧪 Pruebas

Ver `ExampleUsage.kt` para ejemplos completos de:
- GET (con y sin filtros)
- POST multipart (crear mensaje con archivo)
- PUT multipart (actualizar mensaje)
- DELETE (eliminar mensaje)
- Descarga de archivos

## 🔗 Referencias

- [Retrofit Documentation](https://square.github.io/retrofit/)
- [OkHttp Documentation](https://square.github.io/okhttp/)
- [Android Permissions Guide](https://developer.android.com/training/permissions/requesting)
- [Jetpack Compose ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
