package com.example.crimsoneyes.api

import com.example.crimsoneyes.model.Receta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RecetaApiService {

    @POST("recetas/almacenar")
    suspend fun crearReceta(@Body receta: Receta): Response<RecetaResponse>

    @GET("recetas/listar")
    suspend fun listarTodasRecetas(): Response<List<Receta>>

    @GET("recetas/listar/{userId}")
    suspend fun listarRecetasPorUsuario(@Path("userId") userId: String): Response<List<Receta>>
}

data class RecetaResponse(
    val estado: String,
    val mensaje: String,
    val receta: Receta?
)