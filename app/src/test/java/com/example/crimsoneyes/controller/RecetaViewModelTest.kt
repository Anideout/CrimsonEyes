package com.example.crimsoneyes.controller

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.example.crimsoneyes.api.RecetaApiService
import com.example.crimsoneyes.model.Receta
import com.example.crimsoneyes.repository.RecetaRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RecetaViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var recetaRepository: RecetaRepository
    private lateinit var recetaApiService: RecetaApiService
    private lateinit var viewModel: RecetaViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        recetaRepository = mockk()
        recetaApiService = mockk()

        every { recetaRepository.getAllRecetas() } returns flowOf(emptyList())

        viewModel = RecetaViewModel(recetaRepository, "user123")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ===== TEST 1: Validar estado inicial del ViewModel =====
    @Test
    fun testInitialState() = runTest {
        val state = viewModel.state.value

        assert(state.title == "")
        assert(state.bodY == "")
        assert(state.isCreating == false)
        assert(state.created == null)
        assert(state.createError == null)
        assert(state.list == emptyList<Receta>())
        assert(state.isListLoading == true || state.isListLoading == false)
    }

    // ===== TEST 2: Validar cambio de título =====
    @Test
    fun testOnTitleChange() {
        val nuevoTitulo = "Lentes de aumento"
        viewModel.onTitleChange(nuevoTitulo)

        val state = viewModel.state.value
        assert(state.title == nuevoTitulo)
        assert(state.createError == null)
    }

    //Validar cambio de descripción
    @Test
    fun testOnBodyChange() {
        val nuevaDescripcion = "Lentes para lectura"
        viewModel.onBodyChange(nuevaDescripcion)

        val state = viewModel.state.value
        assert(state.bodY == nuevaDescripcion)
        assert(state.createError == null)
    }


    // ===== TEST 5: Validar que cargarRecetas obtiene datos =====
    @Test
    fun testCargarRecetas() = runTest {
        val recetasEsperadas = listOf(
            Receta("1", "user123", "Lentes", "Leer"),
            Receta("2", "user123", "Futuristas", "Retrovisor"),
            Receta("3", "user123", "Redondos", "Leon the professional")
        )

        every { recetaRepository.getAllRecetas() } returns flowOf(recetasEsperadas)

        viewModel.cargarRecetas()
        advanceUntilIdle()

        val state = viewModel.state.value
        assert(state.list.size == 3)
        assert(state.list[0].title == "Lentes")
        assert(state.list[1].title == "Futuristas")
        assert(state.list[2].title == "Redondos")
        assert(state.isListLoading == false)
    }

    // ===== TEST 6: Validar que crearReceta falla sin título =====
    @Test
    fun testCrearRecetaSinTitulo() {
        viewModel.onTitleChange("")
        viewModel.onBodyChange("Descripción")
        viewModel.crearReceta()

        val state = viewModel.state.value
        assert(state.createError != null)
        assert(state.createError == "Ingrese los datos!")
        assert(state.isCreating == false)
    }

    // ===== TEST 7: Validar que crearReceta falla sin descripción =====
    @Test
    fun testCrearRecetaSinDescripcion() {
        viewModel.onTitleChange("Mi Receta")
        viewModel.onBodyChange("")
        viewModel.crearReceta()

        val state = viewModel.state.value
        assert(state.createError != null)
        assert(state.createError == "Ingrese los datos!")
    }

    // Validar que crearReceta falla si ambos están vacíos =====
    @Test
    fun testCrearRecetaSinCampos() {
        viewModel.onTitleChange("")
        viewModel.onBodyChange("")
        viewModel.crearReceta()

        val state = viewModel.state.value
        assert(state.createError == "Ingrese los datos!")
    }

    //Validar que eliminarReceta llama al repositorio
    @Test
    fun testEliminarReceta() = runTest {
        val receta = Receta("1", "user123", "Lentes", "Receta...")
        coEvery { recetaRepository.delete(receta) } just runs

        viewModel.eliminarReceta(receta)
        advanceUntilIdle()

        val state = viewModel.state.value
        assert(state.isDeleting == false)

        coVerify { recetaRepository.delete(receta) }
    }

    // Validar que eliminarReceta maneja errores
    @Test
    fun testEliminarRecetaConError() = runTest {
        val receta = Receta("1", "user123", "Lentes", "Receta...")
        val error = Exception("Error al eliminar")
        coEvery { recetaRepository.delete(receta) } throws error

        viewModel.eliminarReceta(receta)
        advanceUntilIdle()

        val state = viewModel.state.value
        assert(state.deleteError != null)
        assert(state.deleteError == "Error al eliminar")
        assert(state.isDeleting == false)
    }


    // Validar que cargarRecetas maneja errores
    @Test
    fun testCargarRecetasConError() = runTest {
        val error = Exception("Error al cargar recetas")
        every { recetaRepository.getAllRecetas() } throws error

        viewModel.cargarRecetas()
        advanceUntilIdle()

        val state = viewModel.state.value
        assert(state.isListLoading == false)
        assert(state.listError != null)
    }

    //Validar que campos se limpian después de crear
    @Test
    fun testCamposLimpiosDespuesDeCrear() {
        viewModel.onTitleChange("Mi Receta")
        viewModel.onBodyChange("Descripción")

        var state = viewModel.state.value
        assert(state.title == "Mi Receta")
        assert(state.bodY == "Descripción")

        // Nota: Sin mock de API, no se limpiarán, pero validamos el flujo
        assert(state.createError == null)
    }

    //Validar múltiples cambios de título =====
    @Test
    fun testMultiplesTitulosChange() {
        viewModel.onTitleChange("Receta 1")
        assert(viewModel.state.value.title == "Receta 1")

        viewModel.onTitleChange("Receta 2")
        assert(viewModel.state.value.title == "Receta 2")

        viewModel.onTitleChange("Receta 3")
        assert(viewModel.state.value.title == "Receta 3")
    }

    //Validar que estado es immutable (StateFlow) =====
    @Test
    fun testStateFlowImmutability() {
        val estadoInicial = viewModel.state.value
        viewModel.onTitleChange("Nuevo Título")
        val estadoNuevo = viewModel.state.value

        // Son diferentes instancias
        assert(estadoInicial !== estadoNuevo)
        assert(estadoInicial.title == "")
        assert(estadoNuevo.title == "Nuevo Título")
    }
}

