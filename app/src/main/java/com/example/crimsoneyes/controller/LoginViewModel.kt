package com.example.crimsoneyes.controller

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crimsoneyes.api.ApiRepository
import com.example.crimsoneyes.model.LoginRequest
import com.example.crimsoneyes.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Response

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val usuario: Usuario) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _loginState.value = LoginState.Error("Por favor completa todos los campos")
            return
        }

        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading

                // validar credenciales en el backend
                val loginRequest = LoginRequest(email = email, password = password)

                ApiRepository.usuarioApi.login(loginRequest).enqueue(
                    object : Callback<Usuario> {
                        override fun onResponse(call: retrofit2.Call<Usuario>, response: Response<Usuario>) {
                            if (response.isSuccessful && response.body() != null) {
                                val user = response.body()!!
                                _loginState.value = LoginState.Success(user)
                                Log.d("LoginViewModel", "Login exitoso para: ${user.email}")
                            } else {
                                _loginState.value = LoginState.Error("Usuario o contraseña incorrectos")
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<Usuario>, t: Throwable) {
                            _loginState.value = LoginState.Error("Error al iniciar sesión: ${t.message}")
                            Log.e("LoginViewModel", "Error en login", t)
                        }
                    }
                )
            } catch (ex: Exception) {
                _loginState.value = LoginState.Error("Error al iniciar sesión: ${ex.message}")
                Log.e("LoginViewModel", "Exception en login", ex)
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}