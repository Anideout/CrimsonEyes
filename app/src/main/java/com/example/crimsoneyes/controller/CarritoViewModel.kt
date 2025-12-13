package com.example.crimsoneyes.controller

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crimsoneyes.api.CarritoApiService
import com.example.crimsoneyes.api.ItemCarritoApiService
import com.example.crimsoneyes.api.MetodoPagoApiService
import com.example.crimsoneyes.model.ItemCarrito
import com.example.crimsoneyes.model.MetodoPago
import com.example.crimsoneyes.model.Producto
import com.example.crimsoneyes.repository.BoleteRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class CarritoState(
    val items: List<ItemCarrito> = emptyList(),
    val subtotal: Int = 0,
    val envio: Int = 0,
    val total: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val carritoId: Int = 0,
    val message: String? = null,
    val metodoPagoSeleccionado: String? = null,
    val metodoPagoIdSeleccionado: Int? = null,
    val metodosPagoDisponibles: List<MetodoPago> = emptyList(),
    val cargandoMetodosPago: Boolean = false
)

class CarritoViewModel(
    private val apiService: CarritoApiService,
    private val itemCarritoService: ItemCarritoApiService,
    private val metodoPagoApiService: MetodoPagoApiService,
    val usuarioEmail: String = "",
    private val boleteRepository: BoleteRepository?
) : ViewModel() {

    private val _state = MutableStateFlow(CarritoState())
    val state: StateFlow<CarritoState> = _state.asStateFlow()

    init {
        Log.d("CarritoViewModel", "🚀 Inicializando con email: $usuarioEmail")

        cargarMetodosPagoPredefinidos()

        if (usuarioEmail.isNotEmpty()) {
            obtenerCarritoPorEmail(usuarioEmail)
        } else {
            Log.w("CarritoViewModel", "Email vacío, no se puede cargar carrito")
            _state.update {
                it.copy(error = "Usuario no autenticado")
            }
        }
    }


    private fun cargarMetodosPagoPredefinidos() {
        Log.d("CarritoViewModel", "Cargando métodos de pago predefinidos...")

        val metodosPredefinidos = listOf(
            MetodoPago(id = 1, nombre = "Transferencia"),
            MetodoPago(id = 2, nombre = "Débito")
        )

        _state.update {
            it.copy(
                metodosPagoDisponibles = metodosPredefinidos,
                cargandoMetodosPago = false
            )
        }

        Log.d("CarritoViewModel", "Métodos de pago disponibles: ${metodosPredefinidos.size}")
        metodosPredefinidos.forEach {
            Log.d("CarritoViewModel", "${it.nombre} (ID: ${it.id})")
        }
    }

    fun seleccionarMetodoPago(metodo: MetodoPago) {
        _state.update {
            it.copy(
                metodoPagoSeleccionado = metodo.nombre,
                metodoPagoIdSeleccionado = metodo.id
            )
        }
        Log.d("CarritoViewModel", "Método de pago seleccionado: ${metodo.nombre} (ID: ${metodo.id})")
    }

    fun seleccionarMetodoPagoPorNombre(nombreMetodo: String) {
        val metodo = _state.value.metodosPagoDisponibles.find { it.nombre == nombreMetodo }
        if (metodo != null) {
            seleccionarMetodoPago(metodo)
        } else {
            Log.w("CarritoViewModel", "Método de pago no encontrado: $nombreMetodo")
        }
    }

    fun obtenerMetodoPagoSeleccionado(): String? {
        return _state.value.metodoPagoSeleccionado
    }

    fun obtenerMetodoPagoIdSeleccionado(): Int? {
        return _state.value.metodoPagoIdSeleccionado
    }


    fun enviarMetodoPagoAlBackend(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val metodoSeleccionado = _state.value.metodoPagoSeleccionado
        val metodoId = _state.value.metodoPagoIdSeleccionado

        if (metodoSeleccionado == null || metodoId == null) {
            onError("No has seleccionado un método de pago")
            return
        }

        Log.d("CarritoViewModel", "Enviando método de pago al backend: $metodoSeleccionado")

        val metodo = MetodoPago(id = metodoId, nombre = metodoSeleccionado)

        metodoPagoApiService.crearMetodoPago(metodo).enqueue(
            object : Callback<Map<String, Any>> {
                override fun onResponse(
                    call: Call<Map<String, Any>>,
                    response: Response<Map<String, Any>>
                ) {
                    if (response.isSuccessful) {
                        Log.d("CarritoViewModel", "Método de pago registrado en backend")
                        onSuccess()
                    } else {
                        Log.e("CarritoViewModel", "Error al registrar método: ${response.code()}")
                        onError("Error al registrar método de pago")
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    Log.e("CarritoViewModel", "Error de conexión", t)
                    Log.w("CarritoViewModel", "Continuando con método local")
                    onSuccess()
                }
            }
        )
    }


    fun obtenerCarritoPorEmail(email: String) {
        _state.update { it.copy(isLoading = true, error = null) }
        Log.d("CarritoViewModel", "Buscando carrito para: $email")

        apiService.obtenerCarritoPorEmail(email).enqueue(
            object : Callback<Map<String, Any>> {
                override fun onResponse(
                    call: Call<Map<String, Any>>,
                    response: Response<Map<String, Any>>
                ) {
                    Log.d("CarritoViewModel", "Response code: ${response.code()}")

                    if (response.isSuccessful && response.body() != null) {
                        try {
                            val body = response.body()!!
                            Log.d("CarritoViewModel", "Response body: $body")

                            @Suppress("UNCHECKED_CAST")
                            val carritoMap = body["carrito"] as? Map<String, Any>
                            val carritoId = when (val id = carritoMap?.get("id")) {
                                is Number -> id.toInt()
                                is String -> id.toIntOrNull() ?: 0
                                else -> 0
                            }

                            Log.d("CarritoViewModel", "CarritoId obtenido: $carritoId")

                            if (carritoId > 0) {
                                _state.update { it.copy(carritoId = carritoId) }
                                cargarItemsDelCarrito(carritoId)
                            } else {
                                Log.e("CarritoViewModel", "ID de carrito inválido")
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        error = "No se encontró carrito para este usuario"
                                    )
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("CarritoViewModel", "Error al parsear respuesta", e)
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = "Error al procesar carrito: ${e.message}"
                                )
                            }
                        }
                    } else {
                        Log.e("CarritoViewModel", "Response no exitoso: ${response.code()}")
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "Error al obtener carrito: ${response.code()}"
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    Log.e("CarritoViewModel", "Error de conexión", t)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Error de conexión: ${t.message}"
                        )
                    }
                }
            }
        )
    }

    private fun cargarItemsDelCarrito(carritoId: Int) {
        _state.update { it.copy(isLoading = true, error = null) }
        Log.d("CarritoViewModel", "Cargando items del carrito: $carritoId")

        itemCarritoService.obtenerItemsDelCarrito(carritoId).enqueue(
            object : Callback<List<ItemCarrito>> {
                override fun onResponse(
                    call: Call<List<ItemCarrito>>,
                    response: Response<List<ItemCarrito>>
                ) {
                    Log.d("CarritoViewModel", "Items response code: ${response.code()}")

                    if (response.isSuccessful) {
                        try {
                            val items = response.body() ?: emptyList()
                            Log.d("CarritoViewModel", "Items cargados: ${items.size}")

                            val itemsConImagenes = items.map { item ->
                                item.copy(
                                    producto = item.producto.copy(
                                        imagenResId = obtenerImagenPorCategoria(item.producto.categoria)
                                    )
                                )
                            }

                            val nuevosTotales = calcularTotales(itemsConImagenes)
                            _state.update {
                                nuevosTotales.copy(
                                    carritoId = carritoId,
                                    isLoading = false
                                )
                            }
                        } catch (e: Exception) {
                            Log.e("CarritoViewModel", "Error al procesar items", e)
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = "Error al procesar items: ${e.message}"
                                )
                            }
                        }
                    } else {
                        Log.e("CarritoViewModel", "Error al cargar items: ${response.code()}")
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "Error al cargar items: ${response.code()}"
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<List<ItemCarrito>>, t: Throwable) {
                    Log.e("CarritoViewModel", "Error al cargar items", t)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Error de conexión: ${t.message}"
                        )
                    }
                }
            }
        )
    }

    private fun obtenerImagenPorCategoria(categoria: String): Int {
        return when (categoria) {
            "Lectura" -> com.example.crimsoneyes.R.drawable.lente
            "Redondos" -> com.example.crimsoneyes.R.drawable.redondos
            "Aceleracionista" -> com.example.crimsoneyes.R.drawable.cyber
            "Armani" -> com.example.crimsoneyes.R.drawable.armani
            "IDM" -> com.example.crimsoneyes.R.drawable.rave
            "Filtros azul" -> com.example.crimsoneyes.R.drawable.vista
            else -> com.example.crimsoneyes.R.drawable.lente
        }
    }

    fun agregarProducto(producto: Producto, cantidad: Int = 1) {
        if (producto.id <= 0 || producto.nombre.isEmpty()) {
            _state.update { it.copy(error = "Producto inválido") }
            return
        }

        val carritoIdActual = _state.value.carritoId
        if (carritoIdActual <= 0) {
            _state.update { it.copy(error = "Carrito no inicializado") }
            return
        }

        _state.update { it.copy(isLoading = true, error = null) }

        val nuevoItem = ItemCarrito(
            id = 0,
            carritoId = carritoIdActual,
            producto = producto,
            cantidad = cantidad
        )

        Log.d("CarritoViewModel", "Agregando producto: ${producto.nombre} al carrito: $carritoIdActual")

        itemCarritoService.agregarItemAlCarrito(carritoIdActual, nuevoItem).enqueue(
            object : Callback<Map<String, Any>> {
                override fun onResponse(
                    call: Call<Map<String, Any>>,
                    response: Response<Map<String, Any>>
                ) {
                    if (response.isSuccessful) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                message = "Producto agregado al carrito"
                            )
                        }
                        cargarItemsDelCarrito(carritoIdActual)
                        Log.d("CarritoViewModel", "Producto agregado exitosamente")
                    } else {
                        Log.e("CarritoViewModel", "Error al agregar: ${response.code()}")
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "Error al agregar: ${response.code()}"
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    Log.e("CarritoViewModel", "Error al agregar producto", t)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Error al agregar producto: ${t.message}"
                        )
                    }
                }
            }
        )
    }

    fun actualizarCantidad(itemId: Int, nuevaCantidad: Int) {
        if (nuevaCantidad <= 0) {
            eliminarProductoPorItemId(itemId)
            return
        }

        _state.update { it.copy(isLoading = true, error = null) }
        Log.d("CarritoViewModel", "Actualizando cantidad item $itemId a $nuevaCantidad")

        itemCarritoService.actualizarCantidad(itemId, nuevaCantidad).enqueue(
            object : Callback<Map<String, Any>> {
                override fun onResponse(
                    call: Call<Map<String, Any>>,
                    response: Response<Map<String, Any>>
                ) {
                    if (response.isSuccessful) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                message = "Cantidad actualizada"
                            )
                        }
                        val carritoId = _state.value.carritoId
                        if (carritoId > 0) {
                            cargarItemsDelCarrito(carritoId)
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "Error al actualizar: ${response.code()}"
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    Log.e("CarritoViewModel", "Error al actualizar cantidad", t)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Error al actualizar cantidad: ${t.message}"
                        )
                    }
                }
            }
        )
    }

    fun eliminarProductoPorItemId(itemId: Int) {
        _state.update { it.copy(isLoading = true, error = null) }
        Log.d("CarritoViewModel", "Eliminando item: $itemId")

        itemCarritoService.eliminarItemDelCarrito(itemId).enqueue(
            object : Callback<Map<String, Any>> {
                override fun onResponse(
                    call: Call<Map<String, Any>>,
                    response: Response<Map<String, Any>>
                ) {
                    if (response.isSuccessful) {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                message = "Producto eliminado"
                            )
                        }
                        val carritoId = _state.value.carritoId
                        if (carritoId > 0) {
                            cargarItemsDelCarrito(carritoId)
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "Error al eliminar: ${response.code()}"
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    Log.e("CarritoViewModel", "Error al eliminar item", t)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Error al eliminar: ${t.message}"
                        )
                    }
                }
            }
        )
    }

    fun vaciarCarrito() {
        val carritoId = _state.value.carritoId
        if (carritoId <= 0) {
            _state.update { it.copy(error = "Carrito no válido") }
            return
        }

        _state.update { it.copy(isLoading = true, error = null) }
        Log.d("CarritoViewModel", "Vaciando carrito: $carritoId")

        itemCarritoService.vaciarCarrito(carritoId).enqueue(
            object : Callback<Map<String, Any>> {
                override fun onResponse(
                    call: Call<Map<String, Any>>,
                    response: Response<Map<String, Any>>
                ) {
                    if (response.isSuccessful) {
                        _state.update {
                            CarritoState(
                                carritoId = carritoId,
                                metodosPagoDisponibles = _state.value.metodosPagoDisponibles
                            ).copy(
                                message = "Carrito vaciado"
                            )
                        }
                        Log.d("CarritoViewModel", "Carrito vaciado")
                    } else {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = "Error al vaciar: ${response.code()}"
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    Log.e("CarritoViewModel", "Error al vaciar carrito", t)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Error al vaciar carrito: ${t.message}"
                        )
                    }
                }
            }
        )
    }

    fun obtenerCantidadTotal(): Int {
        return _state.value.items.sumOf { it.cantidad }
    }

    fun limpiarError() {
        _state.update { it.copy(error = null, message = null) }
    }

    fun limpiarMensaje() {
        _state.update { it.copy(message = null) }
    }

    private fun calcularTotales(items: List<ItemCarrito>): CarritoState {
        val subtotal = items.sumOf { it.producto.precio * it.cantidad }
        val envio = if (items.isNotEmpty()) 3000 else 0
        val total = subtotal + envio

        return _state.value.copy(
            items = items,
            subtotal = subtotal,
            envio = envio,
            total = total
        )
    }

    fun procesarCompra() {
        Log.d("CarritoViewModel", "Procesando compra con método: ${_state.value.metodoPagoSeleccionado}")
        Log.d("CarritoViewModel", "ID Método: ${_state.value.metodoPagoIdSeleccionado}")
        Log.d("CarritoViewModel", "Items en carrito: ${_state.value.items.size}")
    }

    fun procesarCompraConDescuentoDeStock(productoViewModel: ProductoViewModel?) {
        val itemsAComprar = _state.value.items.toList()
        productoViewModel?.descontarStockDeLista(itemsAComprar)
        Log.d("CarritoViewModel", "Stock descontado para ${itemsAComprar.size} items")
        vaciarCarrito()
    }

    fun guardarBoleta() {
        val state = _state.value

        if (state.items.isEmpty()) {
            Log.w("CarritoViewModel", "No hay items para guardar boleta")
            return
        }

        if (state.metodoPagoSeleccionado == null) {
            Log.w("CarritoViewModel", "No hay método de pago seleccionado")
            return
        }

        viewModelScope.launch {
            try {
                val detallesJson = Gson().toJson(
                    state.items.map { item ->
                        mapOf(
                            "nombre" to item.producto.nombre,
                            "cantidad" to item.cantidad,
                            "precio" to item.producto.precio
                        )
                    }
                )

                boleteRepository?.insertBoleta(
                    usuarioEmail = usuarioEmail,
                    metodoPago = state.metodoPagoSeleccionado ?: "No especificado",
                    subtotal = state.subtotal,
                    envio = state.envio,
                    total = state.total,
                    cantidadProductos = state.items.size,
                    detallesProductos = detallesJson
                )

                Log.d("CarritoViewModel", "Boleta guardada exitosamente")

            } catch (ex: Exception) {
                Log.e("CarritoViewModel", "Error al guardar boleta", ex)
            }
        }
    }
}