package com.example.crimsoneyes.controller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.crimsoneyes.R
import com.example.crimsoneyes.api.ApiRepository
import com.example.crimsoneyes.model.ItemCarrito
import com.example.crimsoneyes.model.Producto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class ProductoUiState(
    val list: List<Producto> = emptyList(),
    val isListLoading: Boolean = false,
    val listError: String? = null,
    val productoSeleccionado: Producto? = null,
    val categoriaSeleccionada: String = "Todos",
    val carritoCount: Int = 0,
    val mensajeCompra: String? = null,
    val error: String? = null,
    val origen: String = "local"
)

class ProductoViewModel(
    application: Application,
    private val carritoViewModel: CarritoViewModel
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(ProductoUiState())
    val state: StateFlow<ProductoUiState> = _state.asStateFlow()

    private val imagenesPorCategoria = mapOf(
        "Lectura" to R.drawable.lente,
        "Redondos" to R.drawable.redondos,
        "Aceleracionista" to R.drawable.cyber,
        "Armani" to R.drawable.armani,
        "IDM" to R.drawable.rave,
        "Filtros azul" to R.drawable.vista
    )

    // Productos de respaldo (si el servidor falla)
    private val productosFijos = listOf(
        Producto(1, "Lentes de Lectura", 30000, "Lentes ideales para lecturas prolongadas...", R.drawable.lente, 15, "Lectura"),
        Producto(2, "Lentes Redondos", 50000, "Estilo minimalista retro...", R.drawable.redondos, 10, "Redondos"),
        Producto(3, "Lentes Futuristas", 15000, "Inspirados en estética cyberpunk...", R.drawable.cyber, 22, "Aceleracionista"),
        Producto(4, "Lentes Armani VE4361", 30000, "Elegancia y diseño clásico italiano.", R.drawable.armani, 15, "Armani"),
        Producto(5, "Lentes Rave's", 30000, "Cristales espejados para fiestas electrónicas.", R.drawable.rave, 15, "IDM"),
        Producto(6, "Lentes Filtro", 30000, "Protección contra luz azul.", R.drawable.vista, 15, "Filtros azul")
    )

    // lista de productos(para filtrado)
    private var listaCompletaProductos = mutableListOf<Producto>()

    init {
        cargarProductos()
        observarCarrito()
    }

    private fun observarCarrito() {
        viewModelScope.launch {
            carritoViewModel.state.collect { carrito ->
                _state.update {
                    it.copy(carritoCount = carrito.items.sumOf { item -> item.cantidad })
                }
            }
        }
    }

    fun cargarProductos() {
        _state.update { it.copy(isListLoading = true, listError = null) }

        ApiRepository.productoApi.obtenerTodosLosProductos()
            .enqueue(object : Callback<List<Producto>> {
                override fun onResponse(
                    call: Call<List<Producto>>,
                    response: Response<List<Producto>>
                ) {
                    if (!response.isSuccessful) {
                        usarProductosLocales("Error ${response.code()}: ${response.message()}")
                        return
                    }

                    val productosServer = response.body() ?: emptyList()

                    if (productosServer.isEmpty()) {
                        listaCompletaProductos = productosFijos.toMutableList()
                        usarProductosLocales("Servidor sin productos")
                        return
                    }

                    // Asignar imágenes a productos según categoría
                    val productosConImagenes = productosServer.map { producto ->
                        val imagenId = imagenesPorCategoria[producto.categoria] ?: R.drawable.lente
                        producto.copy(imagenResId = imagenId)
                    }

                    // Guardar lista completa para filtrado
                    listaCompletaProductos = productosConImagenes.toMutableList()

                    _state.update {
                        it.copy(
                            list = productosConImagenes,
                            isListLoading = false,
                            listError = null,
                            origen = "server"
                        )
                    }
                }

                override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                    usarProductosLocales("Error de conexión: ${t.localizedMessage}")
                }
            })
    }

    private fun usarProductosLocales(mensaje: String) {
        listaCompletaProductos = productosFijos.toMutableList()
        _state.update {
            it.copy(
                list = productosFijos,
                isListLoading = false,
                listError = mensaje,
                origen = "local"
            )
        }
    }

    fun seleccionarProducto(producto: Producto) {
        _state.update { it.copy(productoSeleccionado = producto) }
    }

    fun limpiarProductoSeleccionado() {
        _state.update { it.copy(productoSeleccionado = null) }
    }

    fun agregarAlCarrito(producto: Producto) {
        viewModelScope.launch {
            try {
                if (producto.stock <= 0) {
                    _state.update { it.copy(error = "No hay stock disponible") }
                    return@launch
                }

                carritoViewModel.agregarProducto(producto)

                // Decrementar stock del producto
                val productoActualizado = producto.copy(stock = producto.stock - 1)

                // Actualizar en la lista completa
                listaCompletaProductos = listaCompletaProductos.map { p ->
                    if (p.id == producto.id) productoActualizado else p
                }.toMutableList()

                // Actualizar la lista mostrada (filtro actual)
                val listaActualizada = _state.value.list.map { p ->
                    if (p.id == producto.id) productoActualizado else p
                }

                _state.update {
                    it.copy(
                        list = listaActualizada,
                        productoSeleccionado = if (it.productoSeleccionado?.id == producto.id)
                            productoActualizado else it.productoSeleccionado,
                        mensajeCompra = "${producto.nombre} agregado al carrito"
                    )
                }

                delay(3000)
                _state.update { it.copy(mensajeCompra = null) }

            } catch (ex: Exception) {
                _state.update {
                    it.copy(error = "Error al agregar al carrito: ${ex.message}")
                }
            }
        }
    }

    fun filtarPorCategoria(categoria: String) {
        _state.update { it.copy(categoriaSeleccionada = categoria, isListLoading = true) }

        viewModelScope.launch {
            try {
                delay(200)

                // Usar lista completa guardada
                val listaCompleta = if (listaCompletaProductos.isNotEmpty()) {
                    listaCompletaProductos
                } else {
                    productosFijos
                }

                val productosFiltrados = if (categoria == "Todos") {
                    listaCompleta
                } else {
                    listaCompleta.filter { it.categoria == categoria }
                }

                _state.update {
                    it.copy(
                        list = productosFiltrados,
                        isListLoading = false
                    )
                }

            } catch (ex: Exception) {
                _state.update {
                    it.copy(
                        isListLoading = false,
                        listError = ex.message ?: "Error al filtrar productos"
                    )
                }
            }
        }
    }

    fun cleanError() {
        _state.update { it.copy(error = null, listError = null) }
    }

    fun descontarStockDeLista(items: List<ItemCarrito>) {
        val productosActualizados = _state.value.list.map { producto ->
            val cantidadVendida = items
                .filter { it.producto.id == producto.id }
                .sumOf { it.cantidad }

            if (cantidadVendida > 0) {
                producto.copy(stock = maxOf(0, producto.stock - cantidadVendida))
            } else {
                producto
            }
        }

        _state.update {
            it.copy(list = productosActualizados)
        }
    }
}