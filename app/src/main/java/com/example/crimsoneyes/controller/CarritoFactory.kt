package com.example.crimsoneyes.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crimsoneyes.api.CarritoApiService
import com.example.crimsoneyes.api.ItemCarritoApiService
import com.example.crimsoneyes.api.MetodoPagoApiService
import com.example.crimsoneyes.repository.BoleteRepository

class CarritoFactory(
    private val carritoApiService: CarritoApiService,
    private val itemCarritoApiService: ItemCarritoApiService,
    private val metodoPagoApiService: MetodoPagoApiService, // NUEVO
    private val usuarioEmail: String,
    private val boleteRepository: BoleteRepository? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarritoViewModel::class.java)) {
            return CarritoViewModel(
                carritoApiService,
                itemCarritoApiService,
                metodoPagoApiService,
                usuarioEmail,
                boleteRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}