package com.example.crimsoneyes.repository

import com.example.crimsoneyes.dao.RecetaDAO
import com.example.crimsoneyes.model.Receta
import kotlinx.coroutines.flow.Flow

class RecetaRepository(private val recetaDao: RecetaDAO) {

    fun getAllRecetas(): Flow<List<Receta>> {
        return recetaDao.getAllRecetas()
    }

    fun getRecetasByUserId(userId: String): Flow<List<Receta>> {
        return recetaDao.getRecetasByUserId(userId)
    }

    suspend fun insert(receta: Receta) {
        recetaDao.insert(receta)
    }

    suspend fun delete(receta: Receta) {
        recetaDao.delete(receta)
    }

    suspend fun getRecetaById(id: String): Receta? {
        return recetaDao.getRecetaById(id)
    }
}