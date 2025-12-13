package com.example.crimsoneyes.controller

import android.util.Log
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

    private var metodoGuardado: String = ""


    fun setItemsCarrito(items: List<ItemCarrito>) {
        _uiState.value = _uiState.value.copy(itemsCarrito = items)
        calcularTotal()
    }

    fun setMetodoPago(metodo: String) {
        metodoGuardado = metodo
        _uiState.value = _uiState.value.copy(metodoPago = metodo)
    }


    fun procesarCompra(metodoPago: String) {
        val items = _uiState.value.itemsCarrito

        // Validar
        if (items.isEmpty()) {
            _uiState.value = _uiState.value.copy(error = "❌ El carrito está vacío")
            return
        }


        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(cargando = true, error = null)

            val resultado = ventaRepository.procesarCompra(
                email = usuarioEmail,
                metodoPago = metodoPago,
                itemsCarrito = items
            )

            resultado.onSuccess { venta ->
                // datos para el QR con ID y email
                val qrData = "venta:${venta.id}|${usuarioEmail}"
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
                    error = exception.message ?: "❌ Error desconocido",
                    cargando = false
                )
            }
        }
    }

    fun obtenerDetallesVenta(ventaId: Int) {
        Log.d("CheckoutViewModel", "OBTENER DETALLES DE VENTA")
        Log.d("CheckoutViewModel", "Venta ID: $ventaId")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(cargando = true, error = null)
            Log.d("CheckoutViewModel", "Iniciando carga de venta...")

            val resultado = ventaRepository.obtenerVenta(ventaId)

            resultado.onSuccess { venta ->
                Log.d("CheckoutViewModel", "Venta cargada exitosamente")
                Log.d("CheckoutViewModel", "ID: ${venta.id}, Email: ${venta.usuarioEmail}, Estado: ${venta.estado}")
                Log.d("CheckoutViewModel", "Total: ${venta.total}, Detalles: ${venta.detalles.size}")
                Log.d("CheckoutViewModel", "Método de pago backend: '${venta.metodoPago}'")

                val qrData = "venta:${venta.id}|${venta.usuarioEmail}"
                Log.d("CheckoutViewModel", "QR Data: $qrData")

                val metodoFinal = if (venta.metodoPago.isNotEmpty()) venta.metodoPago else metodoGuardado

                _uiState.value = _uiState.value.copy(
                    compraExitosa = venta.copy(metodoPago = metodoFinal),
                    cargando = false,
                    qrData = qrData,
                    metodoPago = metodoFinal
                )
            }

            resultado.onFailure { exception ->
                Log.e("CheckoutViewModel", "Error cargando venta: ${exception.message}")
                exception.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    error = "❌ ${exception.message}",
                    cargando = false
                )
            }
        }
    }



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

