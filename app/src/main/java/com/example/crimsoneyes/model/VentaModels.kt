package com.example.crimsoneyes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

// ==================== REQUESTS ====================

/**
 * Enviar al backend: POST /ventas/crear
 */
data class CreateVentaRequest(
    @SerializedName("usuarioEmail")
    val usuarioEmail: String,

    @SerializedName("metodoPago")
    val metodoPago: String,  // "TARJETA", "TRANSFERENCIA"

    @SerializedName("total")
    val total: Double,  // Total en pesos

    @SerializedName("detalles")
    val detalles: List<DetalleVentaRequest>
)

data class DetalleVentaRequest(
    @SerializedName("productoId")
    val productoId: Int,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precioUnitario")
    val precioUnitario: Double  // En pesos
)

// ==================== RESPONSES ====================

/**
 * Respuesta del backend
 */
data class ApiResponse<T>(
    @SerializedName("estado")
    val estado: String,  // "OK" o "Error"

    @SerializedName("mensaje")
    val mensaje: String? = null,

    @SerializedName("venta")
    val venta: T? = null
)

/**
 * La venta que recibimos del servidor
 */
data class VentaResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("usuarioEmail")
    val usuarioEmail: String,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("total")
    val total: Double,

    @SerializedName("estado")
    val estado: String,  // "PENDIENTE", "CONFIRMADA", etc.

    @SerializedName("metodoPago")
    val metodoPago: String,

    @SerializedName("detalles")
    val detalles: List<DetalleVentaResponse> = emptyList()
)

data class DetalleVentaResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("productoId")
    val productoId: Int,

    @SerializedName("productoNombre")
    val productoNombre: String,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precioUnitario")
    val precioUnitario: Double,

    @SerializedName("subtotal")
    val subtotal: Double
)

// ==================== DATABASE ENTITY ====================

/**
 * Entidad Venta para la base de datos local
 * Se guarda localmente para historial de compras
 */
@Entity(tableName = "ventas")
data class Venta(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("usuarioEmail")
    val usuarioEmail: String,

    @SerializedName("fecha")
    val fecha: String,

    @SerializedName("total")
    val total: Double,

    @SerializedName("estado")
    val estado: String,

    @SerializedName("metodoPago")
    val metodoPago: String,

    @SerializedName("detallesJson")
    val detallesJson: String = ""
)

