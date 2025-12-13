package com.example.crimsoneyes.dao

import androidx.room.*
import com.example.crimsoneyes.model.Receta
import kotlinx.coroutines.flow.Flow

@Dao
interface RecetaDAO {

    @Query("SELECT * FROM recetas ORDER BY id DESC")
    fun getAllRecetas(): Flow<List<Receta>>

    @Query("SELECT * FROM recetas WHERE userId = :userId ORDER BY id DESC")
    fun getRecetasByUserId(userId: String): Flow<List<Receta>>

    @Query("SELECT * FROM recetas WHERE id = :id")
    suspend fun getRecetaById(id: String): Receta?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(receta: Receta)

    @Delete
    suspend fun delete(receta: Receta)

    @Query("DELETE FROM recetas")
    suspend fun deleteAll()

    @Query("DELETE FROM recetas WHERE userId = :userId")
    suspend fun deleteByUserId(userId: String)
}