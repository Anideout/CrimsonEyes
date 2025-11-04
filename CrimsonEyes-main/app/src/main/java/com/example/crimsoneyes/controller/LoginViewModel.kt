package com.example.crimsoneyes.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crimsoneyes.model.Usuario
import com.example.crimsoneyes.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estados posibles del login
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val usuario: Usuario) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {

    // Estado interno mutable
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    // Estado público para la vista
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    // Variables de control de seguridad
    private var intentosFallidos = 0
    private var tiempoDesbloqueo: Long? = null
    private val tiempoBloqueo = 30_000L // 30 segundos

    fun login(nombre: String, password: String) {
        val ahora = System.currentTimeMillis()

        // Verifica si el usuario está bloqueado
        if (tiempoDesbloqueo != null && ahora < tiempoDesbloqueo!!) {
            val segundosRestantes = (tiempoDesbloqueo!! - ahora) / 1000
            _loginState.value = LoginState.Error("Cuenta bloqueada. Intenta nuevamente en ${segundosRestantes}s.")
            return
        }

        if (nombre.isEmpty() || password.isEmpty()) {
            _loginState.value = LoginState.Error("Por favor completa todos los campos")
            return
        }

        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading
                val user = repository.login(nombre, password)

                if (user == null) {
                    intentosFallidos++
                    if (intentosFallidos >= 3) {
                        tiempoDesbloqueo = ahora + tiempoBloqueo
                        intentosFallidos = 0
                        _loginState.value = LoginState.Error("Demasiados intentos. Bloqueado por 30 segundos.")
                    } else {
                        val restantes = 3 - intentosFallidos
                        _loginState.value = LoginState.Error("Credenciales incorrectas. Intentos restantes: $restantes")
                    }
                } else {
                    intentosFallidos = 0
                    tiempoDesbloqueo = null
                    _loginState.value = LoginState.Success(user)
                }
            } catch (ex: Exception) {
                _loginState.value = LoginState.Error("Error al iniciar sesión: ${ex.message}")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}
