package com.example.crimsoneyes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "boletas")
data class Boleta(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val numeroVenta: String = "",
    val usuarioEmail: String = "", // Email del usuario que compró
    val fechaCompra: String = "", // Fecha y hora de la compra
    val metodoPago: String = "", // Débito, Transferencia, etc
    val subtotal: Int = 0, // Precio sin envío
    val envio: Int = 0, // Costo de envío
    val total: Int = 0, // Total
    val cantidadProductos: Int = 0, // Cantidad de items comprados
    val detallesProductos: String = "", // JSON con detalles de productos
    val estado: String = "completada" // Estado de la boleta
) : Serializable

