package com.example.crimsoneyes.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crimsoneyes.model.Usuario
import com.example.crimsoneyes.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
sealed class LoginState {
    object idle : LoginState()

    object Loading: LoginState()

    data class Success(val usuario: Usuario) : LoginState()

    data class Error(val message: String): LoginState()
}
class LoginViewModel(private val repository: UsuarioRepository): ViewModel() {
    //Estado privado mutable - solo el ViewModel puede modificarlo
    private val _loginState = MutableStateFlow<LoginState>(LoginState.idle)
    //estado público inmutable - la UI solo puede observarlo
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(nombre: String, password: String) {
        if(nombre.isEmpty() || password.isEmpty()) {
            _loginState.value = LoginState.Error("Por favor completa todos los campos")
            return
        }

        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading

                // Llamada al backend para validar credenciales
                val user = repository.login(nombre, password)
                when {
                    user == null -> {
                        _loginState.value = LoginState.Error("Usuario o contraseña incorrectos")
                    }
                    else -> {
                        _loginState.value = LoginState.Success(user)
                    }
                }
            } catch (ex: Exception) {
                _loginState.value = LoginState.Error("Error al iniciar sesión: ${ex.message}")
            }
        }

    }
    fun resetState() {
        _loginState.value = LoginState.idle
    }
}

