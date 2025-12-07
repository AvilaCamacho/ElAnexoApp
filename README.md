# ElAnexoApp

**El Anexo** - Aplicación móvil nativa Android con Jetpack Compose

## 📱 Descripción

El Anexo es una aplicación móvil nativa Android desarrollada como proyecto final, que demuestra el uso de tecnologías modernas y mejores prácticas de desarrollo Android.

## ✨ Características

### Arquitectura
- **MVVM (Model-View-ViewModel)**: Separación clara de responsabilidades
- **Jetpack Compose**: UI moderna y declarativa
- **Kotlin**: Lenguaje de programación principal
- **Coroutines**: Programación asíncrona

### Funcionalidades Principales

#### 1. **CRUD Completo con REST API**
- ✅ **CREATE**: Crear nuevos items
- ✅ **READ**: Listar y ver detalles de items
- ✅ **UPDATE**: Editar items existentes
- ✅ **DELETE**: Eliminar items

La aplicación se conecta a una API REST (JSONPlaceholder como ejemplo) usando:
- **Retrofit**: Cliente HTTP
- **Gson**: Serialización/deserialización JSON
- **OkHttp**: Logging de requests/responses

#### 2. **Integración de Hardware**

##### 📷 Cámara
- Solicitud de permisos de cámara
- Integración con CameraX (preparado para captura de fotos)

##### 📍 Ubicación (GPS)
- Solicitud de permisos de ubicación
- Obtención de coordenadas GPS (latitud/longitud)
- Integración con Google Play Services Location

### 3. **Navegación**
- Navigation Component de Jetpack
- Navegación entre pantallas:
  - Lista de items
  - Detalle de item
  - Crear item
  - Editar item

### 4. **UI/UX**
- Material Design 3
- Tema claro y oscuro
- Indicadores de carga
- Manejo de errores
- Diálogos de confirmación

## 🏗️ Arquitectura MVVM

```
app/
├── model/              # Modelos de datos
│   ├── Item.kt
│   ├── CreateItemRequest.kt
│   └── UpdateItemRequest.kt
├── api/                # Capa de red
│   ├── ApiService.kt
│   └── RetrofitClient.kt
├── repository/         # Capa de datos
│   └── ItemRepository.kt
├── viewmodel/          # Lógica de negocio
│   └── ItemViewModel.kt
├── ui/                 # Interfaz de usuario
│   ├── screens/        # Pantallas
│   ├── navigation/     # Navegación
│   └── theme/          # Tema
└── utils/              # Utilidades
    ├── PermissionUtils.kt
    └── LocationHelper.kt
```

## 🔧 Tecnologías y Dependencias

- **Jetpack Compose**: UI moderna
- **Material 3**: Diseño Material Design
- **Navigation Compose**: Navegación entre pantallas
- **ViewModel & LiveData**: Gestión de estado
- **Retrofit**: Cliente HTTP REST
- **Gson**: Serialización JSON
- **CameraX**: Acceso a cámara
- **Google Play Services Location**: Servicios de ubicación
- **Coil**: Carga de imágenes
- **Coroutines**: Programación asíncrona

## 📋 Requisitos

- Android Studio Hedgehog | 2023.1.1 o superior
- JDK 8 o superior
- Android SDK API 24 (Android 7.0) o superior
- Dispositivo Android o emulador

## 🚀 Instalación y Ejecución

1. **Clonar el repositorio**
```bash
git clone https://github.com/AvilaCamacho/ElAnexoApp.git
cd ElAnexoApp
```

2. **Abrir en Android Studio**
   - File → Open → Seleccionar la carpeta del proyecto

3. **Sincronizar Gradle**
   - Android Studio sincronizará automáticamente las dependencias

4. **Ejecutar la aplicación**
   - Conectar un dispositivo Android o iniciar un emulador
   - Click en el botón "Run" (▶️)

## 🔐 Permisos

La aplicación solicita los siguientes permisos:

- **INTERNET**: Para realizar llamadas a la API REST
- **ACCESS_NETWORK_STATE**: Para verificar conectividad
- **CAMERA**: Para capturar fotos (funcionalidad preparada)
- **ACCESS_FINE_LOCATION**: Para obtener ubicación GPS precisa
- **ACCESS_COARSE_LOCATION**: Para obtener ubicación GPS aproximada

## 📱 Pantallas

### 1. Lista de Items
- Muestra todos los items en un RecyclerView
- FAB para agregar nuevo item
- Click en item para ver detalles

### 2. Detalle de Item
- Muestra información completa del item
- Opciones para editar o eliminar
- Muestra ubicación GPS si está disponible

### 3. Crear/Editar Item
- Formulario para título y descripción
- Botón para capturar foto (cámara)
- Botón para obtener ubicación GPS
- Validación de campos

## 🌐 API REST

Por defecto, la aplicación usa [JSONPlaceholder](https://jsonplaceholder.typicode.com/) como API de demostración.

Para usar tu propia API, modifica la constante `BASE_URL` en `RetrofitClient.kt`:

```kotlin
private const val BASE_URL = "https://tu-api.com/"
```

## 🎨 Personalización

### Cambiar Tema
Edita `ui/theme/Theme.kt` para personalizar colores

### Modificar Strings
Edita `res/values/strings.xml` para cambiar textos

## 🧪 Testing

El proyecto incluye dependencias para testing:
- JUnit para pruebas unitarias
- Espresso para pruebas UI
- Compose Test para pruebas de componentes

## 📝 Licencia

Este proyecto fue desarrollado como proyecto final educativo.

## 👨‍💻 Autor

Proyecto desarrollado para demostrar conocimientos en:
- Desarrollo Android nativo
- Jetpack Compose
- Arquitectura MVVM
- Integración con APIs REST
- Uso de hardware del dispositivo (cámara, GPS)
- Manejo de permisos
- Navegación entre pantallas
- Material Design 3

---

**Versión**: 1.0  
**Compilación SDK**: 34  
**Min SDK**: 24  
**Target SDK**: 34