package com.example.crimsoneyes.api

import com.example.crimsoneyes.model.ApiResponse
import com.example.crimsoneyes.model.CreateVentaRequest
import com.example.crimsoneyes.model.LoginRequest
import com.example.crimsoneyes.model.Receta
import com.example.crimsoneyes.model.Usuario
import com.example.crimsoneyes.model.VentaResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.Path

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("usuario/login")
    fun login(@Body body: LoginRequest): Call<Usuario>

    @Headers("Content-Type: application/json")
    @POST("usuario/register")
    fun register(@Body body: Usuario): Call<Map<String, Any>>

    @GET("usuario/listar/{email}")
    fun listar(@Path("email") email: String): Call<Usuario>

    @GET("usuario/perfil/{email}")
    fun obtenerPerfil(@Path("email") email: String): Call<Usuario>

    @GET("usuario/perfil")
    fun obtenerPerfilByEmail(@Query("email") email: String): Call<Usuario>

    @PUT("usuario/editar/{email}")
    fun editarPerfil(@Path("email") email: String, @Body usuario: Usuario): Call<Map<String, Any>>

    @POST("recetas")
    fun crearReceta(@Body receta: Receta): Call<Map<String, Any>>


}