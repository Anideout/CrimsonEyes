package com.example.crimsoneyes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class CreateVentaRequest(
    @SerializedName("usuarioEmail")
    val usuarioEmail: String,

    @SerializedName("metodoPago")
    val metodoPago: String,  // "TARJETA", "TRANSFERENCIA"

    @SerializedName("total")
    val total: Double,

    @SerializedName("detalles")
    val detalles: List<DetalleVentaRequest>
)

data class DetalleVentaRequest(
    @SerializedName("productoId")
    val productoId: Int,

    @SerializedName("cantidad")
    val cantidad: Int,

    @SerializedName("precioUnitario")
    val precioUnitario: Double
)

data class ApiResponse<T>(
    @SerializedName("estado")
    val estado: String,

    @SerializedName("mensaje")
    val mensaje: String? = null,

    @SerializedName("venta")
    val venta: T? = null
)

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

