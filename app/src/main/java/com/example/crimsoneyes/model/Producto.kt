package com.example.crimsoneyes.model

import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName

data class Producto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("precio")
    val precio: Int,

    @SerializedName("descripcion")
    val descripcion: String,

    //localmente
    @DrawableRes
    val imagenResId: Int = com.example.crimsoneyes.R.drawable.lente,

    @SerializedName("stock")
    val stock: Int = 0,

    @SerializedName("categoria")
    val categoria: String = "Lentes",


)