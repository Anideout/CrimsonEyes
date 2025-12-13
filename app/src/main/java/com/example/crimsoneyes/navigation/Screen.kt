package com.example.crimsoneyes.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Producto: Screen("producto")
    data class Profile(val email: String = "") : Screen("profile/{email}") {
        fun createRoute(email: String) = "profile/$email"
    }
    data object Carrito: Screen("Carrito")
    data object QRScanner: Screen("qr_scanner")
    data class Checkout(val email: String = "") : Screen("checkout/{email}") {
        fun createRoute(email: String) = "checkout/$email"
    }
    data class VentaDetalles(val ventaId: Int = 0, val email: String = "") : Screen("venta_detalles/{ventaId}/{email}") {
        fun createRoute(ventaId: Int, email: String) = "venta_detalles/$ventaId/$email"
    }
}