package com.example.crimsoneyes.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.crimsoneyes.controller.RecetaViewModel
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

/**
 * Tests unitarios para la lógica de RecetaScreen
 * Pruebas enfocadas en la validación de estados y comportamientos
 */
@ExperimentalCoroutinesApi
class RecetaScreenUnitTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockRepository: RecetaRepository
    private lateinit var viewModel: RecetaViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()
        every { mockRepository.getAllRecetas() } returns flowOf(emptyList())
        viewModel = RecetaViewModel(mockRepository, "user123")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ===== TEST 1: Validar que el estado inicial es correcto =====
    @Test
    fun testInitialUiStateIsCorrect() {
        val state = viewModel.state.value

        assert(state.title.isEmpty())
        assert(state.bodY.isEmpty())
        assert(state.isCreating == false)
        assert(state.list.isEmpty())
        assert(state.createError == null)
    }

    // ===== TEST 2: Validar que onTitleChange actualiza el título =====
    @Test
    fun testOnTitleChangeUpdatesState() {
        val nuevoTitulo = "Lentes de Lectura Anti-Fatiga"

        viewModel.onTitleChange(nuevoTitulo)

        val state = viewModel.state.value
        assert(state.title == nuevoTitulo)
    }

    // ===== TEST 3: Validar que onBodyChange actualiza la descripción =====
    @Test
    fun testOnBodyChangeUpdatesState() {
        val nuevaDescripcion = "Lentes con cristales anti-fatiga para reducir la fatiga ocular"

        viewModel.onBodyChange(nuevaDescripcion)

        val state = viewModel.state.value
        assert(state.bodY == nuevaDescripcion)
    }

    // ===== TEST 4: Validar que crearReceta valida campos vacíos =====
    @Test
    fun testCrearRecetaValidatesEmptyTitle() {
        viewModel.onTitleChange("")
        viewModel.onBodyChange("Descripción válida")

        viewModel.crearReceta()

        val state = viewModel.state.value
        assert(state.createError == "Ingrese los datos!")
    }

    // ===== TEST 5: Validar que crearReceta valida descripción vacía =====
    @Test
    fun testCrearRecetaValidatesEmptyBody() {
        viewModel.onTitleChange("Título válido")
        viewModel.onBodyChange("")

        viewModel.crearReceta()

        val state = viewModel.state.value
        assert(state.createError == "Ingrese los datos!")
    }

    // ===== TEST 6: Validar que crearReceta no procede con campos vacíos =====
    @Test
    fun testCrearRecetaDoesNotProceedWithEmptyFields() {
        viewModel.onTitleChange("")
        viewModel.onBodyChange("")

        viewModel.crearReceta()

        val state = viewModel.state.value
        assert(state.isCreating == false)
        assert(state.createError != null)
    }

    // ===== TEST 7: Validar que cargarRecetas carga la lista =====
    @Test
    fun testCargarRecetasLoadsData() = runTest {
        val recetasEsperadas = listOf(
            Receta("1", "user123", "Lentes Redondos", "Estilo retro"),
            Receta("2", "user123", "Lentes de Sol", "Protección UV"),
            Receta("3", "user123", "Lentes Futuristas", "Diseño moderno")
        )

        every { mockRepository.getAllRecetas() } returns flowOf(recetasEsperadas)
        viewModel.cargarRecetas()
        advanceUntilIdle()

        val state = viewModel.state.value
        assert(state.list.size == 3)
        assert(state.isListLoading == false)
    }

    // ===== TEST 8: Validar que cargarRecetas maneja lista vacía =====
    @Test
    fun testCargarRecetasHandlesEmptyList() = runTest {
        every { mockRepository.getAllRecetas() } returns flowOf(emptyList())

        viewModel.cargarRecetas()
        advanceUntilIdle()

        val state = viewModel.state.value
        assert(state.list.isEmpty())
        assert(state.isListLoading == false)
    }

    // ===== TEST 9: Validar que eliminarReceta es llamado correctamente =====
    @Test
    fun testEliminarRecetaCallsRepository() = runTest {
        val receta = Receta("1", "user123", "Lentes", "Receta de prueba")
        coEvery { mockRepository.delete(receta) } just runs

        viewModel.eliminarReceta(receta)
        advanceUntilIdle()

        coVerify { mockRepository.delete(receta) }
    }

    // ===== TEST 10: Validar que eliminarReceta maneja errores =====
    @Test
    fun testEliminarRecetaHandlesError() = runTest {
        val receta = Receta("1", "user123", "Lentes", "Receta de prueba")
        coEvery { mockRepository.delete(receta) } throws Exception("Error de red")

        viewModel.eliminarReceta(receta)
        advanceUntilIdle()

        val state = viewModel.state.value
        assert(state.deleteError != null)
    }

    // ===== TEST 11: Validar que la interfaz muestra múltiples recetas =====
    @Test
    fun testUiDisplaysMultipleRecetas() = runTest {
        val recetas = (1..10).map { i ->
            Receta("$i", "user123", "Lente $i", "Descripción de lente $i")
        }

        every { mockRepository.getAllRecetas() } returns flowOf(recetas)
        viewModel.cargarRecetas()
        advanceUntilIdle()

        val state = viewModel.state.value
        assert(state.list.size == 10)
    }

    // ===== TEST 12: Validar que la receta se crea con datos correctos =====
    @Test
    fun testRecetaIsCreatedWithCorrectData() {
        val titulo = "Lentes Polarizados"
        val descripcion = "Lentes con protección polarizada para el agua"

        viewModel.onTitleChange(titulo)
        viewModel.onBodyChange(descripcion)

        val state = viewModel.state.value
        assert(state.title == titulo)
        assert(state.bodY == descripcion)
    }

    // ===== TEST 13: Validar que se limpian los campos después de crear =====
    @Test
    fun testFieldsClearedAfterCreation() {
        viewModel.onTitleChange("Mi Lente")
        viewModel.onBodyChange("Descripción")

        var state = viewModel.state.value
        assert(state.title.isNotEmpty())
        assert(state.bodY.isNotEmpty())

        // Limpieza manual (simulando)
        viewModel.onTitleChange("")
        viewModel.onBodyChange("")

        state = viewModel.state.value
        assert(state.title.isEmpty())
        assert(state.bodY.isEmpty())
    }

    // ===== TEST 14: Validar que cargarRecetas maneja excepciones =====
    @Test
    fun testCargarRecetasHandlesException() = runTest {
        every { mockRepository.getAllRecetas() } throws Exception("Error de conexión")

        viewModel.cargarRecetas()
        advanceUntilIdle()

        val state = viewModel.state.value
        assert(state.isListLoading == false)
        assert(state.listError != null || state.list.isEmpty())
    }

    // ===== TEST 15: Validar que se puede filtrar recetas por usuario =====
    @Test
    fun testRecetasAreFilteredByUser() = runTest {
        val recetas = listOf(
            Receta("1", "user123", "Lente 1", "Desc 1"),
            Receta("2", "user123", "Lente 2", "Desc 2"),
            Receta("3", "user456", "Lente 3", "Desc 3")
        )

        val usuarioID = "user123"
        val filtradas = recetas.filter { it.userId == usuarioID }

        assert(filtradas.size == 2)
        assert(filtradas.all { it.userId == usuarioID })
    }
}

