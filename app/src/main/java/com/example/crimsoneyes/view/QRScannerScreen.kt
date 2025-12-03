package com.example.crimsoneyes.view

import android.Manifest
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.crimsoneyes.controller.QRScannerViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun QRScannerScreen(
    onQRScanned: (String) -> Unit,
    onBackClick: () -> Unit,
    isDarkMode: Boolean
) {
    val viewModel: QRScannerViewModel = viewModel()
    val scanResult by viewModel.scanResult
    val cameraError by viewModel.cameraError
    val isScanning by viewModel.isScanning

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    // Cuando se detecta un QR
    LaunchedEffect(scanResult.isSuccess) {
        if (scanResult.isSuccess) {
            onQRScanned(scanResult.value)
            viewModel.resetScan()
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Escanear Código QR",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    color = if (isDarkMode) Color(0xFF1A1A1A) else Color.Black
                )
        ) {
            when {
                !cameraPermissionState.status.isGranted -> {
                    // Permiso no otorgado
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .size(64.dp)
                                .padding(bottom = 16.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            "Permiso de Cámara Requerido",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Se necesita acceso a la cámara para escanear códigos QR",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { cameraPermissionState.launchPermissionRequest() }
                        ) {
                            Text("Otorgar Permiso")
                        }
                    }
                }

                cameraError != null -> {
                    // Error en cámara
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .size(64.dp)
                                .padding(bottom = 16.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            "Error de Cámara",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            cameraError ?: "Error desconocido",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = onBackClick) {
                            Text("Volver")
                        }
                    }
                }

                isScanning -> {
                    // Cámara activa
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Preview de cámara
                        AndroidView(
                            factory = { ctx ->
                                PreviewView(ctx).apply {
                                    viewModel.startCamera(ctx, lifecycleOwner, this)
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )

                        // Overlay con instrucciones
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(0.dp))

                            // Marco QR indicador
                            Box(
                                modifier = Modifier
                                    .size(280.dp)
                                    .border(
                                        width = 3.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            )

                            // Texto de instrucciones
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Color.Black.copy(alpha = 0.7f),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(16.dp),
                                color = Color.Transparent
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Alinea el código QR",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        "Acerca el código hacia la cámara",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.White.copy(alpha = 0.8f),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }

                else -> {
                    // Estado inicial/cargando
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(64.dp)
                                .padding(bottom = 16.dp)
                        )
                        Text(
                            "Inicializando cámara...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

// Composable para el border sin usar BorderModifier que no existe
@Composable
private fun Modifier.border(
    width: androidx.compose.ui.unit.Dp,
    color: Color,
    shape: RoundedCornerShape
) = this.then(
    Modifier.background(color = Color.Transparent, shape = shape)
)

