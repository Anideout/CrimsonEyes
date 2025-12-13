package com.example.crimsoneyes.dao

import androidx.room.*
import com.example.crimsoneyes.model.Boleta
import kotlinx.coroutines.flow.Flow

@Dao
interface BoleteDAO {

    @Query("SELECT * FROM boletas ORDER BY id DESC")
    fun getAllBoletas(): Flow<List<Boleta>>

    @Query("SELECT * FROM boletas WHERE id = :id")
    suspend fun getBoleteById(id: Long): Boleta?

    @Query("SELECT * FROM boletas WHERE usuarioEmail = :email ORDER BY id DESC")
    fun getBoletesByUsuario(email: String): Flow<List<Boleta>>

    @Query("SELECT * FROM boletas WHERE numeroVenta = :numeroVenta")
    suspend fun getBoleteByNumero(numeroVenta: String): Boleta?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(boleta: Boleta): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(boletas: List<Boleta>)

    @Update
    suspend fun update(boleta: Boleta)

    @Delete
    suspend fun delete(boleta: Boleta)

    @Query("DELETE FROM boletas")
    suspend fun deleteAll()

    @Query("DELETE FROM boletas WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM boletas WHERE usuarioEmail = :email")
    suspend fun deleteByUsuarioEmail(email: String)

    @Query("SELECT COUNT(*) FROM boletas WHERE usuarioEmail = :email")
    suspend fun getBeletasCountByUsuario(email: String): Int
}

