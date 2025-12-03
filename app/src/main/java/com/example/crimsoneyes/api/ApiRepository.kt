package com.example.crimsoneyes.api

import com.example.crimsoneyes.network.RetrofitProvider

object ApiRepository {

    val productoApi: ProductoApiService by lazy {
        RetrofitProvider.create(ProductoApiService::class.java)
    }

    val carritoApi: CarritoApiService by lazy {
        RetrofitProvider.create(CarritoApiService::class.java)
    }

    val itemCarritoApi: ItemCarritoApiService by lazy {
        RetrofitProvider.create(ItemCarritoApiService::class.java)
    }

    val metodoPagoApi: MetodoPagoApiService by lazy {
        RetrofitProvider.create(MetodoPagoApiService::class.java)
    }

    val usuarioApi: ApiService by lazy {
        RetrofitProvider.create(ApiService::class.java)
    }

    val recetaApi: RecetaApiService by lazy {
        RetrofitProvider.create(RecetaApiService::class.java)
    }

    val ventaApi: VentaApiService by lazy {
        RetrofitProvider.create(VentaApiService::class.java)
    }
}