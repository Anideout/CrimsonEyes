package com.example.crimsoneyes.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.crimsoneyes.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: Usuario): Long

    @Update
    suspend fun update(usuario: Usuario)

    @Delete
    suspend fun delete(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE username = :username")
    suspend fun findByUsername(username: String): Usuario?
}
