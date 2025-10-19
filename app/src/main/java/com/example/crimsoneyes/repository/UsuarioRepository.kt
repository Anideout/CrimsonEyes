package com.example.crimsoneyes.repository

import com.example.crimsoneyes.db.CrimsonDataBase
import com.example.crimsoneyes.model.Usuario

class UsuarioRepository(private val db: CrimsonDataBase) {

    suspend fun findByUsername(username: String): Usuario? {
        return db.UsuarioDao().findByUsername(username)
    }

    suspend fun insert(u: Usuario): Long {
        return db.UsuarioDao().insert(u)
    }

    suspend fun delete(u: Usuario) {
        db.UsuarioDao().delete(u)
    }

    suspend fun update(u: Usuario) {
        db.UsuarioDao().update(u)
    }


}