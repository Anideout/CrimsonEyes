package com.example.crimsoneyes.controller

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProductoViewModelFactory(
    private val application: Application,
    private val carritoViewModel: CarritoViewModel
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            return ProductoViewModel(application, carritoViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}