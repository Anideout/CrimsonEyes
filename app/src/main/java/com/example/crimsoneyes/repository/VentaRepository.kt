package com.example.crimsoneyes.repository

import android.util.Log
import com.example.crimsoneyes.api.VentaApiService
import com.example.crimsoneyes.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VentaRepository(
    private val apiService: VentaApiService
) {

    suspend fun procesarCompra(
        email: String,
        metodoPago: String,
        itemsCarrito: List<ItemCarrito>
    ): Result<VentaResponse> = withContext(Dispatchers.IO) {
        try {
            Log.d("VentaRepository", "=== PROCESAR COMPRA ===")
            Log.d("VentaRepository", "Email: $email, Metodo: $metodoPago, Items: ${itemsCarrito.size}")


            if (itemsCarrito.isEmpty()) {
                Log.e("VentaRepository", "❌ Carrito vacío")
                return@withContext Result.failure(
                    Exception("❌ El carrito está vacío")
                )
            }

            val detalles = itemsCarrito.map { item ->
                DetalleVentaRequest(
                    productoId = item.producto.id,
                    cantidad = item.cantidad,
                    precioUnitario = item.producto.precio.toDouble()
                )
            }

            val total = detalles.sumOf { detalle ->
                detalle.cantidad * detalle.precioUnitario
            }

            Log.d("VentaRepository", "Total calculado: $total")

            val ventaRequest = CreateVentaRequest(
                usuarioEmail = email,
                metodoPago = metodoPago,
                total = total,
                detalles = detalles
            )

            Log.d("VentaRepository", "Enviando request al servidor...")
            val response = apiService.crearVenta(ventaRequest).execute()

            return@withContext if (response.isSuccessful) {
                val body = response.body()
                Log.d("VentaRepository", "Respuesta recibida: ${body?.keys}")

                if (body != null && body.containsKey("venta")) {
                    val ventaData = body["venta"]

                    val gson = com.google.gson.Gson()
                    val ventaJson = gson.toJson(ventaData)
                    val venta = gson.fromJson(ventaJson, VentaResponse::class.java)

                    if (venta != null) {
                        Log.d("VentaRepository", "Compra procesada - Venta ID: ${venta.id}")
                        Result.success(venta)
                    } else {
                        Log.e("VentaRepository", "No se pudo convertir la venta")
                        Result.failure(Exception("No se pudo convertir la venta"))
                    }
                } else {
                    Log.e("VentaRepository", "La respuesta no contiene 'venta'")
                    Result.failure(Exception("La respuesta no contiene 'venta'"))
                }
            } else {
                Log.e("VentaRepository", "Error HTTP ${response.code()}: ${response.message()}")
                Result.failure(
                    Exception("Error ${response.code()}: ${response.message()}")
                )
            }
        } catch (e: Exception) {
            Log.e("VentaRepository", "Excepción: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }

    suspend fun obtenerVenta(ventaId: Int): Result<VentaResponse> =
        withContext(Dispatchers.IO) {
            try {
                Log.d("VentaRepository", "=== OBTENER VENTA POR ID ===")
                Log.d("VentaRepository", "Solicitando venta ID: $ventaId")
                val response = apiService.obtenerVenta(ventaId).execute()

                return@withContext if (response.isSuccessful) {
                    val venta = response.body()
                    if (venta != null) {
                        Log.d("VentaRepository", "Venta obtenida exitosamente")
                        Log.d("VentaRepository", "ID: ${venta.id}, Email: ${venta.usuarioEmail}, Estado: ${venta.estado}")
                        Log.d("VentaRepository", "Total: ${venta.total}, Detalles: ${venta.detalles.size}")
                        Result.success(venta)
                    } else {
                        Log.e("VentaRepository", "Respuesta vacía del servidor")
                        Result.failure(Exception("Venta no encontrada"))
                    }
                } else {
                    Log.e("VentaRepository", "Error HTTP ${response.code()}: ${response.message()}")
                    Result.failure(Exception("Error: ${response.message()}"))
                }
            } catch (e: Exception) {
                Log.e("VentaRepository", "Excepción: ${e.message}")
                e.printStackTrace()
                Result.failure(e)
            }
        }

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

