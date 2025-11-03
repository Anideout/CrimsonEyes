package com.example.crimsoneyes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("rut") val rut: String,
    @SerializedName("nombre") val nombre: String


)
