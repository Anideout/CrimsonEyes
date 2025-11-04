package com.example.crimsoneyes.controller

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.crimsoneyes.R
import com.example.crimsoneyes.model.Producto
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductoUiState(

    val list: List<Producto> = emptyList(),
    val isListLoading: Boolean = false,
    val listError: String? = null,

    val productoSeleccionado: Producto? = null,
    val categoriaSeleccionada: String = "Todos",
    val carritoCount: Int = 0,
    val mensajeCompra: String? = null,

    val error: String? = null

)

class ProductoViewModel(application: Application): AndroidViewModel(application) {
    private val _state = MutableStateFlow(ProductoUiState())
    val state: StateFlow<ProductoUiState> = _state.asStateFlow()

    private val productosFijos = listOf(
        Producto(
            id = 1,
            nombre = "Lentes de Lectura",
            descripcion = "Lentes ideales para lecturas prolongadas y de alto requerimiento, con filtro anti-reflejo y protección contra luz azul",
            precio = 30000,
            imagenResId = R.drawable.lente,
            stock = 15,
            categoria = "Lectura"
        ),
        Producto(
            id = 2,
            nombre = "Lentes Redondos",
            descripcion = "Estilo minimalista y con aire de nostalgia retro, evocando un satirico suspiro de los 70's",
            precio = 50000,
            imagenResId = R.drawable.redondos,
            stock = 10,
            categoria = "Redondos"
        ),
        Producto(
            id = 3,
            nombre = "Lentes Futuristas",
            descripcion = "Inspirados en la estética cyberpunk, simulando un visor de poligonos que devienen. Audaces, vanguardistas y diseñados para destacar. Tecnología y moda se funden en una sola mirada.",
            precio = 15000,
            imagenResId = R.drawable.cyber,
            stock = 22,
            categoria = "Aceleracionista"
        ),
        Producto(
            id = 4,
            nombre = "Lentes Armani VE4361",
            descripcion = "Elegancia en cada detalle. Combinando diseño clásico italiano con materiales de alta gama. Su estructura ligera y acabado metálicos reflejan distinción y elegancia. Perfectos para entornos formales o un look urbano sofisticado.",
            precio = 30000,
            imagenResId = R.drawable.armani,
            stock = 15,
            categoria = "Armani"
        ),
        Producto(
            id = 5,
            nombre = "Lentes Rave's",
            descripcion = "Lentes de forma cuadrada y con cristales espejados o de colores neón, pensados para brillar bajo luces estroboscópicas. Ideal para camuflarse en la cultura electrónica. ",
            precio = 30000,
            imagenResId = R.drawable.rave,

            stock = 15,
            categoria = "IDM"
        ),
        Producto(
            id = 6,
            nombre = "Lentes Filtro",
            descripcion = "Diseñados para la era digital, lentes que protegen tus ojos del brillo de pantallas y dispositivos. Diseño discreto y moderno que los hace ideales para uso diario, combinando salud visual con estilo. Ideal para reducir la fatiga ocular y aportan una estética limpia ",
            precio = 30000,
            imagenResId = R.drawable.vista,
            stock = 15,
            categoria = "Filtros azul"
        ),
    )

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _state.update {it.copy(isListLoading = true, listError = null) }

            try{
                delay(300)

                _state.update {
                    it.copy(
                        list = productosFijos,
                        isListLoading = false
                    )
                }
            }catch(ex: Exception) {
                _state.update {
                    it.copy(
                        isListLoading = false,
                        listError = ex.message ?: "Error..."
                    )
                }
            }
        }
    }

    fun seleccionarProducto(producto: Producto) {
        _state.update { it.copy(productoSeleccionado = producto) }
    }

    fun limpiarProductoSeleccionado() {
        _state.update { it.copy(productoSeleccionado = null)}
    }

    fun agregarAlCarrito(producto: Producto) {
        viewModelScope.launch {
            try {
                if(producto.stock <= 0) {
                    _state.update {
                        it.copy(error = "No hay stock..")
                    }
                    return@launch
                }

                _state.update {
                    it.copy(
                        carritoCount = it.carritoCount + 1,
                        mensajeCompra = "${producto.nombre} Agregado al carrito!"
                    )
                }

                delay(3000)
                _state.update { it.copy(mensajeCompra = null) }
            }catch(ex: Exception) {
                _state.update {
                    it.copy(error = "Error al agregar al carrito... ${ex.message}")
                }
            }
        }
    }

    fun filtarPorCategoria(categoria: String) {
        _state.update { it.copy(categoriaSeleccionada = categoria, isListLoading =  true) }

        viewModelScope.launch {
            try {
                delay(200)

                val productosFiltrados = if (categoria == "Todos") {
                    productosFijos
                }else {
                    productosFijos.filter { it.categoria == categoria }
                }

                _state.update {
                    it.copy(
                        list = productosFiltrados,
                        isListLoading = false
                    )
                }
            }catch (ex: Exception) {
                _state.update {
                    it.copy(
                        isListLoading = false,
                        listError = ex.message ?: "error..."
                    )
                }
            }
        }
    }

    fun cleanError() {
        _state.update { it.copy(error = null, listError = null)}
    }
}