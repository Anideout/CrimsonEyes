package com.example.crimsoneyes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "metodos_pago")
data class MetodoPago(
    @PrimaryKey
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("nombre")
    val nombre: String
)
