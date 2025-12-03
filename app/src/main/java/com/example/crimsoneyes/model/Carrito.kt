package com.example.crimsoneyes.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "carritos",
    foreignKeys = [ForeignKey(
        entity = Usuario::class,
        parentColumns = ["email"],
        childColumns = ["usuarioEmail"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Carrito(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("usuarioEmail")
    val usuarioEmail: String = "",

    @SerializedName("fecha")
    val fecha: String = "",

    @SerializedName("estado")
    val estado: String = "activo"
)