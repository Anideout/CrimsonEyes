package com.example.crimsoneyes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.crimsoneyes.controller.LoginViewModel
import com.example.crimsoneyes.controller.LoginViewModelFactory
import com.example.crimsoneyes.controller.RecetaViewModel
import com.example.crimsoneyes.controller.RecetaViewModelFactory
import com.example.crimsoneyes.controller.RegisterViewModel
import com.example.crimsoneyes.controller.RegisterViewModelFactory
import com.example.crimsoneyes.db.CrimsonDataBase
import com.example.crimsoneyes.repository.RecetaRepository
import com.example.crimsoneyes.repository.UsuarioRepository
import com.example.crimsoneyes.ui.theme.CrimsonEyesTheme
import com.example.crimsoneyes.view.HomeScreen
import com.example.crimsoneyes.view.PantallaLogin
import com.example.crimsoneyes.view.PantallaRegistro

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar base de datos y repositorios
        val database = CrimsonDataBase.get(applicationContext)
        val usuarioRepository = UsuarioRepository(database)
        val recetaRepository = RecetaRepository(database)

        setContent {
            val systemDark = isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf(systemDark) }

            CrimsonEyesTheme(darkTheme = isDarkMode) {
                AppContent(
                    usuarioRepository = usuarioRepository,
                    recetaRepository = recetaRepository,
                    isDarkMode = isDarkMode,
                    onToggleTheme = { isDarkMode = !isDarkMode }
                )
            }
        }
    }
}

@Composable
fun AppContent(
    usuarioRepository: UsuarioRepository,
    recetaRepository: RecetaRepository,
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(usuarioRepository)
            )


            PantallaLogin(
                onLoginSucces = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                isDarkMode = isDarkMode,
                onToggleTheme = onToggleTheme,
                viewModel = loginViewModel
            )
        }

        composable("register") {
            val registerViewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(usuarioRepository)
            )
            PantallaRegistro(
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                isDarkMode = isDarkMode,
                onToggleTheme = onToggleTheme,
                viewModel = registerViewModel
            )
        }

        composable("home") {
            // Instanciar el ViewModel de recetas
            val recetaViewModel: RecetaViewModel = viewModel(
                factory  = RecetaViewModelFactory(recetaRepository)
            )

            HomeScreen(
                viewModel = recetaViewModel,
                isDarkMode = isDarkMode,
                onToggleTheme = onToggleTheme,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}
