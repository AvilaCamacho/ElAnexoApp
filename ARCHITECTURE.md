# Documentación Técnica - El Anexo App

## Tabla de Contenidos
1. [Arquitectura MVVM](#arquitectura-mvvm)
2. [Operaciones CRUD](#operaciones-crud)
3. [Integración de Sensores](#integración-de-sensores)
4. [Jetpack Compose](#jetpack-compose)
5. [API REST con Retrofit](#api-rest-con-retrofit)

---

## Arquitectura MVVM

### ¿Qué es MVVM?

MVVM (Model-View-ViewModel) es un patrón de arquitectura que separa la lógica de negocio de la interfaz de usuario.

### Capas de la Arquitectura

#### 1. Model (Modelo)
**Ubicación**: `app/src/main/java/com/elanexo/app/data/`

Responsabilidades:
- Definir estructuras de datos
- Manejar la lógica de negocio
- Comunicarse con fuentes de datos (API, base de datos)

**Componentes**:
- `model/Product.kt` - Clase de datos para productos
- `model/Location.kt` - Clase de datos para ubicación GPS
- `remote/ApiService.kt` - Interface de servicio REST
- `remote/RetrofitClient.kt` - Cliente Retrofit
- `repository/ProductRepository.kt` - Repositorio de productos
- `repository/LocationRepository.kt` - Repositorio de ubicación

#### 2. View (Vista)
**Ubicación**: `app/src/main/java/com/elanexo/app/ui/screens/`

Responsabilidades:
- Mostrar datos al usuario
- Capturar interacciones del usuario
- NO contiene lógica de negocio

**Componentes**:
- `ProductsScreen.kt` - Pantalla de lista de productos
- `LocationScreen.kt` - Pantalla de ubicación GPS
- `MainScreen.kt` - Navegación principal

#### 3. ViewModel
**Ubicación**: `app/src/main/java/com/elanexo/app/ui/viewmodel/`

Responsabilidades:
- Preparar datos para la vista
- Manejar la lógica de presentación
- Comunicarse con el repositorio
- Mantener el estado de la UI

**Componentes**:
- `ProductViewModel.kt` - ViewModel para productos
- `LocationViewModel.kt` - ViewModel para ubicación

### Flujo de Datos

```
Usuario interactúa con View
    ↓
View notifica al ViewModel
    ↓
ViewModel solicita datos al Repository
    ↓
Repository obtiene datos del Model/API
    ↓
Repository devuelve datos al ViewModel
    ↓
ViewModel actualiza el estado
    ↓
View se recompone automáticamente (Compose)
```

---

## Operaciones CRUD

### ¿Qué es CRUD?

CRUD = **C**reate, **R**ead, **U**pdate, **D**elete
Son las cuatro operaciones básicas de persistencia de datos.

### Implementación en El Anexo App

#### 1. CREATE (POST)
**Función**: `createProduct()`
**Endpoint**: `POST /products`
**Ubicación**: 
- ViewModel: `ProductViewModel.createProduct()`
- Repository: `ProductRepository.createProduct()`
- API: `ApiService.createProduct()`

**Ejemplo de uso**:
```kotlin
viewModel.createProduct(
    Product(
        title = "Nuevo Producto",
        price = 99.99,
        description = "Descripción del producto"
    )
)
```

#### 2. READ (GET)
**Función**: `getProducts()`, `getProduct(id)`
**Endpoint**: `GET /products`, `GET /products/{id}`
**Ubicación**:
- ViewModel: `ProductViewModel.loadProducts()`
- Repository: `ProductRepository.getProducts()`
- API: `ApiService.getProducts()`

**Ejemplo de uso**:
```kotlin
// Cargar todos los productos
viewModel.loadProducts()

// Los productos se observan automáticamente
val products by viewModel.products
```

#### 3. UPDATE (PUT)
**Función**: `updateProduct()`
**Endpoint**: `PUT /products/{id}`
**Ubicación**:
- ViewModel: `ProductViewModel.updateProduct()`
- Repository: `ProductRepository.updateProduct()`
- API: `ApiService.updateProduct()`

**Ejemplo de uso**:
```kotlin
viewModel.updateProduct(
    id = 1,
    product = Product(
        title = "Producto Actualizado",
        price = 149.99,
        description = "Nueva descripción"
    )
)
```

#### 4. DELETE (DELETE)
**Función**: `deleteProduct()`
**Endpoint**: `DELETE /products/{id}`
**Ubicación**:
- ViewModel: `ProductViewModel.deleteProduct()`
- Repository: `ProductRepository.deleteProduct()`
- API: `ApiService.deleteProduct()`

**Ejemplo de uso**:
```kotlin
viewModel.deleteProduct(id = 1)
```

---

## Integración de Sensores

### Sensor GPS (Sistema de Posicionamiento Global)

#### ¿Por qué GPS?
El GPS permite obtener la ubicación geográfica del dispositivo, útil para:
- Rastrear ubicación de inventarios
- Verificar puntos de entrega
- Geolocalización de usuarios

#### Implementación

**Permisos requeridos** (AndroidManifest.xml):
```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

**Componentes**:
1. **LocationRepository** - Maneja la obtención de ubicación
2. **LocationViewModel** - Gestiona el estado de ubicación
3. **LocationScreen** - Muestra la ubicación al usuario

**Código clave**:
```kotlin
// LocationRepository.kt
@SuppressLint("MissingPermission")
suspend fun getCurrentLocation(): Result<Location> {
    val location = fusedLocationClient.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        null
    ).await()
    
    return Result.success(Location(
        latitude = location.latitude,
        longitude = location.longitude,
        accuracy = location.accuracy
    ))
}
```

#### Manejo de Permisos
Se usa **Accompanist Permissions** para solicitar permisos en tiempo de ejecución:

```kotlin
val locationPermissionsState = rememberMultiplePermissionsState(
    permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
)
```

---

## Jetpack Compose

### ¿Qué es Jetpack Compose?

Framework moderno de UI declarativa para Android que simplifica el desarrollo de interfaces.

### Ventajas
- ✅ Código más conciso
- ✅ UI reactiva automática
- ✅ Menos boilerplate
- ✅ Kotlin 100%
- ✅ Preview en tiempo real

### Componentes Principales Usados

#### 1. Scaffold
Estructura básica de una pantalla con AppBar, FAB, etc.

```kotlin
Scaffold(
    topBar = { TopAppBar(...) },
    floatingActionButton = { FloatingActionButton(...) }
) { paddingValues ->
    // Contenido
}
```

#### 2. LazyColumn
Lista eficiente (equivalente a RecyclerView)

```kotlin
LazyColumn {
    items(products) { product ->
        ProductCard(product)
    }
}
```

#### 3. State Management
Manejo reactivo del estado

```kotlin
val products by viewModel.products // Observa cambios
val isLoading by viewModel.isLoading
```

---

## API REST con Retrofit

### ¿Qué es Retrofit?

Librería de networking type-safe para Android que convierte una API REST en una interfaz de Kotlin.

### Configuración

#### 1. Dependencias
```kotlin
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
```

#### 2. Interface de API
```kotlin
interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>
    
    @POST("products")
    suspend fun createProduct(@Body product: Product): Product
    
    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Product
    
    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Product
}
```

#### 3. Cliente Retrofit
```kotlin
object RetrofitClient {
    private const val BASE_URL = "https://fakestoreapi.com/"
    
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
```

### API Utilizada

**Fake Store API**: https://fakestoreapi.com/

Es una API de prueba gratuita que simula una tienda en línea.

**Nota**: Las operaciones POST, PUT y DELETE se simulan pero no persisten realmente en el servidor.

---

## Resumen

Esta aplicación demuestra:

1. ✅ **Arquitectura MVVM** completa y bien estructurada
2. ✅ **CRUD completo** con operaciones GET, POST, PUT, DELETE
3. ✅ **Integración de sensor GPS** para ubicación
4. ✅ **Jetpack Compose** para UI moderna
5. ✅ **Retrofit** para comunicación con API REST
6. ✅ **Kotlin 100%** código moderno y conciso
7. ✅ **Material Design 3** diseño visual atractivo

Todo implementado siguiendo las mejores prácticas de desarrollo Android.
