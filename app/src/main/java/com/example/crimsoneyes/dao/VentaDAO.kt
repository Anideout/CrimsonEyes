package com.example.crimsoneyes.dao

import androidx.room.*
import com.example.crimsoneyes.model.Venta
import kotlinx.coroutines.flow.Flow

/**
 * 💾 DAO para acceder a la tabla de ventas en la base de datos
 * Se usa para guardar historial local de compras
 */
@Dao
interface VentaDAO {

    /**
     * 📋 Obtener todas las ventas
     */
    @Query("SELECT * FROM ventas ORDER BY id DESC")
    fun getAllVentas(): Flow<List<Venta>>

    /**
     * 🔍 Obtener venta por ID
     */
    @Query("SELECT * FROM ventas WHERE id = :id")
    suspend fun getVentaById(id: Int): Venta?

    /**
     * 👤 Obtener todas las ventas de un usuario
     */
    @Query("SELECT * FROM ventas WHERE usuarioEmail = :email ORDER BY id DESC")
    fun getVentasByUsuario(email: String): Flow<List<Venta>>

    /**
     * 📊 Contar ventas de un usuario
     */
    @Query("SELECT COUNT(*) FROM ventas WHERE usuarioEmail = :email")
    suspend fun getVentasCountByUsuario(email: String): Int

    /**
     * 💰 Obtener total de ventas por usuario
     */
    @Query("SELECT SUM(total) FROM ventas WHERE usuarioEmail = :email")
    suspend fun getTotalVentasByUsuario(email: String): Double?

    /**
     * 🔎 Buscar ventas por estado
     */
    @Query("SELECT * FROM ventas WHERE estado = :estado ORDER BY id DESC")
    fun getVentasByEstado(estado: String): Flow<List<Venta>>

    /**
     * 💳 Obtener ventas por método de pago
     */
    @Query("SELECT * FROM ventas WHERE metodoPago = :metodo ORDER BY id DESC")
    fun getVentasByMetodoPago(metodo: String): Flow<List<Venta>>

    /**
     * 📅 Obtener ventas en un rango de fechas
     */
    @Query("SELECT * FROM ventas WHERE fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY id DESC")
    suspend fun getVentasByFechas(fechaInicio: String, fechaFin: String): List<Venta>

    /**
     * ➕ Insertar una nueva venta
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(venta: Venta): Long

    /**
     * ➕ Insertar múltiples ventas
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ventas: List<Venta>)

    /**
     * ✏️ Actualizar una venta (cambiar estado, etc.)
     */
    @Update
    suspend fun update(venta: Venta)

    /**
     * 🗑️ Eliminar una venta
     */
    @Delete
    suspend fun delete(venta: Venta)

    /**
     * 🗑️ Eliminar todas las ventas
     */
    @Query("DELETE FROM ventas")
    suspend fun deleteAll()

    /**
     * 🗑️ Eliminar venta por ID
     */
    @Query("DELETE FROM ventas WHERE id = :id")
    suspend fun deleteById(id: Int)

    /**
     * 🗑️ Eliminar ventas de un usuario
     */
    @Query("DELETE FROM ventas WHERE usuarioEmail = :email")
    suspend fun deleteByUsuarioEmail(email: String)

    /**
     * 🗑️ Eliminar ventas por estado
     */
    @Query("DELETE FROM ventas WHERE estado = :estado")
    suspend fun deleteByEstado(estado: String)

    /**
     * 📊 Obtener venta más reciente
     */
    @Query("SELECT * FROM ventas ORDER BY id DESC LIMIT 1")
    suspend fun getLatestVenta(): Venta?

    /**
     * 📊 Obtener venta más reciente de un usuario
     */
    @Query("SELECT * FROM ventas WHERE usuarioEmail = :email ORDER BY id DESC LIMIT 1")
    suspend fun getLatestVentaByUsuario(email: String): Venta?
}

