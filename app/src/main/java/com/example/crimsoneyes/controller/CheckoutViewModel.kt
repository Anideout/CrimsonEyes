package com.example.crimsoneyes.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crimsoneyes.model.ItemCarrito
import com.example.crimsoneyes.model.VentaResponse
import com.example.crimsoneyes.repository.VentaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CheckoutUiState(
    val compraExitosa: VentaResponse? = null,
    val error: String? = null,
    val cargando: Boolean = false,
    val subtotal: Double = 0.0,
    val envio: Double = 3000.0,
    val total: Double = 0.0,
    val itemsCarrito: List<ItemCarrito> = emptyList(),
    val qrData: String? = null,
    val metodoPago: String = ""
)

class CheckoutViewModel(
    private val ventaRepository: VentaRepository,
    val usuarioEmail: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    // ============= MÉTODOS =============

    fun setItemsCarrito(items: List<ItemCarrito>) {
        _uiState.value = _uiState.value.copy(itemsCarrito = items)
        calcularTotal()
    }

    fun setMetodoPago(metodo: String) {
        _uiState.value = _uiState.value.copy(metodoPago = metodo)
    }


    fun procesarCompra(metodoPago: String) {
        val items = _uiState.value.itemsCarrito

        // Validar
        if (items.isEmpty()) {
            _uiState.value = _uiState.value.copy(error = "El carrito está vacío")
            return
        }


        // Procesar en background
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(cargando = true, error = null)

            val resultado = ventaRepository.procesarCompra(
                email = usuarioEmail,
                metodoPago = metodoPago,
                itemsCarrito = items
            )

            resultado.onSuccess { venta ->
                // Generar datos para el QR (solo ID de venta para simplificar)
                val qrData = "venta:${venta.id}"
                // Si la venta no tiene metodoPago (porque el backend no lo devuelve), agregarlo manualmente
                val ventaConMetodo = venta.copy(metodoPago = metodoPago)
                _uiState.value = _uiState.value.copy(
                    compraExitosa = ventaConMetodo,
                    total = ventaConMetodo.total,
                    cargando = false,
                    qrData = qrData,
                    metodoPago = metodoPago
                )
            }

            resultado.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    error = exception.message ?: "Error desconocido",
                    cargando = false
                )
            }
        }
    }

    fun obtenerDetallesVenta(ventaId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(cargando = true, error = null)

            val resultado = ventaRepository.obtenerVenta(ventaId)

            resultado.onSuccess { venta ->
                val qrData = "venta:${venta.id}"
                _uiState.value = _uiState.value.copy(
                    compraExitosa = venta,
                    cargando = false,
                    qrData = qrData,
                    metodoPago = venta.metodoPago
                )
            }

            resultado.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    error = "${exception.message}",
                    cargando = false
                )
            }
        }
    }

    // ============= PRIVADO =============

    private fun calcularTotal() {
        val items = _uiState.value.itemsCarrito
        val subtotal = items.sumOf {
            (it.cantidad * it.producto.precio).toDouble()
        }
        val envio = 3000.0
        val total = subtotal + envio
        _uiState.value = _uiState.value.copy(
            subtotal = subtotal,
            envio = envio,
            total = total
        )
    }

    fun resetear() {
        _uiState.value = CheckoutUiState()
    }

    fun limpiarError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

