package com.example.crimsoneyes.model

import com.google.gson.annotations.SerializedName

data class ItemCarrito(
        @SerializedName("id")
        val id: Int = 0,

        @SerializedName("cantidad")
        val cantidad: Int = 1,

        // Campo necesario para enviar al servidor
        @SerializedName("carritoId")
        val carritoId: Int = 0,

        @SerializedName("producto")
        val producto: Producto
)