package com.example.crimsoneyes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "recetas")
data class Receta(
        @PrimaryKey(autoGenerate = false)
        @SerializedName("id")
        val id: String = "",

        @SerializedName("userId")
        val userId: String = "",

        @SerializedName("title")
        val title: String = "",

        @SerializedName("body")
        val body: String = ""
)