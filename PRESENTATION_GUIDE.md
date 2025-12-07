# Guía de Presentación - El Anexo App
## Semana 15 - Defensa del Proyecto

---

## 📋 Agenda de Presentación (15-20 minutos)

### 1. Introducción (2 minutos)
**Presentador:** [Nombre del líder del equipo]

- Nombre del proyecto: **El Anexo App**
- Objetivo: Sistema de gestión de productos con integración de sensores
- Tecnologías principales: Kotlin, Jetpack Compose, MVVM, Retrofit, GPS

---

### 2. Demostración de la Aplicación (8 minutos)

#### Paso 1: Pantalla de Productos (3 min)
**Demostrador:** [Asignar persona]

**Acciones a realizar:**
1. Abrir la aplicación
2. Mostrar lista de productos (GET)
3. Hacer clic en el botón de refresh
4. Explicar: "Aquí se obtienen los productos de la API usando Retrofit"
5. Clic en botón "+" para agregar producto (POST)
6. Llenar formulario con datos de ejemplo:
   - Nombre: "Laptop Dell"
   - Precio: "15999.99"
   - Descripción: "Laptop de alta gama"
7. Guardar y mostrar cómo se actualiza la lista
8. Seleccionar un producto y eliminarlo (DELETE)
9. Mostrar cómo desaparece de la lista

**Puntos clave a mencionar:**
- "Esta pantalla implementa las operaciones CRUD completas"
- "Usamos Retrofit para comunicarnos con la API REST"
- "El patrón MVVM separa la lógica de la interfaz"

#### Paso 2: Pantalla de Ubicación GPS (3 min)
**Demostrador:** [Asignar persona]

**Acciones a realizar:**
1. Cambiar a la pestaña "Ubicación"
2. Si es primera vez, otorgar permisos de ubicación
3. Explicar: "Aquí integramos el sensor GPS del dispositivo"
4. Clic en botón "Obtener Ubicación"
5. Mostrar las coordenadas obtenidas:
   - Latitud
   - Longitud
   - Precisión
   - Timestamp
6. Hacer clic en refresh para actualizar

**Puntos clave a mencionar:**
- "Integramos el sensor GPS como requisito del proyecto"
- "Manejamos los permisos de forma segura"
- "Los datos se actualizan en tiempo real"

#### Paso 3: Navegación y UI (2 min)
**Demostrador:** [Asignar persona]

- Mostrar la navegación entre pestañas
- Destacar el diseño Material Design 3
- Mostrar estados de loading y errores si es posible
- Mencionar que es responsive

---

### 3. Explicación Técnica (5 minutos)

#### Arquitectura MVVM (2 min)
**Presentador:** [Asignar persona experta en arquitectura]

**Mostrar en código:**
```
📁 Estructura del proyecto
├── Model (data/)
│   ├── Product.kt - Modelo de datos
│   └── Repository - Lógica de datos
├── View (ui/screens/)
│   └── Pantallas Compose
└── ViewModel (ui/viewmodel/)
    └── Lógica de presentación
```

**Explicar:**
- "El Model maneja los datos y la comunicación con la API"
- "El ViewModel prepara los datos para la Vista"
- "La View solo muestra y captura interacciones"
- "Esto hace el código más mantenible y testeable"

#### Retrofit y API REST (2 min)
**Presentador:** [Asignar persona experta en networking]

**Mostrar archivo ApiService.kt:**
```kotlin
@GET("products")          // Lectura
@POST("products")         // Creación
@PUT("products/{id}")     // Actualización
@DELETE("products/{id}")  // Eliminación
```

**Explicar:**
- "Retrofit convierte la API en funciones de Kotlin"
- "Manejamos las respuestas de forma asíncrona con coroutines"
- "CRUD completo implementado: Create, Read, Update, Delete"

#### Sensor GPS (1 min)
**Presentador:** [Asignar persona]

**Mostrar código LocationRepository.kt**

**Explicar:**
- "Usamos FusedLocationProviderClient de Google Play Services"
- "Obtenemos ubicación de alta precisión"
- "Manejamos permisos correctamente"

---

### 4. Gestión del Proyecto (3 minutos)

#### GitHub y Colaboración
**Presentador:** [Asignar persona]

**Mostrar en pantalla:**
1. Repositorio en GitHub
2. Historial de commits (mostrar colaboración)
3. README.md completo
4. Documentación adicional (ARCHITECTURE.md, CONTRIBUTING.md)
5. Workflows de CI/CD (si están configurados)

**Puntos a mencionar:**
- "Todo el código está versionado en GitHub"
- "Todos los miembros del equipo participaron activamente"
- "Documentación profesional incluida"
- "Builds automáticos configurados"

---

### 5. Sesión de Preguntas (2-5 minutos)

**Preparación para preguntas comunes:**

**P: ¿Por qué eligieron MVVM?**
R: "Es el patrón recomendado por Google para Android. Separa responsabilidades y facilita el mantenimiento."

**P: ¿Cómo funciona Retrofit?**
R: "Retrofit convierte las llamadas HTTP en funciones de Kotlin usando anotaciones. Maneja automáticamente la serialización JSON."

**P: ¿Qué hace Jetpack Compose?**
R: "Es el framework moderno de UI de Google. Permite crear interfaces de forma declarativa, similar a React."

**P: ¿Por qué GPS?**
R: "El GPS es útil para rastrear ubicaciones de productos, verificar entregas, o geolocalizar usuarios."

**P: ¿La API persiste los cambios?**
R: "Usamos una API de demostración (Fake Store API) que simula los cambios pero no los persiste realmente."

**P: ¿Cómo manejan los errores?**
R: "Usamos try-catch en los repositorios y Result<T> para propagar errores. La UI muestra mensajes apropiados."

**P: ¿Qué aprenderían diferente?**
R: "Podríamos agregar persistencia local con Room, más sensores, o autenticación de usuarios."

---

## 🎯 Checklist Pre-Presentación

### Día Anterior
- [ ] Probar la aplicación en el dispositivo a usar
- [ ] Verificar que todos los miembros conocen su parte
- [ ] Revisar que el código compila sin errores
- [ ] Preparar dispositivo Android cargado
- [ ] Tener datos de ejemplo listos para agregar
- [ ] Revisar posibles preguntas y respuestas

### 30 Minutos Antes
- [ ] Abrir Android Studio con el proyecto
- [ ] Tener el repositorio de GitHub abierto en navegador
- [ ] Instalar la app en el dispositivo de demostración
- [ ] Verificar conexión a Internet
- [ ] Habilitar permisos de ubicación
- [ ] Cerrar otras aplicaciones para evitar distracciones

### Durante la Presentación
- [ ] Hablar claro y con confianza
- [ ] Mantener contacto visual con profesores/audiencia
- [ ] No leer, explicar con propias palabras
- [ ] Mostrar entusiasmo por el proyecto
- [ ] Gestionar bien el tiempo (15-20 min)
- [ ] Todos los miembros deben participar

---

## 💡 Tips para Impresionar

### Demostración
✅ Tener la app instalada y probada
✅ Usar datos de ejemplo realistas
✅ Mostrar flujos completos (agregar → ver → eliminar)
✅ Manejar errores con gracia (ej: sin internet)

### Explicación Técnica
✅ Usar términos técnicos correctamente
✅ Relacionar con lo aprendido en clase
✅ Mencionar decisiones de diseño
✅ Mostrar código limpio y organizado

### Profesionalismo
✅ Documentación completa en GitHub
✅ README bien estructurado
✅ Código comentado (cuando necesario)
✅ Trabajo en equipo evidente

---

## ⚠️ Cosas a Evitar

❌ No memorizar un script (sonar robótico)
❌ No culpar a compañeros si algo falla
❌ No decir "no sé" sin intentar responder
❌ No perder tiempo en configuraciones
❌ No pasar del tiempo asignado
❌ No dejar a un miembro del equipo sin participar

---

## 📊 Distribución de Tareas Sugerida

**Miembro 1:** Introducción + Demo Productos + Preguntas CRUD
**Miembro 2:** Demo GPS + Explicación Sensores + Preguntas técnicas
**Miembro 3:** Arquitectura MVVM + GitHub + Preguntas diseño

*Ajustar según las fortalezas de cada miembro*

---

## 🏆 Criterios de Evaluación (Probable)

- **Funcionalidad (30%)**: La app funciona correctamente
- **Arquitectura (25%)**: MVVM bien implementado
- **Integración (20%)**: Retrofit y GPS funcionando
- **Presentación (15%)**: Claridad y profesionalismo
- **Trabajo en equipo (10%)**: Participación de todos

---

## 📝 Notas Finales

**Recuerden:**
- Vale 20% de la nota final
- Es individual pero en equipo
- Demuestren que saben lo que hicieron
- Confianza y preparación son clave

**¡Mucho éxito en su presentación!** 🚀

---

## 🔗 Recursos de Última Hora

- Repositorio: https://github.com/AvilaCamacho/ElAnexoApp
- API Usada: https://fakestoreapi.com/
- Documentación: Ver ARCHITECTURE.md y QUICK_GUIDE.md en el repo

**Contacto de emergencia:** [Agregar contacto del líder del equipo]
