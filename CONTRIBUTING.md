# Guía de Contribución

## Cómo contribuir al proyecto

### Requisitos previos

1. **Android Studio** - Última versión estable (Hedgehog o superior)
2. **JDK 17** - Java Development Kit versión 17
3. **Android SDK** - API Level 24 o superior
4. **Git** - Para control de versiones

### Configuración del entorno de desarrollo

1. Clona el repositorio:
```bash
git clone https://github.com/AvilaCamacho/ElAnexoApp.git
cd ElAnexoApp
```

2. Abre el proyecto en Android Studio

3. Sincroniza las dependencias de Gradle

4. Conecta un dispositivo Android o inicia un emulador

5. Ejecuta la aplicación

### Estructura del proyecto

```
app/src/main/
├── java/com/elanexo/app/
│   ├── data/              # Capa de datos (Model)
│   │   ├── model/         # Clases de datos
│   │   ├── remote/        # API y Retrofit
│   │   └── repository/    # Repositorios
│   ├── ui/                # Capa de presentación (View)
│   │   ├── screens/       # Pantallas Compose
│   │   ├── theme/         # Tema visual
│   │   └── viewmodel/     # ViewModels
│   └── MainActivity.kt    # Actividad principal
└── res/                   # Recursos Android
```

### Convenciones de código

- **Lenguaje**: Kotlin 100%
- **Estilo**: Seguir las convenciones oficiales de Kotlin
- **Arquitectura**: MVVM estrictamente
- **Comentarios**: En español o inglés, claros y concisos

### Proceso de contribución

1. Crea un fork del repositorio
2. Crea una rama para tu feature: `git checkout -b feature/nueva-funcionalidad`
3. Realiza tus cambios siguiendo las convenciones
4. Haz commit de tus cambios: `git commit -m 'Agrega nueva funcionalidad'`
5. Push a la rama: `git push origin feature/nueva-funcionalidad`
6. Abre un Pull Request

### Testing

- Ejecuta las pruebas antes de hacer commit
- Asegúrate de que la app compila sin errores
- Prueba la funcionalidad en un dispositivo real si es posible

### Reportar bugs

Usa el sistema de Issues de GitHub incluyendo:
- Descripción del problema
- Pasos para reproducir
- Comportamiento esperado vs actual
- Capturas de pantalla si aplica
- Versión de Android del dispositivo

## Contacto

Para preguntas o sugerencias, abre un Issue en GitHub.
