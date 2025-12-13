package com.example.crimsoneyes.repository

import com.example.crimsoneyes.api.ApiService
import com.example.crimsoneyes.db.CrimsonDataBase
import com.example.crimsoneyes.model.LoginRequest
import com.example.crimsoneyes.model.Usuario
import com.example.crimsoneyes.network.RetrofitProvider
import retrofit2.Call

class UsuarioRepository(private val db: CrimsonDataBase) {

    private val api: ApiService by lazy { RetrofitProvider.create() }

    suspend fun findByUsername(nombre: String): Usuario? {
        return db.UsuarioDao().findByUsername(nombre)
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

    // Backend methods
    fun login(email: String, password: String): Call<Usuario> {
        return api.login(LoginRequest(email = email, password = password))
    }

    fun register(u: Usuario): Call<Map<String, Any>> {
        return api.register(u)
    }


}