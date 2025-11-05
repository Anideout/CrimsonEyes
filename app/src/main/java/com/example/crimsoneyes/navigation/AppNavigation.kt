package com.example.crimsoneyes.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.crimsoneyes.controller.LoginViewModel
import com.example.crimsoneyes.controller.LoginViewModelFactory
import com.example.crimsoneyes.controller.ProductoViewModel
import com.example.crimsoneyes.controller.RecetaViewModel
import com.example.crimsoneyes.controller.RecetaViewModelFactory
import com.example.crimsoneyes.controller.RegisterViewModel
import com.example.crimsoneyes.controller.RegisterViewModelFactory
import com.example.crimsoneyes.db.CrimsonDataBase
import com.example.crimsoneyes.repository.RecetaRepository
import com.example.crimsoneyes.repository.UsuarioRepository
import com.example.crimsoneyes.view.HomeScreen
import com.example.crimsoneyes.view.PantallaLogin
import com.example.crimsoneyes.view.PantallaRegistro
import com.example.crimsoneyes.view.ProductosScreen
import com.example.crimsoneyes.view.ProfileScreen

@Composable
fun AppNavigation(
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val navController = rememberNavController()

    // Inicializar base de datos y repositorios una sola vez
    val database = remember { CrimsonDataBase.get(context) }
    val usuarioRepository = remember { UsuarioRepository(database) }
    val recetaRepository = remember { RecetaRepository(database) }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // Pantalla de Login
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(usuarioRepository)
            )

            PantallaLogin(
                viewModel = loginViewModel,
                isDarkMode = isDarkMode,
                onToggleTheme = onToggleTheme,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // Pantalla de Registro
        composable(Screen.Register.route) {
            val registerViewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(usuarioRepository)
            )

            PantallaRegistro(
                viewModel = registerViewModel,
                isDarkMode = isDarkMode,
                onToggleTheme = onToggleTheme,
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla Home
        composable(Screen.Home.route) {
            val recetaViewModel: RecetaViewModel = viewModel(
                factory = RecetaViewModelFactory(recetaRepository)
            )

            HomeScreen(
                viewModel = recetaViewModel,
                isDarkMode = isDarkMode,
                onToggleTheme = onToggleTheme,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToProductos = {
                    navController.navigate(Screen.Producto.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        // Pantalla de Productos
        composable(Screen.Producto.route) {
            val productoViewModel: ProductoViewModel = viewModel()

            ProductosScreen(
                viewModel = productoViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de Perfil
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}