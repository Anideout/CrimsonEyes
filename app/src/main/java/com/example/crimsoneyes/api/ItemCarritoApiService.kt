package com.example.crimsoneyes.api

import com.example.crimsoneyes.model.ItemCarrito
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

//
interface ItemCarritoApiService {

    @GET("items-carrito/carrito/{carritoId}")
    fun obtenerCarritoPorUsuario(@Path("carritoId") carritoId: Int): Call<List<ItemCarrito>>

    @GET("items-carrito/carrito/{carritoId}")
    fun obtenerItemsDelCarrito(@Path("carritoId") carritoId: Int): Call<List<ItemCarrito>>

    @POST("items-carrito/agregar/{carritoId}")
    fun agregarItemAlCarrito(@Path("carritoId") carritoId: Int, @Body item: ItemCarrito): Call<Map<String, Any>>

    @PUT("items-carrito/actualizar-cantidad/{itemId}/{nuevaCantidad}")
    fun actualizarCantidad(@Path("itemId") itemId: Int, @Path("nuevaCantidad") nuevaCantidad: Int): Call<Map<String, Any>>

    @DELETE("items-carrito/eliminar/{id}")
    fun eliminarItemDelCarrito(@Path("id") id: Int): Call<Map<String, Any>>

    @DELETE("items-carrito/vaciar-carrito/{carritoId}")
    fun vaciarCarrito(@Path("carritoId") carritoId: Int): Call<Map<String, Any>>
}
