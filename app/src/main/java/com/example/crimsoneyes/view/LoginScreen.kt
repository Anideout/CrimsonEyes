package com.example.crimsoneyes.view

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.shadow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.crimsoneyes.R
import com.example.crimsoneyes.controller.LoginState
import com.example.crimsoneyes.controller.LoginViewModel

@Composable
fun PantallaLogin(
    // Variables asignadas para confirmar que logeas correctamente
    onLoginSucces: () -> Unit,
    // Darkmode
    isDarkMode: Boolean,
    onNavigateToRegister: () -> Unit,
    // Para cambiar el tema
    onToggleTheme: () -> Unit,
    // ViewModel inyectado (puedes pasarlo desde MainActivity o usar viewModel())
    viewModel: LoginViewModel = viewModel()
) {
    // Variables de email y password
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    // Observar el estado del login desde el ViewModel
    val loginState by viewModel.loginState.collectAsState()

    // Variable para controlar el mensaje de error local (campos vacíos)
    var localErrorMessage by remember { mutableStateOf("") }

    // Manejar el éxito del login
    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                // Usuario autenticado correctamente, navegar
                onLoginSucces()
                // Resetear el estado después de navegar
                viewModel.resetState()
            }
            else -> { }
        }
    }

    // Determinar qué mensaje de error mostrar
    val errorMessage = when (loginState) {
        is LoginState.Error -> (loginState as LoginState.Error).message
        else -> localErrorMessage
    }

    // Verificar si está cargando
    val isLoading = loginState is LoginState.Loading

    // Para almacenar elementos
    Box(
        // Indicamos que ocupe toda la pantalla
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        // Botón para el dark mode
        IconButton(
            onClick = onToggleTheme,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.oscurin),
                contentDescription = "cambiar tema",
                modifier = Modifier.size(30.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card del logo
            Card(
                modifier = Modifier
                    .size(140.dp)
                    .shadow(8.dp, CircleShape),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.sunglass),
                        contentDescription = "Logo",
                        modifier = Modifier.size(70.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "CrimsonEyes",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ve el mundo con otros ojos, ve con Crimson Eyes",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Campo para ingresar email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    localErrorMessage = ""
                    // Resetear estado de error del ViewModel si existe
                    if (loginState is LoginState.Error) {
                        viewModel.resetState()
                    }
                },
                label = { Text("Email o Usuario") },
                singleLine = true,
                enabled = !isLoading, // Deshabilitar mientras carga
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para ingresar contraseña
            OutlinedTextField(
                value = pass,
                onValueChange = {
                    pass = it
                    localErrorMessage = ""
                    if (loginState is LoginState.Error) {
                        viewModel.resetState()
                    }
                },
                label = { Text("Contraseña") },
                singleLine = true,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )

            // Mostrar mensaje de error con animación
            AnimatedVisibility(
                visible = errorMessage.isNotEmpty(),
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de inicio de sesión
            Button(
                onClick = {
                    // Validación local de campos vacíos
                    if (email.isNotEmpty() && pass.isNotEmpty()) {
                        // Llamar al ViewModel para hacer login
                        viewModel.login(email, pass)
                    } else {
                        localErrorMessage = "Por favor completa todos los campos!"
                    }
                },


                enabled = !isLoading, // Deshabilitar mientras carga
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                // Mostrar spinner o texto según el estado
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Iniciar sesión uwu",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text(
                    text = "¿No tienes cuenta? Regístrate aquí!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }
    }
}