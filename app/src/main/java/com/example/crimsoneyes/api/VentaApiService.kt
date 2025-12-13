package com.example.crimsoneyes.api

import com.example.crimsoneyes.model.CreateVentaRequest
import com.example.crimsoneyes.model.VentaResponse
import retrofit2.Call
import retrofit2.http.*

interface VentaApiService {

    @POST("ventas/crear")
    fun crearVenta(@Body request: CreateVentaRequest): Call<Map<String, Any>>

    @GET("ventas/{id}")
    fun obtenerVenta(@Path("id") id: Int): Call<VentaResponse>

    @GET("ventas/usuario/{email}")
    fun obtenerVentasUsuario(@Path("email") email: String): Call<List<VentaResponse>>

    @PUT("ventas/{id}/estado")
    fun actualizarEstadoVenta(
        @Path("id") id: Int,
        @Body estado: Map<String, String>
    ): Call<Map<String, Any>>
}

