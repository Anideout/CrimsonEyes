package com.example.crimsoneyes.repository

import com.example.crimsoneyes.api.VentaApiService
import com.example.crimsoneyes.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VentaRepository(
    private val apiService: VentaApiService
) {

    /**
     * PROCESAR COMPRA
     * Convierte items del carrito en una venta
     */
    suspend fun procesarCompra(
        email: String,
        metodoPago: String,
        itemsCarrito: List<ItemCarrito>
    ): Result<VentaResponse> = withContext(Dispatchers.IO) {
        try {
            // Validar que hay items
            if (itemsCarrito.isEmpty()) {
                return@withContext Result.failure(
                    Exception("El carrito está vacío")
                )
            }

            // Convertir ItemCarrito → DetalleVentaRequest
            val detalles = itemsCarrito.map { item ->
                DetalleVentaRequest(
                    productoId = item.producto.id,
                    cantidad = item.cantidad,
                    precioUnitario = item.producto.precio.toDouble()
                )
            }

            // Calcular total
            val total = detalles.sumOf { detalle ->
                detalle.cantidad * detalle.precioUnitario
            }

            // Crear request
            val ventaRequest = CreateVentaRequest(
                usuarioEmail = email,
                metodoPago = metodoPago,
                total = total,
                detalles = detalles
            )


            val response = apiService.crearVenta(ventaRequest).execute()

            // Procesar respuesta
            return@withContext if (response.isSuccessful) {
                val body = response.body()

                if (body != null && body.containsKey("venta")) {
                    val ventaData = body["venta"]

                    // Convertir el Map a VentaResponse usando Gson
                    val gson = com.google.gson.Gson()
                    val ventaJson = gson.toJson(ventaData)
                    val venta = gson.fromJson(ventaJson, VentaResponse::class.java)

                    if (venta != null) {
                        Result.success(venta)
                    } else {
                        Result.failure(Exception("No se pudo convertir la venta"))
                    }
                } else {
                    Result.failure(Exception("La respuesta no contiene 'venta'"))
                }
            } else {
                Result.failure(
                    Exception("Error ${response.code()}: ${response.message()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * OBTENER VENTA POR ID
     */
    suspend fun obtenerVenta(ventaId: Int): Result<VentaResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.obtenerVenta(ventaId).execute()

                return@withContext if (response.isSuccessful) {
                    val venta = response.body()
                    if (venta != null) {
                        Result.success(venta)
                    } else {
                        Result.failure(Exception("Venta no encontrada"))
                    }
                } else {
                    Result.failure(Exception("Error: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * OBTENER HISTORIAL DE COMPRAS DEL USUARIO
     */
    @Suppress("UNUSED")
    suspend fun obtenerVentasUsuario(email: String): Result<List<VentaResponse>> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.obtenerVentasUsuario(email).execute()

                return@withContext if (response.isSuccessful) {
                    val ventas = response.body() ?: emptyList()
                    Result.success(ventas)
                } else {
                    Result.failure(Exception("Error: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * CAMBIAR ESTADO DE VENTA (Admin)
     * Estados: PENDIENTE, CONFIRMADA, ENVIADA, ENTREGADA, CANCELADA
     */
    @Suppress("UNUSED")
    suspend fun cambiarEstado(
        ventaId: Int,
        nuevoEstado: String
    ): Result<VentaResponse> = withContext(Dispatchers.IO) {
        try {
            val estadoMap = mapOf("estado" to nuevoEstado)
            val response = apiService.actualizarEstadoVenta(ventaId, estadoMap).execute()

            return@withContext if (response.isSuccessful) {
                val body = response.body()
                // Si la respuesta es un mapa, extraer la venta
                @Suppress("UNCHECKED_CAST")
                val venta: VentaResponse? = if (body is Map<*, *>) {
                    body["venta"] as? VentaResponse
                } else {
                    null
                }

                if (venta != null) {
                    Result.success(venta)
                } else {
                    Result.failure(Exception("No se pudo cambiar estado"))
                }
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

