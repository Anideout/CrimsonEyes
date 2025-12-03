package com.example.crimsoneyes.navigation

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.crimsoneyes.api.ApiRepository
import com.example.crimsoneyes.controller.CarritoFactory
import com.example.crimsoneyes.controller.CarritoViewModel
import com.example.crimsoneyes.controller.LoginViewModel
import com.example.crimsoneyes.controller.LoginViewModelFactory
import com.example.crimsoneyes.controller.ProductoViewModel
import com.example.crimsoneyes.controller.ProductoViewModelFactory
import com.example.crimsoneyes.controller.ProfileViewModel
import com.example.crimsoneyes.controller.ProfileViewModelFactory
import com.example.crimsoneyes.controller.RecetaViewModel
import com.example.crimsoneyes.controller.RecetaViewModelFactory
import com.example.crimsoneyes.controller.RegisterViewModel
import com.example.crimsoneyes.controller.RegisterViewModelFactory
import com.example.crimsoneyes.db.CrimsonDataBase
import com.example.crimsoneyes.repository.RecetaRepository
import com.example.crimsoneyes.view.CarritoScreen
import com.example.crimsoneyes.view.CheckoutScreen
import com.example.crimsoneyes.view.HomeScreen
import com.example.crimsoneyes.view.PantallaLogin
import com.example.crimsoneyes.view.PantallaRegistro
import com.example.crimsoneyes.view.ProductosScreen
import com.example.crimsoneyes.view.ProfileScreen
import com.example.crimsoneyes.view.QRScannerScreen
import com.example.crimsoneyes.view.VentaDetallesScreen

@Composable
fun AppNavigation(
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit
) {
    val context = LocalContext.current
    val navController = rememberNavController()

    // Base de datos y repositorios
    val database = remember { CrimsonDataBase.get(context) }
    val recetaDao = remember { database.RecetaDao() }
    val recetaRepository = remember { RecetaRepository(recetaDao) }

    // Variable para guardar el email del usuario logueado
    var usuarioLogueadoEmail by remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // Pantalla de Login
        composable(Screen.Login.route) {
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory()
            )

            PantallaLogin(
                viewModel = loginViewModel,
                isDarkMode = isDarkMode,
                onToggleTheme = onToggleTheme,
                onLoginSuccess = { email ->
                    usuarioLogueadoEmail = email
                    Log.d("AppNavigation", "Usuario logueado: $email")
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
                factory = RegisterViewModelFactory()
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
                factory = RecetaViewModelFactory(
                    recetaRepository,
                    usuarioLogueadoEmail
                ),
                key = "receta_${usuarioLogueadoEmail}"
            )

            HomeScreen(
                viewModel = recetaViewModel,
                isDarkMode = isDarkMode,
                onToggleTheme = onToggleTheme,
                onLogout = {
                    usuarioLogueadoEmail = ""
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToProductos = {
                    navController.navigate(Screen.Producto.route)
                },
                onNavigateToProfile = {
                    navController.navigate(
                        Screen.Profile(usuarioLogueadoEmail).createRoute(usuarioLogueadoEmail)
                    )
                },
                onNavigateToQRScanner = {
                    navController.navigate(Screen.QRScanner.route)
                }
            )
        }

        // Pantalla de Productos
        composable(Screen.Producto.route) {
            Log.d("AppNavigation", "=== PRODUCTOS SCREEN ===")
            Log.d("AppNavigation", "Email: '$usuarioLogueadoEmail'")

            // Crear CarritoViewModel
            val carritoViewModel: CarritoViewModel = viewModel(
                factory = CarritoFactory(
                    ApiRepository.carritoApi,
                    ApiRepository.itemCarritoApi,
                    usuarioLogueadoEmail
                ),
                key = "carrito_${usuarioLogueadoEmail}"
            )

            // Crear ProductoViewModel
            val productoViewModel: ProductoViewModel = viewModel(
                factory = ProductoViewModelFactory(
                    application = context.applicationContext as Application,
                    carritoViewModel = carritoViewModel
                )
            )

            ProductosScreen(
                viewModel = productoViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCarrito = {
                    navController.navigate(Screen.Carrito.route)
                }
            )
        }

        // Pantalla de Carrito
        composable(Screen.Carrito.route) {
            Log.d("AppNavigation", "=== CARRITO SCREEN ===")
            Log.d("AppNavigation", "Email actual: '$usuarioLogueadoEmail'")

            // Verificar que el email no esté vacío
            if (usuarioLogueadoEmail.isEmpty()) {
                // Mostrar error si no hay usuario logueado
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Error: Usuario no autenticado",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Por favor, inicia sesión nuevamente",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(
                            onClick = { navController.popBackStack() }
                        ) {
                            Text("Volver")
                        }
                    }
                }
            } else {
                // Crear CarritoViewModel con el email correcto
                val carritoViewModel: CarritoViewModel = viewModel(
                    factory = CarritoFactory(
                        ApiRepository.carritoApi,
                        ApiRepository.itemCarritoApi,
                        usuarioLogueadoEmail
                    ),
                    key = "carrito_${usuarioLogueadoEmail}"
                )

                CarritoScreen(
                    viewModel = carritoViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToCheckout = { email ->
                        navController.navigate(
                            Screen.Checkout(email).createRoute(email)
                        )
                    }
                )
            }
        }

        // Pantalla de Perfil
        composable(
            route = Screen.Profile("").route,
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val emailFromRoute = backStackEntry.arguments?.getString("email").orEmpty()
            val emailFinal = if (emailFromRoute.isNotEmpty()) emailFromRoute else usuarioLogueadoEmail

            Log.d("AppNavigation", "ProfileScreen - Email recibido: '$emailFinal'")

            if (emailFinal.isNotEmpty()) {
                val profileViewModel: ProfileViewModel = viewModel(
                    factory = ProfileViewModelFactory(emailFinal),
                    key = "profile_$emailFinal"
                )

                ProfileScreen(
                    viewModel = profileViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            } else {
                // Mostrar error si no hay email
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Error: No se pudo cargar el perfil",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Button(
                            onClick = { navController.popBackStack() }
                        ) {
                            Text("Volver")
                        }
                    }
                }
            }
        }

        // Pantalla de Escaner QR
        composable(Screen.QRScanner.route) {
            QRScannerScreen(
                onQRScanned = { qrValue ->
                    Log.d("AppNavigation", "QR escaneado: $qrValue")

                    // Procesar QR de venta: venta:ID
                    if (qrValue.startsWith("venta:")) {
                        val ventaId = qrValue.substringAfter("venta:").toIntOrNull() ?: 0

                        if (ventaId > 0) {
                            Log.d("AppNavigation", "Navegando a VentaDetalles - ID: $ventaId, Email: $usuarioLogueadoEmail")
                            // Solo navegar, sin popUpTo para mantener el back stack
                            navController.navigate(
                                Screen.VentaDetalles(ventaId, usuarioLogueadoEmail).createRoute(ventaId, usuarioLogueadoEmail)
                            )
                        } else {
                            Log.e("AppNavigation", "ID de venta inválido: $ventaId")
                            navController.popBackStack()
                        }
                    } else if (qrValue.contains("venta/")) {
                        // Formato antiguo por compatibilidad
                        val ventaIdRegex = """venta/(\d+)""".toRegex()
                        val emailRegex = """email=([^&]+)""".toRegex()

                        val ventaIdMatch = ventaIdRegex.find(qrValue)
                        val emailMatch = emailRegex.find(qrValue)

                        if (ventaIdMatch != null && emailMatch != null) {
                            val ventaId = ventaIdMatch.groupValues[1].toIntOrNull() ?: 0
                            val emailFromQR = emailMatch.groupValues[1]

                            if (ventaId > 0) {
                                Log.d("AppNavigation", "Navegando a VentaDetalles (formato antiguo) - ID: $ventaId, Email: $emailFromQR")
                                navController.navigate(
                                    Screen.VentaDetalles(ventaId, emailFromQR).createRoute(ventaId, emailFromQR)
                                )
                            } else {
                                Log.e("AppNavigation", "ID de venta inválido en formato antiguo")
                                navController.popBackStack()
                            }
                        } else {
                            Log.w("AppNavigation", "QR en formato antiguo pero sin datos válidos")
                            navController.popBackStack()
                        }
                    } else {
                        Log.w("AppNavigation", "QR no reconocido: $qrValue")
                        navController.popBackStack()
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
                isDarkMode = isDarkMode
            )
        }

        // Pantalla de Checkout
        composable(
            route = Screen.Checkout("").route,
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val emailFromRoute = backStackEntry.arguments?.getString("email").orEmpty()
            val emailFinal = if (emailFromRoute.isNotEmpty()) emailFromRoute else usuarioLogueadoEmail

            if (emailFinal.isNotEmpty()) {
                val carritoViewModel: CarritoViewModel = viewModel(
                    factory = CarritoFactory(
                        ApiRepository.carritoApi,
                        ApiRepository.itemCarritoApi,
                        emailFinal
                    ),
                    key = "carrito_checkout_$emailFinal"
                )

                val carritoState by carritoViewModel.state.collectAsState()

                CheckoutScreen(
                    usuarioEmail = emailFinal,
                    itemsCarrito = carritoState.items,
                    metodoPago = carritoState.metodoPagoSeleccionado ?: "",
                    onVentaExitosa = { venta ->
                        // Navegar a detalles de la venta
                        navController.navigate(
                            Screen.VentaDetalles(venta.id, emailFinal).createRoute(venta.id, emailFinal)
                        ) {
                            popUpTo(Screen.Checkout("").route) { inclusive = true }
                        }
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        // Pantalla de Detalles de Venta
        composable(
            route = Screen.VentaDetalles(0, "").route,
            arguments = listOf(
                navArgument("ventaId") {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val ventaId = backStackEntry.arguments?.getInt("ventaId") ?: 0
            val emailFromRoute = backStackEntry.arguments?.getString("email").orEmpty()
            val emailFinal = if (emailFromRoute.isNotEmpty()) emailFromRoute else usuarioLogueadoEmail

            if (ventaId > 0 && emailFinal.isNotEmpty()) {
                VentaDetallesScreen(
                    ventaId = ventaId,
                    usuarioEmail = emailFinal,
                    onBack = {
                        // Volver al Home desde VentaDetallesScreen
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.VentaDetalles(0, "").route) { inclusive = true }
                        }
                    },
                    onScanQR = {
                        navController.navigate(Screen.QRScanner.route)
                    }
                )
            }
        }
    }
}