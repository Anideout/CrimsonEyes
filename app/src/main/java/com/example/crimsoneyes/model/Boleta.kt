package com.example.crimsoneyes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "boletas")
data class Boleta(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val numeroVenta: String = "",
    val usuarioEmail: String = "",
    val fechaCompra: String = "",
    val metodoPago: String = "",
    val subtotal: Int = 0,
    val envio: Int = 0,
    val total: Int = 0,
    val cantidadProductos: Int = 0,
    val detallesProductos: String = "",
    val estado: String = "completada"
) : Serializable

