package com.example.crimsoneyes.controller

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crimsoneyes.api.ApiService
import com.example.crimsoneyes.model.Usuario
import com.example.crimsoneyes.network.RetrofitProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class ProfileUiState(
    val usuario: Usuario? = null,
    val nombre: String = "",
    val email: String = "",
    val rut: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

class ProfileViewModel(private val usuarioEmail: String) : ViewModel() {

    private val api = RetrofitProvider.create<ApiService>()

    private val _state = MutableStateFlow(ProfileUiState(email = usuarioEmail))
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    init {
        Log.d(TAG, "ProfileViewModel inicializado con email: '$usuarioEmail'")

        // cargar datos del usuario xd
        cargarDatosUsuario()
    }

    fun cargarDatosUsuario() {
        if (usuarioEmail.isBlank()) {
            Log.e(TAG, "ERROR: El email está vacío!")
            _state.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "No se pudo obtener el email del usuario. Por favor, inicia sesión nuevamente."
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            Log.d(TAG, "Cargando datos del usuario: '$usuarioEmail'")

            try {
                // Usar endpoint
                api.obtenerPerfil(usuarioEmail)
                    .enqueue(
                        object : Callback<Usuario> {
                            override fun onResponse(
                                call: Call<Usuario>,
                                response: Response<Usuario>
                            ) {
                                Log.d(TAG, "Respuesta recibida - Código: ${response.code()}")
                                Log.d(TAG, "URL completa: ${call.request().url}")

                                if (response.isSuccessful && response.body() != null) {
                                    val usuario = response.body()!!
                                    Log.d(TAG, "Usuario cargado exitosamente: ${usuario.nombre}")
                                    Log.d(TAG, "Email: ${usuario.email}, Nombre: ${usuario.nombre}, RUT: ${usuario.rut}")

                                    _state.update {
                                        it.copy(
                                            usuario = usuario,
                                            nombre = usuario.nombre,
                                            email = usuario.email,
                                            rut = usuario.rut,
                                            isLoading = false
                                        )
                                    }
                                } else {
                                    val errorBody = response.errorBody()?.string()
                                    Log.e(TAG, "Error al cargar usuario: ${response.code()}")
                                    Log.e(TAG, "Error body: $errorBody")

                                    _state.update {
                                        it.copy(
                                            isLoading = false,
                                            errorMessage = "No se pudo cargar el perfil (código: ${response.code()}). Si el problema persiste, cierra sesión e intenta de nuevo."
                                        )
                                    }
                                }
                            }

                            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                                Log.e(TAG, "Fallo al cargar usuario", t)
                                Log.e(TAG, "URL intentada: ${call.request().url}")
                                Log.e(TAG, "Excepción: ${t.stackTraceToString()}")

                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        errorMessage = "Error de conexión: ${t.message}. ERROR :c."
                                    )
                                }
                            }
                        }
                    )
            } catch (e: Exception) {
                Log.e(TAG, "Excepción al intentar cargar perfil", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Error inesperado: ${e.message}"
                    )
                }
            }
        }
    }

    fun actualizarNombre(nombre: String) {
        _state.update { it.copy(nombre = nombre) }
    }

    fun actualizarEmail(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun actualizarRut(rut: String) {
        _state.update { it.copy(rut = rut) }
    }

    fun actualizarPasswordActual(password: String) {
        _state.update { it.copy(currentPassword = password) }
    }

    fun actualizarNuevaPassword(password: String) {
        _state.update { it.copy(newPassword = password) }
    }

    fun actualizarConfirmarPassword(password: String) {
        _state.update { it.copy(confirmPassword = password) }
    }

    fun guardarCambios() {
        viewModelScope.launch {
            val currentState = _state.value
            _state.update { it.copy(errorMessage = null, successMessage = null) }

            val errorMsg = validarDatos(currentState)
            if (errorMsg != null) {
                _state.update { it.copy(errorMessage = errorMsg) }
                return@launch
            }

            val usuarioOriginal = currentState.usuario
            if (usuarioOriginal == null) {
                _state.update { it.copy(errorMessage = "No se encontraron datos del usuario") }
                return@launch
            }

            val usuarioActualizado =
                usuarioOriginal.copy(
                    nombre = currentState.nombre.trim(),
                    // El email y rut no se modifican, se mantienen los originales
                    email = usuarioOriginal.email,
                    rut = usuarioOriginal.rut,
                    password =
                        if (currentState.newPassword.isNotEmpty()) {
                            currentState.newPassword
                        } else {
                            usuarioOriginal.password
                        }
                )
            guardarEnServidor(usuarioActualizado)
        }
    }

    private fun validarDatos(state: ProfileUiState): String? {
        if (state.nombre.isBlank()) {
            return "El nombre no puede estar vacío"
        }
        if (state.nombre.length < 3) {
            return "El nombre debe tener al menos 3 caracteres"
        }

        // Validación de contraseña
        if (state.newPassword.isNotEmpty() || state.confirmPassword.isNotEmpty() || state.currentPassword.isNotEmpty()) {
            if (state.currentPassword.isBlank()) {
                return "Ingresa tu contraseña actual para cambiarla"
            }
            if (state.newPassword.isBlank()) {
                return "Ingresa la nueva contraseña"
            }
            if (state.newPassword.length < 6) {
                return "La nueva contraseña debe tener al menos 6 caracteres"
            }
            if (state.newPassword != state.confirmPassword) {
                return "Las contraseñas nuevas no coinciden"
            }

        }

        return null
    }

    private fun guardarEnServidor(usuario: Usuario) {
        _state.update { it.copy(isSaving = true) }

        Log.d(TAG, "Guardando cambios del usuario: ${usuario.email}")

        api.editarPerfil(usuarioEmail, usuario)
            .enqueue(
                object : Callback<Map<String, Any>> {
                    override fun onResponse(
                        call: Call<Map<String, Any>>,
                        response: Response<Map<String, Any>>
                    ) {
                        if (response.isSuccessful) {
                            Log.d(TAG, "Perfil actualizado exitosamente")

                            _state.update {
                                it.copy(
                                    usuario = usuario,
                                    isSaving = false,
                                    successMessage = "¡Cambios guardados exitosamente!",
                                    currentPassword = "",
                                    newPassword = "",
                                    confirmPassword = ""
                                )
                            }

                            viewModelScope.launch {
                                kotlinx.coroutines.delay(3000)
                                _state.update { it.copy(successMessage = null) }
                            }
                        } else {
                            Log.e(TAG, "Error al guardar: ${response.code()}")
                            _state.update {
                                it.copy(
                                    isSaving = false,
                                    errorMessage = "Error al guardar cambios: ${response.code()}"
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                        Log.e(TAG, "Fallo al guardar perfil", t)
                        _state.update {
                            it.copy(
                                isSaving = false,
                                errorMessage = "Error de conexión: ${t.message}"
                            )
                        }
                    }
                }
            )
    }

    fun limpiarError() {
        _state.update { it.copy(errorMessage = null) }
    }

    fun limpiarMensajeExito() {
        _state.update { it.copy(successMessage = null) }
    }

    fun limpiarPasswordFields() {
        _state.update { it.copy(currentPassword = "", newPassword = "", confirmPassword = "") }
    }
}