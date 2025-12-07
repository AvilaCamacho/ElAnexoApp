# Guía Rápida - El Anexo App

## Para el Equipo de Desarrollo

Esta es una guía rápida para entender y presentar el proyecto.

## ✅ Checklist de Requisitos Cumplidos

### Arquitectura e Interfaz
- ✅ **Desarrollo 100% en Kotlin** - Todo el código está en Kotlin
- ✅ **Jetpack Compose** - UI completamente en Compose
- ✅ **Patrón MVVM** - Arquitectura claramente separada en Model-View-ViewModel
- ✅ **Diseño moderno y responsive** - Material Design 3

### Conectividad y Datos (Retrofit)
- ✅ **GET** - `loadProducts()` obtiene productos de la API
- ✅ **POST** - `createProduct()` crea nuevos productos
- ✅ **PUT** - `updateProduct()` actualiza productos existentes
- ✅ **DELETE** - `deleteProduct()` elimina productos

### Integración de Hardware (Sensores)
- ✅ **GPS** - Sensor de ubicación completamente funcional
  - Permisos implementados
  - Obtención de coordenadas (latitud/longitud)
  - Precisión y timestamp

### Gestión del Código (GitHub)
- ✅ **Repositorio público** - github.com/AvilaCamacho/ElAnexoApp
- ✅ **Commits colaborativos** - Historial de commits visible
- ✅ **Workflows CI/CD** - Build automático y releases
- ✅ **README.md completo** - Documentación profesional

## 📱 Funcionalidades de la App

### Pantalla 1: Productos (Tab izquierda)
- Lista de productos obtenidos de la API
- Botón flotante "+" para agregar producto
- Botón eliminar en cada producto
- Refresh para recargar datos
- Manejo de estados: loading, error, success

### Pantalla 2: Ubicación GPS (Tab derecha)
- Solicitud de permisos de ubicación
- Botón para obtener ubicación actual
- Muestra: Latitud, Longitud, Precisión, Hora
- Manejo de errores y permisos

## 🏗️ Estructura del Código (MVVM)

```
📁 data/                    (MODEL)
  ├── model/               - Clases de datos
  ├── remote/              - API con Retrofit
  └── repository/          - Fuente única de datos

📁 ui/                     (VIEW + VIEWMODEL)
  ├── screens/             - Pantallas Compose (VIEW)
  ├── viewmodel/           - Lógica de presentación (VIEWMODEL)
  └── theme/               - Colores y estilos
```

## 🎯 Para la Presentación

### Preguntas Frecuentes

**¿Por qué MVVM?**
- Separación de responsabilidades
- Facilita testing
- Código más mantenible
- Es el patrón recomendado por Google

**¿Por qué Jetpack Compose?**
- UI declarativa (más fácil de entender)
- Menos código boilerplate
- UI reactiva automática
- Es el futuro de Android

**¿Qué hace Retrofit?**
- Convierte la API REST en funciones de Kotlin
- Maneja automáticamente la serialización JSON
- Facilita las llamadas de red

**¿Cómo funciona el GPS?**
- Usa Google Play Services Location
- Solicita permisos al usuario
- Obtiene ubicación de alta precisión
- Actualiza en tiempo real

### Demostración en Vivo

1. **Abrir app** → Mostrar pantalla de productos
2. **Hacer clic en refresh** → Ver loading y carga de datos
3. **Agregar producto** → Botón +, llenar formulario, guardar
4. **Eliminar producto** → Clic en icono de eliminar
5. **Cambiar a pestaña GPS** → Otorgar permisos
6. **Obtener ubicación** → Mostrar coordenadas

### Puntos Clave para Mencionar

1. **Arquitectura limpia** - Todo está organizado por capas
2. **Código profesional** - Sigue estándares de la industria
3. **Manejo de errores** - La app no crashea, muestra errores
4. **Responsive** - Se adapta a diferentes tamaños de pantalla
5. **Seguridad** - Permisos manejados correctamente

## 🚀 Cómo Correr el Proyecto

### Opción 1: Android Studio
1. Abrir Android Studio
2. File → Open → Seleccionar carpeta del proyecto
3. Esperar sincronización de Gradle
4. Conectar dispositivo o emulador
5. Run → Run 'app'

### Opción 2: Línea de Comandos
```bash
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 📦 Generar APK

### Método 1: Android Studio
1. Build → Build Bundle(s) / APK(s) → Build APK(s)
2. Esperar compilación
3. Click en "locate" para encontrar el APK

### Método 2: Terminal
```bash
./gradlew assembleDebug
# El APK estará en: app/build/outputs/apk/debug/
```

### Método 3: GitHub Actions (Automático)
1. Hacer push al repositorio
2. GitHub Actions construye automáticamente
3. APK disponible en Artifacts

## 📝 Notas Importantes

### API Utilizada
- **Fake Store API**: https://fakestoreapi.com/
- Es una API de demostración gratuita
- Los cambios POST/PUT/DELETE se simulan pero no persisten

### Permisos Necesarios
- `INTERNET` - Para llamadas API
- `ACCESS_FINE_LOCATION` - Para GPS de alta precisión
- `ACCESS_COARSE_LOCATION` - Para GPS de baja precisión

### Dependencias Principales
- Jetpack Compose - UI
- Retrofit - Networking
- Play Services Location - GPS
- ViewModel - MVVM
- Accompanist Permissions - Manejo de permisos

## 🎓 Recursos de Aprendizaje

- **Kotlin**: https://kotlinlang.org/
- **Jetpack Compose**: https://developer.android.com/jetpack/compose
- **MVVM**: https://developer.android.com/topic/architecture
- **Retrofit**: https://square.github.io/retrofit/

## ✅ Checklist Pre-Presentación

- [ ] Proyecto compila sin errores
- [ ] APK generado y probado
- [ ] Permisos funcionan correctamente
- [ ] Datos se cargan de la API
- [ ] GPS obtiene ubicación
- [ ] README.md actualizado
- [ ] Screenshots añadidos (opcional)
- [ ] Release en GitHub (si es posible)

## 🏆 Puntos Extra

Para impresionar:
- Mostrar el código MVVM bien organizado
- Explicar el flujo de datos
- Demostrar manejo de errores
- Mostrar los commits en GitHub
- Explicar las decisiones de diseño

---

**¡Buena suerte en la presentación!** 🚀
