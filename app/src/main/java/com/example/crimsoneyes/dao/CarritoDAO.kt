package com.example.crimsoneyes.dao

import androidx.room.*
import com.example.crimsoneyes.model.Carrito
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDAO {

    @Query("SELECT * FROM carritos ORDER BY fecha DESC")
    fun getAllCarritos(): Flow<List<Carrito>>

    @Query("SELECT * FROM carritos WHERE id = :id")
    suspend fun getCarritoById(id: Int): Carrito?

    @Query("SELECT * FROM carritos WHERE usuarioEmail = :usuarioEmail ORDER BY fecha DESC")
    fun getCarritosByUsuario(usuarioEmail: String): Flow<List<Carrito>>

    @Query("SELECT * FROM carritos WHERE usuarioEmail = :usuarioEmail AND estado = :estado")
    suspend fun getCarritoByUsuarioYEstado(usuarioEmail: String, estado: String): Carrito?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(carrito: Carrito): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(carritos: List<Carrito>)

    @Update
    suspend fun update(carrito: Carrito)

    @Delete
    suspend fun delete(carrito: Carrito)

    @Query("DELETE FROM carritos")
    suspend fun deleteAll()

    @Query("DELETE FROM carritos WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM carritos WHERE usuarioEmail = :usuarioEmail")
    suspend fun deleteByUsuarioEmail(usuarioEmail: String)

    @Query("SELECT COUNT(*) FROM carritos WHERE usuarioEmail = :usuarioEmail")
    suspend fun getCarritosCountByUsuario(usuarioEmail: String): Int
}

