package com.example.crimsoneyes.model

// Ajustado para coincidir con el DTO del backend (email, password)
data class LoginRequest(val email: String = "", val password: String = "")
