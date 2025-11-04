package com.example.crimsoneyes.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crimsoneyes.repository.RecetaRepository

class RecetaViewModelFactory(
    private val repository: RecetaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecetaViewModel::class.java)) {  // ← Línea 16
            return RecetaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}