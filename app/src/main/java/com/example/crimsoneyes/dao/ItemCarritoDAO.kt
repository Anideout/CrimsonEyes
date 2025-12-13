package com.example.crimsoneyes.dao

import androidx.room.*
import com.example.crimsoneyes.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemCarritoDAO {

    @Query("SELECT * FROM items_carrito ORDER BY id ASC")
    fun getAllItems(): Flow<List<ItemCarrito>>

    @Query("SELECT * FROM items_carrito WHERE id = :id")
    suspend fun getItemById(id: Int): ItemCarrito?

    @Query("SELECT * FROM items_carrito WHERE carritoId = :carritoId ORDER BY id ASC")
    fun getItemsByCarritoId(carritoId: Int): Flow<List<ItemCarrito>>

    @Query("SELECT * FROM items_carrito WHERE carritoId = :carritoId AND productoId = :productoId")
    suspend fun getItemByCarritoIdAndProductoId(carritoId: Int, productoId: Int): ItemCarrito?

    @Query("SELECT COUNT(*) FROM items_carrito WHERE carritoId = :carritoId")
    suspend fun getItemsCountByCarrito(carritoId: Int): Int

    @Query("SELECT SUM(cantidad) FROM items_carrito WHERE carritoId = :carritoId")
    suspend fun getTotalCantidadByCarrito(carritoId: Int): Int?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(itemCarrito: ItemCarrito): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ItemCarrito>)

    @Update
    suspend fun update(itemCarrito: ItemCarrito)

    @Delete
    suspend fun delete(itemCarrito: ItemCarrito)

    @Query("DELETE FROM items_carrito")
    suspend fun deleteAll()

    @Query("DELETE FROM items_carrito WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM items_carrito WHERE carritoId = :carritoId")
    suspend fun deleteByCarritoId(carritoId: Int)

    @Query("DELETE FROM items_carrito WHERE carritoId = :carritoId AND productoId = :productoId")
    suspend fun deleteByCarritoIdAndProductoId(carritoId: Int, productoId: Int)
}

