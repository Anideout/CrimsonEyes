package com.example.crimsoneyes.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crimsoneyes.api.ApiRepository
import com.example.crimsoneyes.model.VentaResponse
import com.example.crimsoneyes.repository.VentaRepository
import com.example.crimsoneyes.utils.QRCodeGenerator
import com.example.crimsoneyes.controller.CheckoutViewModel
import com.example.crimsoneyes.controller.CheckoutViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentaDetallesScreen(
    ventaId: Int,
    usuarioEmail: String,
    onBack: () -> Unit,
    onScanQR: () -> Unit = {}
) {
    // Crear ViewModel
    val ventaRepository = remember {
        VentaRepository(ApiRepository.ventaApi)
    }
    val factory = remember {
        CheckoutViewModelFactory(ventaRepository, usuarioEmail)
    }
    val viewModel: CheckoutViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = factory
    )

    val uiState by viewModel.uiState.collectAsState()

    // Cargar detalles de la venta
    LaunchedEffect(ventaId) {
        viewModel.obtenerDetallesVenta(ventaId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles de Venta", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            uiState.cargando -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Cargando detalles de la venta...")
                }
            }
            !uiState.error.isNullOrEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Red.copy(alpha = 0.1f)),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Red.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "❌ Error",
                                color = Color.Red,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.error!!,
                                color = Color.Red,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = onBack) {
                        Text("Volver")
                    }
                }
            }
            uiState.compraExitosa != null -> {
                VentaDetallesContent(
                    venta = uiState.compraExitosa!!,
                    qrData = uiState.qrData ?: "",
                    innerPadding = innerPadding,
                    onBack = onBack,
                    onScanQR = onScanQR
                )
            }
        }
    }
}

@Composable
private fun VentaDetallesContent(
    venta: VentaResponse,
    qrData: String,
    innerPadding: PaddingValues,
    onBack: () -> Unit,
    onScanQR: () -> Unit = {}
) {
    val qrBitmap = remember { QRCodeGenerator.generateQRCode(qrData) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //Información Principal
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Venta #${venta.id}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(statusBackgroundColor(venta.estado)),
                        color = statusBackgroundColor(venta.estado)
                    ) {
                        Text(
                            text = venta.estado,
                            modifier = Modifier.padding(8.dp, 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusTextColor(venta.estado)
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.fillMaxWidth())

                DetalleRow("Email:", venta.usuarioEmail)
                DetalleRow("Fecha:", formatearFecha(venta.fecha))
                DetalleRow("Método de Pago:", venta.metodoPago.takeIf { it.isNotEmpty() } ?: "No especificado")

                HorizontalDivider(modifier = Modifier.fillMaxWidth())

                DetalleRow(
                    "Total:",
                    "$${"%.0f".format(venta.total)}",
                    MaterialTheme.colorScheme.primary,
                    bold = true
                )
            }
        }

        if (venta.detalles.isNotEmpty()) {
            Text(
                text = "Artículos",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    venta.detalles.forEach { detalle ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = detalle.productoNombre,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "Cantidad: ${detalle.cantidad} x $${"%.0f".format(detalle.precioUnitario)}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                                Text(
                                    text = "$${"%.0f".format(detalle.subtotal)}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            if (venta.detalles.last() != detalle) {
                                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                            }
                        }
                    }
                }
            }
        }

        Text(
            text = "Comprobante Digital",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Card(
            modifier = Modifier
                .size(220.dp)
                .clip(RoundedCornerShape(12.dp))
                .align(Alignment.CenterHorizontally),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Image(
                painter = BitmapPainter(qrBitmap.asImageBitmap()),
                contentDescription = "QR Code",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentScale = ContentScale.Fit
            )
        }

        Text(
            text = "ID Venta: ${venta.id}",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Escanea el código QR para verificar esta venta",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Button(
            onClick = onScanQR,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("Escanear QR")
        }

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Inicio")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DetalleRow(
    label: String,
    valor: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    bold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontSize = 12.sp
        )
        Text(
            text = valor,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            color = color,
            fontSize = 12.sp
        )
    }
}

private fun formatearFecha(fecha: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(fecha)
        outputFormat.format(date ?: Date())
    } catch (_: Exception) {
        fecha
    }
}

private fun statusBackgroundColor(estado: String): Color {
    return when (estado) {
        "CONFIRMADA" -> Color.Green.copy(alpha = 0.2f)
        "PENDIENTE" -> Color.Yellow.copy(alpha = 0.2f)
        "ENVIADA" -> Color.Blue.copy(alpha = 0.2f)
        "ENTREGADA" -> Color.Green.copy(alpha = 0.2f)
        "CANCELADA" -> Color.Red.copy(alpha = 0.2f)
        else -> Color.Gray.copy(alpha = 0.2f)
    }
}

private fun statusTextColor(estado: String): Color {
    return when (estado) {
        "CONFIRMADA" -> Color.Green
        "PENDIENTE" -> Color(0xFFF57C00)  // Orange
        "ENVIADA" -> Color.Blue
        "ENTREGADA" -> Color.Green
        "CANCELADA" -> Color.Red
        else -> Color.Gray
    }
}

