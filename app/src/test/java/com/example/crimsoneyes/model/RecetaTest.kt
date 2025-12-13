package com.example.crimsoneyes.model

import com.example.crimsoneyes.dao.RecetaDAO
import com.example.crimsoneyes.repository.RecetaRepository
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RecetaTest {
    private lateinit var recetaRepository: RecetaRepository
    private lateinit var recetaDAO: RecetaDAO

    @Before
    fun setup() {
        recetaDAO = mockk()
        recetaRepository = RecetaRepository(recetaDAO)
    }

    //Crear receta
    @Test
    fun testRecetaCreationWithAllFields() {
        val receta = Receta(
            id = "1",
            userId = "user123",
            title = "Lentes de Lectura",
            body = "Lentes para poder leer tranquilo.."
        )

        assert(receta.id == "1")
        assert(receta.userId == "user123")
        assert(receta.title == "Lentes de Lectura")
        assert(receta.body == "Lentes para poder leer tranquilo..")
    }


    //Validar igualdad entre dos Recetas
    @Test
    fun testRecetaEquality() {
        val receta1 = Receta("1", "user123", "Lentes", "Para leer...")
        val receta2 = Receta("1", "user123", "Lentes", "Para leer...")

        assert(receta1 == receta2)
    }

    // Validar desigualdad entre dos Recetas
    @Test
    fun testRecetaInequality() {
        val receta1 = Receta("1", "user123", "Lentes", "Para leer...")
        val receta2 = Receta("2", "user456", "Redondos", "Stylo...")

        assert(receta1 != receta2)
    }

    //getAllRecetas
    @Test
    fun testGetAllRecetas() = runTest {
        val recetas = listOf(
            Receta("1", "user1", "Lentes", "Receta 1"),
            Receta("2", "user2", "Redondos", "Receta 2"),
            Receta("3", "user3", "Futuristas", "Receta 3")
        )

        every { recetaDAO.getAllRecetas() } returns flowOf(recetas)

        recetaRepository.getAllRecetas().collect { result ->
            assert(result.size == 3)
            assert(result[0].title == "Lentes")
            assert(result[1].title == "Redondos")
            assert(result[2].title == "Futuristas")
        }

        verify {
            recetaDAO.getAllRecetas()
        }
    }


    @Test
    fun testInsertReceta() = runTest {
        val receta = Receta("1", "user123", "Lentes", "Receta de lentes...")
        coEvery { recetaDAO.insert(receta) } just runs

        recetaRepository.insert(receta)

        coVerify { recetaDAO.insert(receta) }
    }

    @Test
    fun testDeleteReceta() = runTest {
        val receta = Receta("1", "user123", "Lentes", "Receta de lentes...")
        coEvery { recetaDAO.delete(receta) } just runs

        recetaRepository.delete(receta)

        coVerify { recetaDAO.delete(receta) }
    }

    @Test
    fun testGetRecetaById() = runTest {
        val recetaId = "1"
        val receta = Receta("1", "user123", "armani", "Receta de armani...")
        coEvery { recetaDAO.getRecetaById(recetaId) } returns receta

        val result = recetaRepository.getRecetaById(recetaId)

        assert(result != null)
        assert(result?.id == "1")
        assert(result?.title == "armani")

        coVerify { recetaDAO.getRecetaById(recetaId) }
    }

    @Test
    fun testGetRecetaByIdNotFound() = runTest {
        val recetaId = "999"
        coEvery { recetaDAO.getRecetaById(recetaId) } returns null

        val result = recetaRepository.getRecetaById(recetaId)

        assert(result == null)

        coVerify { recetaDAO.getRecetaById(recetaId) }
    }

    @Test
    fun testGetAllRecetasEmpty() = runTest {
        every { recetaDAO.getAllRecetas() } returns flowOf(emptyList())

        recetaRepository.getAllRecetas().collect { result ->
            assert(result.isEmpty())
        }

        verify { recetaDAO.getAllRecetas() }
    }

    @Test
    fun testGetRecetasByUserIdEmpty() = runTest {
        val userId = "userNoExiste"
        every { recetaDAO.getRecetasByUserId(userId) } returns flowOf(emptyList())

        recetaRepository.getRecetasByUserId(userId).collect { result ->
            assert(result.isEmpty())
        }

        verify { recetaDAO.getRecetasByUserId(userId) }
    }
}