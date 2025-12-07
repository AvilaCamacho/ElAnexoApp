# API Integration Guide

## Overview

This application integrates with a REST API to perform CRUD operations on items. By default, it uses [JSONPlaceholder](https://jsonplaceholder.typicode.com/) as a demo API, but it can be easily configured to work with any REST API.

## Configuring the API

### Change Base URL

Edit `app/src/main/java/com/elanexo/app/api/RetrofitClient.kt`:

```kotlin
private const val BASE_URL = "https://your-api.com/"
```

## API Endpoints

The application expects the following REST endpoints:

### 1. Get All Items
**Endpoint**: `GET /items`

**Response**:
```json
[
  {
    "id": 1,
    "title": "Item Title",
    "description": "Item Description",
    "imageUrl": "https://example.com/image.jpg",
    "latitude": 37.7749,
    "longitude": -122.4194,
    "createdAt": "2023-01-01T00:00:00Z",
    "updatedAt": "2023-01-01T00:00:00Z"
  }
]
```

### 2. Get Single Item
**Endpoint**: `GET /items/{id}`

**Response**:
```json
{
  "id": 1,
  "title": "Item Title",
  "description": "Item Description",
  "imageUrl": "https://example.com/image.jpg",
  "latitude": 37.7749,
  "longitude": -122.4194,
  "createdAt": "2023-01-01T00:00:00Z",
  "updatedAt": "2023-01-01T00:00:00Z"
}
```

### 3. Create Item
**Endpoint**: `POST /items`

**Request Body**:
```json
{
  "title": "New Item",
  "description": "New Description",
  "imageUrl": "https://example.com/image.jpg",
  "latitude": 37.7749,
  "longitude": -122.4194
}
```

**Response**:
```json
{
  "id": 2,
  "title": "New Item",
  "description": "New Description",
  "imageUrl": "https://example.com/image.jpg",
  "latitude": 37.7749,
  "longitude": -122.4194,
  "createdAt": "2023-01-01T00:00:00Z",
  "updatedAt": "2023-01-01T00:00:00Z"
}
```

### 4. Update Item
**Endpoint**: `PUT /items/{id}`

**Request Body**:
```json
{
  "title": "Updated Title",
  "description": "Updated Description",
  "imageUrl": "https://example.com/updated-image.jpg",
  "latitude": 40.7128,
  "longitude": -74.0060
}
```

**Response**:
```json
{
  "id": 1,
  "title": "Updated Title",
  "description": "Updated Description",
  "imageUrl": "https://example.com/updated-image.jpg",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "createdAt": "2023-01-01T00:00:00Z",
  "updatedAt": "2023-01-02T00:00:00Z"
}
```

### 5. Delete Item
**Endpoint**: `DELETE /items/{id}`

**Response**: HTTP 200/204 with empty body

## Data Models

### Item
```kotlin
data class Item(
    val id: Int? = null,
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
```

### CreateItemRequest
```kotlin
data class CreateItemRequest(
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
```

### UpdateItemRequest
```kotlin
data class UpdateItemRequest(
    val title: String,
    val description: String,
    val imageUrl: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)
```

## Error Handling

The application handles the following error scenarios:

1. **Network Errors**: Connection timeout, no internet
2. **HTTP Errors**: 4xx, 5xx status codes
3. **Parsing Errors**: Invalid JSON responses

All errors are propagated to the UI through the `Result` sealed class:

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

## Authentication (Optional)

To add authentication to API calls, modify `RetrofitClient.kt`:

```kotlin
private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("Authorization", "Bearer YOUR_TOKEN")
            .method(original.method, original.body)
            .build()
        chain.proceed(request)
    }
    .addInterceptor(loggingInterceptor)
    .build()
```

## Logging

HTTP requests and responses are logged in debug builds using `HttpLoggingInterceptor`:

```kotlin
private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}
```

To disable logging in production:

```kotlin
private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG) 
        HttpLoggingInterceptor.Level.BODY 
    else 
        HttpLoggingInterceptor.Level.NONE
}
```

## Sample Backend

For testing purposes, you can use:

1. **JSONPlaceholder** (default): https://jsonplaceholder.typicode.com/
2. **json-server**: Create local mock API
3. **Custom Backend**: Node.js, Python Flask, etc.

### Setting up json-server locally

```bash
npm install -g json-server
```

Create `db.json`:
```json
{
  "items": [
    {
      "id": 1,
      "title": "Test Item",
      "description": "Test Description",
      "latitude": 37.7749,
      "longitude": -122.4194
    }
  ]
}
```

Run server:
```bash
json-server --watch db.json --host 0.0.0.0 --port 3000
```

Update `BASE_URL`:
```kotlin
private const val BASE_URL = "http://YOUR_IP:3000/"
```

## CORS Configuration

If using a web-based backend, ensure CORS is configured to allow mobile app requests:

```javascript
// Example: Express.js
app.use(cors({
  origin: '*',
  methods: ['GET', 'POST', 'PUT', 'DELETE']
}));
```
