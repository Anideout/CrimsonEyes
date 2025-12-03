## 📱 ACTUALIZACIÓN DE ENDPOINTS - CrimsonEyes Frontend Kotlin

**Fecha:** 24 de Noviembre de 2025  
**Cambios realizados:** Sincronización con nuevos endpoints del backend  
**Estado:** ✅ Completado

---

## 🎯 RESUMEN DE CAMBIOS

Se han actualizado y creado los siguientes servicios API para consumir los nuevos y mejorados endpoints del backend Spring Boot:

### ✅ Servicios Actualizados:
1. **ProductoApiService** - Sin cambios (ya era completo)
2. **CarritoApiService** - Mejorado con mejor documentación
3. **ItemCarritoApiService** - Sin cambios (ya era completo)
4. **MetodoPagoApiService** - Sin cambios (ya era completo)

### ✨ Nuevos Servicios Creados:
5. **UsuarioApiService** - Consume endpoints de usuario
6. **RecetaApiService** - Consume endpoints de recetas

### 📦 ApiRepository Actualizado:
- Agregadas referencias a `usuarioApi` y `recetaApi`
- Mantiene compatibilidad con servicios anteriores

---

## 📋 ENDPOINTS DISPONIBLES POR SERVICIO

### 1️⃣ PRODUCTOS - `ApiRepository.productoApi`

```kotlin
// Obtener todos los productos
productoApi.obtenerTodosLosProductos(): Call<List<Producto>>
// GET /api/v1/productos

// Obtener un producto específico por ID
productoApi.obtenerProductoPorId(id: Int): Call<Producto>
// GET /api/v1/productos/{id}

// Crear nuevo producto
productoApi.crearProducto(producto: Producto): Call<Map<String, Any>>
// POST /api/v1/productos

// Actualizar producto existente
productoApi.actualizarProducto(producto: Producto): Call<Map<String, Any>>
// PUT /api/v1/productos

// Eliminar producto por ID
productoApi.eliminarProducto(id: Int): Call<Map<String, Any>>
// DELETE /api/v1/productos/{id}
```

---

### 2️⃣ CARRITOS - `ApiRepository.carritoApi`

```kotlin
// Obtener todos los carritos
carritoApi.obtenerTodosLosCarritos(): Call<List<Carrito>>
// GET /api/v1/carritos

// Obtener carrito por ID
carritoApi.obtenerCarritoPorId(carritoId: Int): Call<Carrito>
// GET /api/v1/carritos/{id}

// Obtener carrito de un usuario por EMAIL (MÁS USADO)
carritoApi.obtenerCarritoPorUsuarioEmail(email: String): Call<Carrito>
// GET /api/v1/carritos/usuario/{email}

// Crear nuevo carrito
carritoApi.crearCarrito(carrito: Carrito): Call<Map<String, Any>>
// POST /api/v1/carritos
```

**Ejemplo de uso:**
```kotlin
val repo = ApiRepository()
repo.carritoApi.obtenerCarritoPorUsuarioEmail("usuario@ejemplo.com").enqueue(
    object : Callback<Carrito> {
        override fun onResponse(call: Call<Carrito>, response: Response<Carrito>) {
            if (response.isSuccessful) {
                val carrito = response.body()
                // Usar el carrito...
            }
        }
        
        override fun onFailure(call: Call<Carrito>, t: Throwable) {
            Log.e("Error", t.message ?: "Error desconocido")
        }
    }
)
```

---

### 3️⃣ ITEMS DEL CARRITO - `ApiRepository.itemCarritoApi`

```kotlin
// Obtener todos los items de un carrito específico
itemCarritoApi.obtenerItemsDelCarrito(carritoId: Int): Call<List<ItemCarrito>>
// GET /api/v1/items-carrito/carrito/{carritoId}

// Agregar item al carrito (suma cantidades si existe)
itemCarritoApi.agregarItemAlCarrito(item: ItemCarrito): Call<Map<String, Any>>
// POST /api/v1/items-carrito

// Eliminar item del carrito
itemCarritoApi.eliminarItemDelCarrito(id: Int): Call<Map<String, Any>>
// DELETE /api/v1/items-carrito/{id}
```

---

### 4️⃣ MÉTODOS DE PAGO - `ApiRepository.metodoPagoApi`

```kotlin
// Obtener todos los métodos de pago disponibles
metodoPagoApi.obtenerTodosLosMetodos(): Call<List<MetodoPago>>
// GET /api/v1/metodos-pago

// Obtener método de pago por ID
metodoPagoApi.obtenerMetodoPorId(id: Int): Call<MetodoPago>
// GET /api/v1/metodos-pago/{id}

// Crear nuevo método de pago
metodoPagoApi.crearMetodoPago(metodo: MetodoPago): Call<Map<String, Any>>
// POST /api/v1/metodos-pago
```

---

### 5️⃣ USUARIO - `ApiRepository.usuarioApi` ✨ NUEVO

```kotlin
// Obtener lista de todos los usuarios
usuarioApi.obtenerTodosLosUsuarios(): Call<List<Usuario>>
// GET /usuario

// Login - Validar credenciales
usuarioApi.validarCredenciales(loginRequest: LoginRequest): Call<Usuario>
// POST /usuario/login
// Body: { "email": "...", "password": "..." }

// Registrar nuevo usuario
usuarioApi.registrarUsuario(usuario: Usuario): Call<Map<String, Any>>
// POST /usuario/register
// Body: { "email": "...", "password": "...", "nombre": "...", "rut": "...", "telefono": ... }

// Editar perfil de usuario
usuarioApi.editarPerfil(email: String, usuario: Usuario): Call<Map<String, Any>>
// PUT /usuario/{email}/editar
```

**Ejemplo de Login:**
```kotlin
val repo = ApiRepository()
val loginRequest = LoginRequest(
    email = "usuario@ejemplo.com",
    password = "password123"
)

repo.usuarioApi.validarCredenciales(loginRequest).enqueue(
    object : Callback<Usuario> {
        override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
            if (response.isSuccessful) {
                val usuario = response.body()
                Log.d("Login", "¡Bienvenido ${usuario?.nombre}!")
            }
        }
        
        override fun onFailure(call: Call<Usuario>, t: Throwable) {
            Log.e("Login Error", t.message ?: "Error en login")
        }
    }
)
```

**Ejemplo de Registro:**
```kotlin
val nuevoUsuario = Usuario(
    email = "nuevo@ejemplo.com",
    password = "password123",
    nombre = "Juan Pérez",
    rut = "12345678-9",
    telefono = 987654321
)

repo.usuarioApi.registrarUsuario(nuevoUsuario).enqueue(
    object : Callback<Map<String, Any>> {
        override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
            if (response.isSuccessful) {
                Log.d("Register", "Usuario registrado exitosamente")
            }
        }
        
        override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
            Log.e("Register Error", t.message ?: "Error en registro")
        }
    }
)
```

---

### 6️⃣ RECETAS - `ApiRepository.recetaApi` ✨ NUEVO

```kotlin
// Crear nueva receta
recetaApi.crearReceta(receta: Receta): Call<Map<String, Any>>
// POST /recetas
```

**Ejemplo de crear receta:**
```kotlin
val receta = Receta(
    id = "",  // Se genera automáticamente en el backend
    userId = "usuario@ejemplo.com",
    title = "Receta de Gafas Perfectas",
    body = "Instrucciones detalladas para elegir gafas..."
)

repo.recetaApi.crearReceta(receta).enqueue(
    object : Callback<Map<String, Any>> {
        override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
            if (response.isSuccessful) {
                Log.d("Receta", "¡Receta creada exitosamente!")
            }
        }
        
        override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
            Log.e("Receta Error", t.message ?: "Error creando receta")
        }
    }
)
```

---

## 🔄 FLUJO DE COMPRA COMPLETO (CON NUEVOS ENDPOINTS)

```
1. USUARIO INICIA SESIÓN
   ↓
   usuarioApi.validarCredenciales(loginRequest)
   GET /usuario/login
   
   ↓ Si falla:
   usuarioApi.registrarUsuario(usuario)
   POST /usuario/register

2. OBTENER O CREAR CARRITO
   ↓
   carritoApi.obtenerCarritoPorUsuarioEmail(email)
   GET /api/v1/carritos/usuario/{email}
   
   ↓ Si no existe, crear:
   carritoApi.crearCarrito(carrito)
   POST /api/v1/carritos

3. VER PRODUCTOS DISPONIBLES
   ↓
   productoApi.obtenerTodosLosProductos()
   GET /api/v1/productos

4. AGREGAR PRODUCTO AL CARRITO
   ↓
   itemCarritoApi.agregarItemAlCarrito(item)
   POST /api/v1/items-carrito

5. VER ITEMS EN CARRITO
   ↓
   itemCarritoApi.obtenerItemsDelCarrito(carritoId)
   GET /api/v1/items-carrito/carrito/{carritoId}

6. SELECCIONAR MÉTODO DE PAGO (EN CHECKOUT)
   ↓
   metodoPagoApi.obtenerTodosLosMetodos()
   GET /api/v1/metodos-pago

7. USUARIO PUEDE:
   - Eliminar items: itemCarritoApi.eliminarItemDelCarrito(id)
   - Editar perfil: usuarioApi.editarPerfil(email, usuario)
   - Ver detalles de producto: productoApi.obtenerProductoPorId(id)
```

---

## 📝 MODELOS KOTLIN ACTUALIZADOS

### Usuario.kt
```kotlin
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey val email: String,
    val password: String = "",
    val rut: String = "",
    val nombre: String = "",
    val telefono: Int = 0,
    val id: Long = 0
)
```

### LoginRequest.kt
```kotlin
data class LoginRequest(
    val email: String = "",
    val password: String = ""
)
```

### Receta.kt
```kotlin
@Entity(tableName = "recetas")
data class Receta(
    @PrimaryKey val id: String = "",
    val userId: String = "",
    val title: String = "",
    val body: String = ""
)
```

---

## 🛠️ CÓMO USAR EN TUS VIEWMODELS

### Ejemplo en LoginViewModel

```kotlin
class LoginViewModel(private val repository: UsuarioRepository): ViewModel() {
    
    fun login(email: String, password: String) {
        val loginRequest = LoginRequest(email = email, password = password)
        
        ApiRepository.usuarioApi.validarCredenciales(loginRequest).enqueue(
            object : Callback<Usuario> {
                override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                    if (response.isSuccessful) {
                        val usuario = response.body()
                        // Guardar en base de datos local
                        viewModelScope.launch {
                            repository.insert(usuario)
                        }
                        _loginState.value = LoginState.Success(usuario)
                    } else {
                        _loginState.value = LoginState.Error("Credenciales inválidas")
                    }
                }
                
                override fun onFailure(call: Call<Usuario>, t: Throwable) {
                    _loginState.value = LoginState.Error(t.message ?: "Error de conexión")
                }
            }
        )
    }
}
```

### Ejemplo en CarritoViewModel

```kotlin
class CarritoViewModel : ViewModel() {
    
    fun obtenerCarritoDelUsuario(email: String) {
        ApiRepository.carritoApi.obtenerCarritoPorUsuarioEmail(email).enqueue(
            object : Callback<Carrito> {
                override fun onResponse(call: Call<Carrito>, response: Response<Carrito>) {
                    if (response.isSuccessful) {
                        val carrito = response.body()
                        _carrito.value = carrito
                        // Ahora obtener items del carrito
                        if (carrito != null) {
                            obtenerItemsDelCarrito(carrito.id)
                        }
                    }
                }
                
                override fun onFailure(call: Call<Carrito>, t: Throwable) {
                    Log.e("Error", t.message ?: "Error obteniendo carrito")
                }
            }
        )
    }
    
    fun obtenerItemsDelCarrito(carritoId: Int) {
        ApiRepository.itemCarritoApi.obtenerItemsDelCarrito(carritoId).enqueue(
            object : Callback<List<ItemCarrito>> {
                override fun onResponse(call: Call<List<ItemCarrito>>, response: Response<List<ItemCarrito>>) {
                    if (response.isSuccessful) {
                        _items.value = response.body() ?: emptyList()
                    }
                }
                
                override fun onFailure(call: Call<List<ItemCarrito>>, t: Throwable) {
                    Log.e("Error", t.message ?: "Error obteniendo items")
                }
            }
        )
    }
}
```

---

## 📂 ARCHIVOS MODIFICADOS

✅ **Creados:**
- `/network/api/UsuarioApiService.kt` - Nuevo servicio para endpoints de usuario
- `/network/api/RecetaApiService.kt` - Nuevo servicio para endpoints de recetas

✅ **Actualizados:**
- `/network/api/CarritoApiService.kt` - Mejorada documentación
- `/network/ApiRepository.kt` - Agregadas referencias a usuarioApi y recetaApi
- `/model/Usuario.kt` - Actualizado para coincidir con UsuarioDTO del backend
- `/model/Receta.kt` - Actualizado para coincidir con modelo del backend

✅ **Sin cambios (ya estaban completos):**
- `ProductoApiService.kt`
- `ItemCarritoApiService.kt`
- `MetodoPagoApiService.kt`

---

## 🚀 PRÓXIMOS PASOS

1. **Integra en tus ViewModels** - Usa `ApiRepository` en lugar de llamadas directas
2. **Implementa manejo de errores** - Captura excepciones y muestra feedback al usuario
3. **Agrega loading states** - Muestra indicadores de carga mientras se espera respuesta
4. **Sincroniza con base de datos local** - Guarda datos del backend en Room
5. **Implementa autenticación persistente** - Guarda token/sesión del usuario

---

## ⚠️ NOTAS IMPORTANTES

1. **Validación de campos:** Asegúrate de que los campos no sean nulos antes de enviar
2. **Manejo de errores:** Siempre implementa `onFailure` para capturar errores de red
3. **IDs automáticos:** El backend genera IDs si envías cadena vacía o "0"
4. **Restricciones de datos:**
   - `email` es la clave primaria de Usuario (único)
   - `carritoId` no puede ser nulo
   - `stock` se actualiza manualmente desde admin

---

## 📞 SOPORTE

Si tienes dudas sobre qué endpoint usar o cómo implementarlo:
- Revisa los ejemplos en este documento
- Consulta la documentación del backend: `/backend/API_README.md`
- Verifica los DTOs en el backend para ver qué campos son necesarios

¡Éxito integrando los endpoints en tu app Kotlin! 🎉
