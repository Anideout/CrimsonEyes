package com.example.crimsoneyes.view

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.crimsoneyes.controller.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    // Cargar datos del usuario cuando el ProfileScreen se compone
    LaunchedEffect(Unit) {
        viewModel.cargarDatosUsuario()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mi Perfil",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Fondo degradado decorativo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Mostrar loading mientras carga datos
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Header con ícono
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                modifier = Modifier.size(100.dp),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                shadowElevation = 8.dp
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Person,
                                    contentDescription = null,
                                    modifier = Modifier.padding(28.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = if (state.nombre.isNotBlank()) state.nombre else "Usuario",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = if (state.email.isNotBlank()) state.email else "Correo electrónico",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Información Personal
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                "Información Personal",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            OutlinedTextField(
                                value = state.nombre,
                                onValueChange = { viewModel.actualizarNombre(it) },
                                label = { Text("Nombre Completo") },
                                leadingIcon = {
                                    Icon(Icons.Outlined.Person, contentDescription = null)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = state.email,
                                onValueChange = { },
                                label = { Text("Correo Electrónico") },
                                leadingIcon = {
                                    Icon(Icons.Outlined.Email, contentDescription = null)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                enabled = false,
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                    disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )

                            OutlinedTextField(
                                value = state.rut,
                                onValueChange = { },
                                label = { Text("RUT") },
                                leadingIcon = {
                                    Icon(Icons.Outlined.Badge, contentDescription = null)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                enabled = false,
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                    disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    // Cambio de Contraseña
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                "Cambiar Contraseña",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            OutlinedTextField(
                                value = state.currentPassword,
                                onValueChange = { viewModel.actualizarPasswordActual(it) },
                                label = { Text("Contraseña Actual") },
                                leadingIcon = {
                                    Icon(Icons.Outlined.Lock, contentDescription = null)
                                },
                                trailingIcon = {
                                    IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                                        Icon(
                                            if (showCurrentPassword) Icons.Outlined.Visibility
                                            else Icons.Outlined.VisibilityOff,
                                            contentDescription = if (showCurrentPassword) "Ocultar" else "Mostrar"
                                        )
                                    }
                                },
                                visualTransformation = if (showCurrentPassword) VisualTransformation.None
                                else PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = state.newPassword,
                                onValueChange = { viewModel.actualizarNuevaPassword(it) },
                                label = { Text("Nueva Contraseña") },
                                leadingIcon = {
                                    Icon(Icons.Outlined.Lock, contentDescription = null)
                                },
                                trailingIcon = {
                                    IconButton(onClick = { showNewPassword = !showNewPassword }) {
                                        Icon(
                                            if (showNewPassword) Icons.Outlined.Visibility
                                            else Icons.Outlined.VisibilityOff,
                                            contentDescription = if (showNewPassword) "Ocultar" else "Mostrar"
                                        )
                                    }
                                },
                                visualTransformation = if (showNewPassword) VisualTransformation.None
                                else PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = state.confirmPassword,
                                onValueChange = { viewModel.actualizarConfirmarPassword(it) },
                                label = { Text("Confirmar Nueva Contraseña") },
                                leadingIcon = {
                                    Icon(Icons.Outlined.Lock, contentDescription = null)
                                },
                                trailingIcon = {
                                    IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                        Icon(
                                            if (showConfirmPassword) Icons.Outlined.Visibility
                                            else Icons.Outlined.VisibilityOff,
                                            contentDescription = if (showConfirmPassword) "Ocultar" else "Mostrar"
                                        )
                                    }
                                },
                                visualTransformation = if (showConfirmPassword) VisualTransformation.None
                                else PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }
                    }

                    // Mensaje de error
                    AnimatedVisibility(
                        visible = state.errorMessage != null,
                        enter = slideInVertically() + fadeIn(),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Error,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    state.errorMessage ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }

                    // Mensaje de éxito
                    AnimatedVisibility(
                        visible = state.successMessage != null,
                        enter = slideInVertically() + fadeIn(),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                                Text(
                                    state.successMessage ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }

                    // Botón guardar
                    Button(
                        onClick = { viewModel.guardarCambios() },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        enabled = !state.isSaving,
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.5.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                "Guardando...",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            Icon(Icons.Default.Save, contentDescription = null)
                            Spacer(Modifier.width(12.dp))
                            Text(
                                "Guardar Cambios",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}