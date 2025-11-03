package com.example.crimsoneyes.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crimsoneyes.api.ApiService
import com.example.crimsoneyes.model.Producto
import com.example.crimsoneyes.model.Receta
import com.example.crimsoneyes.network.RetrofitProvider
import com.example.crimsoneyes.repository.RecetaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
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

class RecetaViewModel(private val repository: RecetaRepository): ViewModel() {
    private val api: ApiService by lazy { RetrofitProvider.create<ApiService>() }
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
                // Cargar recetas desde la base de datos local
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
                // Crear receta localmente
                val nuevaReceta = Receta(
                    userId = 1,
                    title = p.title,
                    body = p.bodY
                )

                // Guardar en base de datos local
                val id = repository.insert(nuevaReceta)
                val recetaGuardada = nuevaReceta.copy(id = id.toInt())

                // Actualizar estado: receta creada exitosamente
                _state.update {
                    it.copy(
                        isCreating = false,
                        created = recetaGuardada,
                        title = "",  // Limpiar formulario
                        bodY = "",
                        createError = null
                    )
                }

                // enviar a la API (en segundo plano)
                try {
                    api.crearReceta(nuevaReceta)
                } catch (apiError: Exception) {
                    // Si falla la API, no importa, ya está guardado localmente
                    println("Error al sincronizar con API: ${apiError.message}")
                }

                // Limpiar mensaje de éxito después de 3 segundos
                delay(3000)
                _state.update { it.copy(created = null) }

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
}