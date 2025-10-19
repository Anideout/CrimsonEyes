package com.example.crimsoneyes.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val username: String,
    val password: String,

    )
