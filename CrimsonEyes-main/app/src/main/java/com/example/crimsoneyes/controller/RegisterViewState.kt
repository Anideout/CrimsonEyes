package com.example.crimsoneyes.controller

import com.example.crimsoneyes.model.Usuario

sealed class RegisterState {
    object Idle: RegisterState()
    object Loading: RegisterState()
    data class Success(val usuario: Usuario): RegisterState()
    data class Error(val message: String): RegisterState()
}