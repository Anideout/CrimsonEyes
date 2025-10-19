package com.example.crimsoneyes.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crimsoneyes.repository.UsuarioRepository

class LoginViewModelFactory (
    private val repository: UsuarioRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create (modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        throw IllegalArgumentException("Uknown ViewModel Class")
    }
}