package com.example.crimsoneyes.controller

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crimsoneyes.api.ApiRepository
import com.example.crimsoneyes.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Response

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val usuario: Usuario) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

class RegisterViewModel : ViewModel() {
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState.asStateFlow()

    fun register(nombre: String, email: String, password: String, rut: String) {
        viewModelScope.launch {
            try {
                _registerState.value = RegisterState.Loading

                // Validaciones básicas
                if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    _registerState.value = RegisterState.Error("Por favor completa todos los campos requeridos")
                    return@launch
                }

                val newUser = Usuario(
                    nombre = nombre,
                    email = email,
                    password = password,
                    rut = rut
                )

                // Llamada al backend para registrar
                ApiRepository.usuarioApi.register(newUser).enqueue(
                    object : Callback<Map<String, Any>> {
                        override fun onResponse(
                            call: retrofit2.Call<Map<String, Any>>,
                            response: Response<Map<String, Any>>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("RegisterViewModel", "Usuario registrado exitosamente: $nombre")
                                _registerState.value = RegisterState.Success(newUser)
                            } else {
                                val errorMsg = response.errorBody()?.string() ?: "Error al registrar"
                                _registerState.value = RegisterState.Error(errorMsg)
                                Log.e("RegisterViewModel", "Error en respuesta: ${response.code()} - $errorMsg")
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<Map<String, Any>>, t: Throwable) {
                            _registerState.value = RegisterState.Error("Error al conectar con el servidor: ${t.message}")
                            Log.e("RegisterViewModel", "Error en registro", t)
                        }
                    }
                )
            } catch (ex: Exception) {
                Log.e("RegisterViewModel", "Exception en registro", ex)
                _registerState.value = RegisterState.Error("Error al registrar: ${ex.message}")
            }
        }
    }

    fun resetState() {
        _registerState.value = RegisterState.Idle
    }
}
