package com.example.crimsoneyes.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.crimsoneyes.model.Receta
import kotlinx.coroutines.flow.Flow

@Dao
interface RecetaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(receta: Receta): Long

    @Update
    suspend fun update(receta: Receta)

    @Delete
    suspend fun delete(receta: Receta)

    @Query("SELECT * FROM recetas ORDER BY id DESC")
    fun getAllRecetas(): Flow<List<Receta>>

    @Query("SELECT * FROM recetas WHERE title = :title ")
    suspend fun findBytitle(title: String): Receta?


}