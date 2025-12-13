package com.example.crimsoneyes.api

import com.example.crimsoneyes.model.Carrito
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface CarritoApiService {

    @GET("carritos/listar")
    fun obtenerTodosLosCarritos(): Call<List<Carrito>>

    @GET("carritos/usuario/{email}")
    fun obtenerCarritoPorEmail(@Path("email") email: String): Call<Map<String, Any>>

    @GET("carritos/{id}")
    fun obtenerCarritoPorId(@Path("id") carritoId: Int): Call<Map<String, Any>>

    @POST("carritos/crear")
    fun crearCarrito(@Body carrito: Carrito): Call<Map<String, Any>>

    @POST("carritos/finalizar-compra/{carritoId}")
    fun finalizarCompra(@Path("carritoId") carritoId: Int): Call<Map<String, Any>>

}
