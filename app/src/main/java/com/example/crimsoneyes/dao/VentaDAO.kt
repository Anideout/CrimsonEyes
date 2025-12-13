package com.example.crimsoneyes.dao

import androidx.room.*
import com.example.crimsoneyes.model.Venta
import kotlinx.coroutines.flow.Flow

@Dao
interface VentaDAO {


    @Query("SELECT * FROM ventas ORDER BY id DESC")
    fun getAllVentas(): Flow<List<Venta>>

    @Query("SELECT * FROM ventas WHERE id = :id")
    suspend fun getVentaById(id: Int): Venta?
    @Query("SELECT * FROM ventas WHERE usuarioEmail = :email ORDER BY id DESC")
    fun getVentasByUsuario(email: String): Flow<List<Venta>>

    @Query("SELECT COUNT(*) FROM ventas WHERE usuarioEmail = :email")
    suspend fun getVentasCountByUsuario(email: String): Int

    @Query("SELECT SUM(total) FROM ventas WHERE usuarioEmail = :email")
    suspend fun getTotalVentasByUsuario(email: String): Double?

    @Query("SELECT * FROM ventas WHERE estado = :estado ORDER BY id DESC")
    fun getVentasByEstado(estado: String): Flow<List<Venta>>

    @Query("SELECT * FROM ventas WHERE metodoPago = :metodo ORDER BY id DESC")
    fun getVentasByMetodoPago(metodo: String): Flow<List<Venta>>

    @Query("SELECT * FROM ventas WHERE fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY id DESC")
    suspend fun getVentasByFechas(fechaInicio: String, fechaFin: String): List<Venta>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(venta: Venta): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ventas: List<Venta>)

    @Update
    suspend fun update(venta: Venta)

    @Delete
    suspend fun delete(venta: Venta)

    @Query("DELETE FROM ventas")
    suspend fun deleteAll()

    @Query("DELETE FROM ventas WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM ventas WHERE usuarioEmail = :email")
    suspend fun deleteByUsuarioEmail(email: String)

    @Query("DELETE FROM ventas WHERE estado = :estado")
    suspend fun deleteByEstado(estado: String)

    @Query("SELECT * FROM ventas ORDER BY id DESC LIMIT 1")
    suspend fun getLatestVenta(): Venta?

    @Query("SELECT * FROM ventas WHERE usuarioEmail = :email ORDER BY id DESC LIMIT 1")
    suspend fun getLatestVentaByUsuario(email: String): Venta?
}

