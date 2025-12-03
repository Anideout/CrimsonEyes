package com.example.crimsoneyes.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crimsoneyes.api.CarritoApiService
import com.example.crimsoneyes.api.ItemCarritoApiService
import com.example.crimsoneyes.repository.BoleteRepository

class CarritoFactory(
    private val apiService: CarritoApiService,
    private val itemCarritoService: ItemCarritoApiService,
    private val usuarioEmail: String = "",
    private val boleteRepository: BoleteRepository? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarritoViewModel::class.java)) {
            return CarritoViewModel(apiService, itemCarritoService, usuarioEmail, boleteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
