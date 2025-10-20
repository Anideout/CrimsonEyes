package com.example.crimsoneyes.api

import com.example.crimsoneyes.model.LoginRequest
import com.example.crimsoneyes.model.Receta
import com.example.crimsoneyes.model.Usuario
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("posts")
    suspend fun crearReceta(@Body body: Receta): Receta

    @GET("posts")
    suspend fun obtenerRecetas(): List<Receta>
}