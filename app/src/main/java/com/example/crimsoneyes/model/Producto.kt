package com.example.crimsoneyes.model

import androidx.annotation.DrawableRes
import androidx.room.Entity

@Entity(tableName = "productos")
data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Int,
    val descripcion: String,
    @DrawableRes val imagenResId: Int,
    val stock: Int = 0,
    val categoria: String = "Lentes"
)
