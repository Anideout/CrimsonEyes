package com.example.crimsoneyes.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crimsoneyes.api.RecetaApiService
import com.example.crimsoneyes.model.Producto
import com.example.crimsoneyes.model.Receta
import com.example.crimsoneyes.network.RetrofitProvider
import com.example.crimsoneyes.repository.RecetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

data class RecetaUiState(
    // creación
    val title: String = "",
    val bodY: String = "",
    val isCreating: Boolean = false,
    val created: Receta? = null,
    val createError: String? = null,
    //listado
    val list: List<Receta> = emptyList(),
    val isListLoading: Boolean = false,
    val listError: String? = null,
    // Eliminación
    val isDeleting: Boolean = false,
    val deleteError: String? = null,

    val productos: List<Producto> = emptyList(),
    val isProductosLoading: Boolean = false,
    val productosError: String? = null,
    val selectedTab: Int = 0
)

class RecetaViewModel(
    private val repository: RecetaRepository,
    private val currentUserId: String
): ViewModel() {
    private val api: RecetaApiService by lazy { RetrofitProvider.create<RecetaApiService>() }
    private val _state = MutableStateFlow(RecetaUiState())
    val state: StateFlow<RecetaUiState> = _state.asStateFlow()

    init {
        cargarRecetas()
    }

    fun onTitleChange(value: String) {
        _state.update { it.copy(title = value, createError = null) }
    }

    fun onBodyChange(value: String) {
        _state.update{ it.copy(bodY = value, createError = null)}
    }

    fun cargarRecetas() {
        viewModelScope.launch {
            _state.update { it.copy(isListLoading = true, listError = null) }

            try {
                repository.getAllRecetas()
                    .onEach { recetas ->
                        _state.update { it.copy(list = recetas, isListLoading = false) }
                    }
                    .catch { e ->
                        _state.update { it.copy(isListLoading = false, listError = e.message) }
                    }
                    .collect()
            } catch (e: Exception) {
                _state.update { it.copy(isListLoading = false, listError = e.message) }
            }
        }
    }

    fun crearReceta() {
        val p = _state.value
        if(p.title.isBlank() || p.bodY.isBlank()) {
            _state.update { it.copy(createError = "Ingrese los datos!") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isCreating = true, createError = null, created = null) }

            try {
                val nuevaReceta = Receta(
                    id = "",
                    userId = currentUserId,
                    title = p.title,
                    body = p.bodY
                )

                try {
                    val response = api.crearReceta(nuevaReceta)

                    if (response.isSuccessful && response.body() != null) {
                        val serverResponse = response.body()!!

                        if (serverResponse.estado == "ok") {
                            val recetaDelServidor = serverResponse.receta

                            if (recetaDelServidor != null) {
                                repository.insert(recetaDelServidor)
                            }

                            _state.update {
                                it.copy(
                                    isCreating = false,
                                    created = recetaDelServidor,
                                    title = "",
                                    bodY = "",
                                    createError = null
                                )
                            }

                            delay(3000)
                            _state.update { it.copy(created = null) }

                        } else {
                            _state.update {
                                it.copy(
                                    isCreating = false,
                                    createError = serverResponse.mensaje
                                )
                            }
                        }
                    } else {
                        throw Exception("Error en la respuesta del servidor: ${response.code()}")
                    }

                } catch (apiError: Exception) {
                    println("Error al sincronizar con API: ${apiError.message}")

                    // Generar un ID temporal para la BD local (UUID o timestamp)
                    val idTemporal = System.currentTimeMillis().toString()
                    val recetaLocal = nuevaReceta.copy(id = idTemporal)

                    // Guardar en bd
                    repository.insert(recetaLocal)

                    _state.update {
                        it.copy(
                            isCreating = false,
                            created = recetaLocal,
                            title = "",
                            bodY = "",
                            createError = null
                        )
                    }

                    delay(3000)
                    _state.update { it.copy(created = null, createError = null) }
                }

            } catch(ex: Exception) {
                _state.update {
                    it.copy(
                        isCreating = false,
                        createError = ex.message ?: "Error al guardar la receta"
                    )
                }
            }
        }
    }

    fun eliminarReceta(receta: Receta) {
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true, deleteError = null) }
            try {
                repository.delete(receta)
                _state.update { it.copy(isDeleting = false) }
            } catch (ex: Exception) {
                _state.update {
                    it.copy(
                        isDeleting = false,
                        deleteError = ex.message ?: "Error al eliminar la receta"
                    )
                }
            }
        }
    }

    // Sincronizar recetas del servidor con la DB
    fun sincronizarConServidor() {
        viewModelScope.launch {
            try {
                val response = api.listarRecetasPorUsuario(currentUserId)
                if (response.isSuccessful && response.body() != null) {
                    val recetasServidor = response.body()!!
                    // Actualizar la DB local con las recetas
                    recetasServidor.forEach { receta ->
                        repository.insert(receta)
                    }
                }
            } catch (e: Exception) {
                println("Error al sincronizar: ${e.message}")
            }
        }
    }
}