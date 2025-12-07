# Arquitectura de ElAnexo App

## Resumen de Arquitectura MVVM

Esta aplicación sigue el patrón de arquitectura **MVVM (Model-View-ViewModel)** recomendado por Google para aplicaciones Android modernas.

## Capas de la Arquitectura

### 1. Model (Modelo)
**Ubicación**: `app/src/main/java/com/elanexo/app/model/`

Contiene las clases de datos que representan la información de la aplicación:
- `Item.kt`: Modelo principal de datos
- `CreateItemRequest.kt`: DTO para crear items
- `UpdateItemRequest.kt`: DTO para actualizar items

### 2. View (Vista)
**Ubicación**: `app/src/main/java/com/elanexo/app/ui/`

Implementada con **Jetpack Compose**, incluye:

#### Screens (Pantallas)
- `ItemListScreen.kt`: Lista de todos los items
- `ItemDetailScreen.kt`: Detalles de un item específico
- `ItemCreateScreen.kt`: Formulario para crear items
- `ItemEditScreen.kt`: Formulario para editar items

#### Navigation (Navegación)
- `Screen.kt`: Definición de rutas
- `AppNavigation.kt`: Configuración del NavHost

#### Theme (Tema)
- `Theme.kt`: Configuración de colores y tema de Material 3

### 3. ViewModel
**Ubicación**: `app/src/main/java/com/elanexo/app/viewmodel/`

- `ItemViewModel.kt`: Gestiona el estado de la UI y la lógica de negocio
  - Expone `StateFlow<ItemsUiState>` para la UI
  - Maneja operaciones CRUD
  - Gestiona estados de carga y errores

### 4. Repository (Repositorio)
**Ubicación**: `app/src/main/java/com/elanexo/app/repository/`

- `ItemRepository.kt`: Abstrae la fuente de datos
  - Implementa operaciones CRUD
  - Maneja errores de red
  - Convierte respuestas a objetos de dominio

### 5. API Layer (Capa de API)
**Ubicación**: `app/src/main/java/com/elanexo/app/api/`

- `ApiService.kt`: Interfaz Retrofit con endpoints REST
- `RetrofitClient.kt`: Configuración de Retrofit y OkHttp

### 6. Utils (Utilidades)
**Ubicación**: `app/src/main/java/com/elanexo/app/utils/`

- `PermissionUtils.kt`: Gestión de permisos Android
- `LocationHelper.kt`: Integración con GPS

## Flujo de Datos

```
UI (Compose) 
    ↕️
ViewModel (StateFlow)
    ↕️
Repository
    ↕️
API Service (Retrofit)
    ↕️
REST API
```

## Gestión de Estado

La aplicación usa **StateFlow** para gestión reactiva del estado:

```kotlin
data class ItemsUiState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedItem: Item? = null,
    val operationSuccess: Boolean = false
)
```

## Operaciones CRUD

### CREATE
```kotlin
POST /items
Body: CreateItemRequest
Response: Item
```

### READ
```kotlin
GET /items -> List<Item>
GET /items/{id} -> Item
```

### UPDATE
```kotlin
PUT /items/{id}
Body: UpdateItemRequest
Response: Item
```

### DELETE
```kotlin
DELETE /items/{id}
Response: Unit
```

## Integración de Hardware

### Cámara
- Usa **CameraX** (dependencias incluidas)
- Solicita permiso `CAMERA`
- Preparado para captura de fotos

### GPS/Ubicación
- Usa **Google Play Services Location**
- Solicita permisos `ACCESS_FINE_LOCATION` y `ACCESS_COARSE_LOCATION`
- Obtiene coordenadas actuales usando `FusedLocationProviderClient`

## Navegación

Sistema de navegación basado en rutas:

```
ItemList (inicio)
    ├── ItemDetail/{id}
    │   ├── ItemEdit/{id}
    │   └── (back)
    └── ItemCreate
        └── (back)
```

## Dependencias Principales

- **Jetpack Compose**: UI declarativa
- **Navigation Compose**: Navegación
- **Retrofit**: Cliente REST
- **Coroutines**: Operaciones asíncronas
- **Material 3**: Componentes de UI
- **CameraX**: Acceso a cámara
- **Play Services Location**: GPS

## Mejores Prácticas Implementadas

1. ✅ Separación de responsabilidades (MVVM)
2. ✅ Inyección de dependencias manual
3. ✅ Programación reactiva con Flow
4. ✅ Manejo de errores centralizado
5. ✅ Estados de UI predecibles
6. ✅ Navegación tipo-segura
7. ✅ Gestión de permisos runtime
8. ✅ UI responsive con Compose

## Extensibilidad

Para agregar nuevas funcionalidades:

1. **Nueva entidad**: Crear modelo en `model/`
2. **Nuevos endpoints**: Agregar a `ApiService`
3. **Nueva pantalla**: Crear en `ui/screens/`
4. **Nueva ruta**: Agregar a `Screen.kt` y `AppNavigation.kt`
5. **Nueva lógica**: Extender o crear nuevo ViewModel
