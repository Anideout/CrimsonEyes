package com.example.crimsoneyes.api

import com.example.crimsoneyes.model.Producto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductoApiService {

    @GET("productos/listar")
    fun obtenerTodosLosProductos(): Call<List<Producto>>

    @POST("productos/crear")
    fun crearProducto(@Body producto: Producto): Call<Map<String, Any>>

    @PUT("productos/actualizar")
    fun actualizarProducto(@Body producto: Producto): Call<Map<String, Any>>

    @DELETE("productos/eliminar/{id}")
    fun eliminarProducto(@Path("id") id: Int): Call<Map<String, Any>>
}
