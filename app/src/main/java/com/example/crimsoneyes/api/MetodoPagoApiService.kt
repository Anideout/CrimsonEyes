package com.example.crimsoneyes.api

import com.example.crimsoneyes.model.MetodoPago
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MetodoPagoApiService {


    @GET("metodos-pago/listar") fun obtenerTodosLosMetodos(): Call<List<MetodoPago>>

    @POST("metodos-pago/crear")
    fun crearMetodoPago(@Body metodo: MetodoPago): Call<Map<String, Any>>
}
