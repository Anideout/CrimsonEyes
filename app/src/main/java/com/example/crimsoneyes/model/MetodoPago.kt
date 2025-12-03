package com.example.crimsoneyes.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metodos_pago")
data class MetodoPago(
    @PrimaryKey
    val id: Int = 0,
    val nombre: String
)
