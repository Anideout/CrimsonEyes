package com.example.crimsoneyes.repository

import com.example.crimsoneyes.dao.RecetaDAO
import com.example.crimsoneyes.db.CrimsonDataBase
import com.example.crimsoneyes.model.Receta
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class RecetaRepository(private val db: CrimsonDataBase) {

    suspend fun findbyTitle(title: String): Receta? {
        return db.RecetaDao().findBytitle(title)
    }

    fun getAllRecetas(): Flow<List<Receta>> {
        return db.RecetaDao().getAllRecetas()
    }

    suspend fun insert(receta: Receta): Long {
        return db.RecetaDao().insert(receta)
    }

    suspend fun getAllRecetasList(): List<Receta> {
        return db.RecetaDao().getAllRecetas().first()
    }



}