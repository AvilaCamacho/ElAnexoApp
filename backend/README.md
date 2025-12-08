# API Flask para AudioMessages

Esta API proporciona operaciones CRUD completas para gestionar mensajes de audio, incluyendo metadatos y archivos de audio.

## 🚀 Instalación

### Requisitos previos
- Python 3.8 o superior
- pip (gestor de paquetes de Python)

### Pasos de instalación

1. **Navega al directorio del backend:**
```bash
cd backend
```

2. **Crea un entorno virtual (recomendado):**
```bash
python -m venv venv
```

3. **Activa el entorno virtual:**

En Linux/macOS:
```bash
source venv/bin/activate
```

En Windows:
```bash
venv\Scripts\activate
```

4. **Instala las dependencias:**
```bash
pip install -r requirements.txt
```

5. **Inicia el servidor:**
```bash
python app.py
```

El servidor estará disponible en `http://localhost:5000`

## 📋 Endpoints de la API

### 1. Listar mensajes
```
GET /messages
```
**Query params opcionales:**
- `sender`: Filtrar por remitente
- `recipient`: Filtrar por destinatario

**Ejemplo:**
```bash
curl http://localhost:5000/messages
curl http://localhost:5000/messages?sender=Juan
```

### 2. Obtener mensaje específico
```
GET /messages/<id>
```

**Ejemplo:**
```bash
curl http://localhost:5000/messages/1
```

### 3. Crear nuevo mensaje
```
POST /messages
```
**Content-Type:** `multipart/form-data`

**Campos requeridos:**
- `sender` (string): Nombre del remitente
- `recipient` (string): Nombre del destinatario
- `audio_file` (file): Archivo de audio (.mp3, .wav, .ogg, .m4a, .aac, .3gp)

**Campos opcionales:**
- `duration` (float): Duración en segundos

**Ejemplo con curl:**
```bash
curl -X POST http://localhost:5000/messages \
  -F "sender=Juan" \
  -F "recipient=Maria" \
  -F "audio_file=@/ruta/al/archivo.mp3" \
  -F "duration=45.5"
```

**Respuesta exitosa (201):**
```json
{
  "id": 1,
  "sender": "Juan",
  "recipient": "Maria",
  "filename": "archivo.mp3",
  "duration": 45.5,
  "file_size": 1048576,
  "created_at": "2024-12-08T19:30:00.000Z"
}
```

### 4. Actualizar mensaje
```
PUT /messages/<id>
```
**Content-Type:** `multipart/form-data`

**Campos opcionales:**
- `sender` (string)
- `recipient` (string)
- `audio_file` (file)
- `duration` (float)

**Ejemplo:**
```bash
curl -X PUT http://localhost:5000/messages/1 \
  -F "sender=Pedro" \
  -F "duration=60.0"
```

### 5. Eliminar mensaje
```
DELETE /messages/<id>
```

**Ejemplo:**
```bash
curl -X DELETE http://localhost:5000/messages/1
```

### 6. Servir archivo de audio
```
GET /media/<filename>
```

**Ejemplo:**
```bash
curl http://localhost:5000/media/archivo.mp3 --output descarga.mp3
```

## 🗄️ Base de datos

La aplicación utiliza SQLite con SQLAlchemy ORM. La base de datos (`audio_messages.db`) se crea automáticamente al iniciar el servidor por primera vez.

### Estructura de la tabla `audio_messages`:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Integer | ID único (clave primaria) |
| sender | String(100) | Nombre del remitente |
| recipient | String(100) | Nombre del destinatario |
| filename | String(255) | Nombre del archivo (único) |
| duration | Float | Duración en segundos |
| file_size | Integer | Tamaño en bytes |
| created_at | DateTime | Fecha de creación |

## 📁 Estructura de directorios

```
backend/
├── app.py              # Aplicación Flask principal
├── models.py           # Modelo de base de datos
├── requirements.txt    # Dependencias
├── README.md          # Este archivo
├── uploads/           # Archivos de audio (creado automáticamente)
└── audio_messages.db  # Base de datos SQLite (creado automáticamente)
```

## 🔧 Uso con el emulador de Android

Para conectar la app Android al servidor Flask desde un emulador:

1. **Asegúrate de que el servidor Flask esté corriendo en tu máquina:**
```bash
python app.py
```

2. **En el emulador de Android, usa la dirección especial:**
```
http://10.0.2.2:5000
```

**Nota:** `10.0.2.2` es la dirección IP especial que el emulador Android usa para referirse a `localhost` en la máquina host.

3. **Para dispositivos físicos, usa la IP de tu red local:**
```bash
# Encuentra tu IP local (Linux/macOS)
ifconfig | grep "inet "

# Encuentra tu IP local (Windows)
ipconfig
```

Luego usa: `http://TU_IP_LOCAL:5000`

## 🛡️ Seguridad

⚠️ **Nota:** Esta implementación es para propósitos de desarrollo y demostración. En producción, considera:
- Autenticación y autorización
- Validación adicional de archivos
- Límites de tasa (rate limiting)
- HTTPS/TLS
- Configuración de CORS apropiada
- Variables de entorno para configuración sensible

## 📝 Formatos de audio soportados

- MP3 (.mp3)
- WAV (.wav)
- OGG (.ogg)
- M4A (.m4a)
- AAC (.aac)
- 3GP (.3gp)

## ❌ Manejo de errores

La API devuelve códigos HTTP estándar:
- `200 OK`: Operación exitosa
- `201 Created`: Recurso creado exitosamente
- `400 Bad Request`: Datos inválidos
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error del servidor

Todas las respuestas de error incluyen un objeto JSON con un campo `error` descriptivo.

## 🧪 Pruebas

Puedes probar la API usando:
- **curl** (ejemplos incluidos arriba)
- **Postman** o **Insomnia** (herramientas GUI)
- **Cliente Android** (ejemplos en la carpeta `android/`)

## 📱 Integración con Android

Ver la carpeta `android/` en la raíz del proyecto para ejemplos de:
- Interface Retrofit (`ApiService.kt`)
- Cliente Retrofit (`RetrofitClient.kt`)
- Uso de ejemplo (`ExampleUsage.kt`)
