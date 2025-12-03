package com.example.crimsoneyes.dao

import androidx.room.*
import com.example.crimsoneyes.model.MetodoPago
import kotlinx.coroutines.flow.Flow

@Dao
interface MetodoPagoDAO {

    @Query("SELECT * FROM metodos_pago ORDER BY id ASC")
    fun getAllMetodosPago(): Flow<List<MetodoPago>>

    @Query("SELECT * FROM metodos_pago WHERE id = :id")
    suspend fun getMetodoPagoById(id: Int): MetodoPago?

    @Query("SELECT * FROM metodos_pago WHERE nombre = :nombre")
    suspend fun getMetodoPagoByNombre(nombre: String): MetodoPago?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(metodoPago: MetodoPago): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(metodosPago: List<MetodoPago>)

    @Update
    suspend fun update(metodoPago: MetodoPago)

    @Delete
    suspend fun delete(metodoPago: MetodoPago)

    @Query("DELETE FROM metodos_pago")
    suspend fun deleteAll()

    @Query("DELETE FROM metodos_pago WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM metodos_pago")
    suspend fun getMetodosPagoCount(): Int
}

