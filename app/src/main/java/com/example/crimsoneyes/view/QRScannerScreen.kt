package com.example.crimsoneyes.view

import android.Manifest
import android.util.Log
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
    val cameraReady by viewModel.cameraReady
    val isInitializing by viewModel.isInitializing

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopCamera()
        }
    }

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(cameraPermissionState.status.isGranted) {
        if (cameraPermissionState.status.isGranted && !cameraReady && !isInitializing) {
            kotlinx.coroutines.delay(100)
            Log.d("QRScanner", "Listo para iniciar cámara")
        }
    }

    // Cuando se detecta un QR
    LaunchedEffect(scanResult.isSuccess) {
        if (scanResult.isSuccess && scanResult.value.isNotEmpty()) {
            Log.d("QRScanner", "LaunchedEffect detectó QR: ${scanResult.value}")
            Log.d("QRScanner", "Llamando onQRScanned...")

            // Llamar al callback PRIMERO
            onQRScanned(scanResult.value)

            // Esperar un momento para que la navegación ocurra
            kotlinx.coroutines.delay(100)

            // Resetear DESPUÉS
            viewModel.resetScan()

            Log.d("QRScanner", "Scanner reseteado, navegación completada")
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
                    IconButton(onClick = {
                        viewModel.stopCamera()
                        onBackClick()
                    }) {
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
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Se necesita acceso a la cámara para escanear códigos QR",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = Color.White.copy(alpha = 0.7f)
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
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            cameraError ?: "Error desconocido",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(onClick = {
                            viewModel.stopCamera()
                            onBackClick()
                        }) {
                            Text("Volver")
                        }
                    }
                }

                cameraPermissionState.status.isGranted -> {
                    // Permiso otorgado - mostrar cámara
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Preview de cámara
                        AndroidView(
                            factory = { ctx ->
                                Log.d("QRScanner", "AndroidView factory ejecutándose")
                                PreviewView(ctx).apply {
                                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                                    Log.d("QRScanner", "PreviewView creado, iniciando cámara...")
                                    // Iniciar cámara en un post para asegurar que el View está listo
                                    post {
                                        viewModel.startCamera(ctx, lifecycleOwner, this)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxSize()
                        )

                        // Overlay con instrucciones (siempre visible)
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
                                        color = if (isScanning) MaterialTheme.colorScheme.primary else Color.Gray,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            )

                            // Texto de instrucciones
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = Color.Black.copy(alpha = 0.7f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (isInitializing) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "Inicializando cámara...",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White
                                        )
                                    } else {
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
                }

                else -> {
                    // Estado inicial - nunca debería llegar aquí
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(64.dp)
                                .padding(bottom = 16.dp),
                            color = Color.White
                        )
                        Text(
                            "Preparando cámara...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}