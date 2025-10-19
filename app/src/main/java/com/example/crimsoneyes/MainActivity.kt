package com.example.crimsoneyes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.crimsoneyes.controller.LoginViewModel
import com.example.crimsoneyes.controller.LoginViewModelFactory
import com.example.crimsoneyes.controller.RegisterViewModel
import com.example.crimsoneyes.controller.RegisterViewModelFactory
import com.example.crimsoneyes.db.CrimsonDataBase
import com.example.crimsoneyes.repository.UsuarioRepository
import com.example.crimsoneyes.ui.theme.CrimsonEyesTheme
import com.example.crimsoneyes.view.PantallaLogin
import com.example.crimsoneyes.view.PantallaRegistro

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar db y el repositorio
        val database = CrimsonDataBase.get(applicationContext)
        val repository = UsuarioRepository(database)

        setContent {
            val systemDark = isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf(systemDark) }

            CrimsonEyesTheme(darkTheme = isDarkMode) {
                AppContent(
                    repository = repository,
                    isDarkMode = isDarkMode,
                    onToggleTheme = { isDarkMode = !isDarkMode }
                )
            }
        }
    }
}

@Composable
fun AppContent(
    repository: UsuarioRepository,
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
                factory = LoginViewModelFactory(repository)
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
                factory = RegisterViewModelFactory(repository)
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
            HomeScreen(
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


@Composable
fun HomeScreen(
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    onLogout: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Bienvenido a CrimsonEyes!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onToggleTheme,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.6f)
            ) {
                Text(text = if (isDarkMode) "Modo Claro" else "Modo Oscuro")
            }

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.6f)
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}