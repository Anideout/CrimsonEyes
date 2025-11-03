package com.example.crimsoneyes.repository

import com.example.crimsoneyes.api.ApiService
import com.example.crimsoneyes.db.CrimsonDataBase
import com.example.crimsoneyes.model.LoginRequest
import com.example.crimsoneyes.model.Usuario
import com.example.crimsoneyes.network.RetrofitProvider

class UsuarioRepository(private val db: CrimsonDataBase) {

    private val api: ApiService by lazy { RetrofitProvider.create() }

    // Mantener acceso local a BD para otras funcionalidades
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

    // --- Operaciones remotas con el backend ---
    suspend fun login(email: String, password: String): Usuario? {
        return try {
            api.login(LoginRequest(email = email, password = password))
        } catch (ex: Exception) {
            null
        }
    }

    suspend fun register(u: Usuario): Boolean {
        return try {
            val resp = api.register(u)
            resp.isSuccessful
        } catch (ex: Exception) {
            false
        }
    }
}
