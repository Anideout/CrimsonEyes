package com.example.crimsoneyes.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.crimsoneyes.controller.ProductoViewModel
import com.example.crimsoneyes.model.Producto
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
        viewModel: ProductoViewModel,
        onNavigateBack: () -> Unit,
        onNavigateToCarrito: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val categorias =
            listOf(
                    "Todos",
                    "Lectura",
                    "Redondos",
                    "Aceleracionista",
                    "Armani",
                    "IDM",
                    "Filtros azul"
            )

    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Column {
                                Text("Catálogo de Lentes", fontWeight = FontWeight.Bold)
                                Text(
                                        "${state.list.size} productos disponibles",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Volver"
                                )
                            }
                        },
                        actions = {
                            BadgedBox(
                                    badge = {
                                        if (state.carritoCount > 0) {
                                            Badge { Text("${state.carritoCount}") }
                                        }
                                    }
                            ) {
                                IconButton(onClick = onNavigateToCarrito) {
                                    Icon(
                                            imageVector = Icons.Outlined.ShoppingCart,
                                            contentDescription = "Carrito"
                                    )
                                }
                            }
                        }
                )
            },
            snackbarHost = {
                //  mensajes de compra
                state.mensajeCompra?.let { mensaje ->
                    Snackbar(
                            modifier = Modifier.padding(16.dp),
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ) { Text(mensaje) }
                }
            }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Fondo degradado sutil
            Box(
                    modifier =
                            Modifier.fillMaxWidth()
                                    .height(250.dp)
                                    .background(
                                            Brush.verticalGradient(
                                                    colors =
                                                            listOf(
                                                                    MaterialTheme.colorScheme
                                                                            .secondaryContainer
                                                                            .copy(alpha = 0.3f),
                                                                    Color.Transparent
                                                            )
                                            )
                                    )
            )

            LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Filtros por categoría
                item {
                    CategoriaFilterChips(
                            categorias = categorias,
                            categoriaSeleccionada = state.categoriaSeleccionada,
                            onCategoriaSelected = { viewModel.filtarPorCategoria(it) }
                    )
                }

                if (state.isListLoading && state.list.isEmpty()) {
                    item { LoadingProductosCard() }
                }
                // ERROR
                else if (state.listError != null) {
                    item {
                        ErrorProductosCard(
                                mensaje = state.listError ?: "Error desconocido",
                                onRetry = { viewModel.cargarProductos() }
                        )
                    }
                }
                // LISTA VACÍA
                else if (state.list.isEmpty()) {
                    item { EmptyProductosCard() }
                }
                // LISTA DE PRODUCTOS
                else {
                    items(items = state.list, key = { it.id }) { producto ->
                        ProductoCard(
                                producto = producto,
                                onProductoClick = { viewModel.seleccionarProducto(producto) },
                                onAgregarCarrito = { viewModel.agregarAlCarrito(producto) }
                        )
                    }
                }

                // Padding final
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }

    state.productoSeleccionado?.let { producto ->
        ProductoDetalleDialog(
                producto = producto,
                onDismiss = { viewModel.limpiarProductoSeleccionado() },
                onComprar = {
                    viewModel.agregarAlCarrito(producto)
                    viewModel.limpiarProductoSeleccionado()
                }
        )
    }
}

@Composable
private fun CategoriaFilterChips(
        categorias: List<String>,
        categoriaSeleccionada: String,
        onCategoriaSelected: (String) -> Unit
) {
    LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(categorias) { categoria ->
            FilterChip(
                    selected = categoria == categoriaSeleccionada,
                    onClick = { onCategoriaSelected(categoria) },
                    label = { Text(categoria) },
                    leadingIcon =
                            if (categoria == categoriaSeleccionada) {
                                {
                                    Icon(
                                            Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                    )
                                }
                            } else null
            )
        }
    }
}

@Composable
private fun ProductoCard(
        producto: Producto,
        onProductoClick: () -> Unit,
        onAgregarCarrito: () -> Unit
) {
    Card(
            modifier = Modifier.fillMaxWidth().clickable { onProductoClick() },
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Imagen del producto
            Card(modifier = Modifier.size(100.dp), shape = RoundedCornerShape(12.dp)) {
                Image(
                        painter = painterResource(id = producto.imagenResId),
                        contentDescription = producto.nombre,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                        producto.categoria,
                                        style = MaterialTheme.typography.labelSmall
                                )
                            },
                            modifier = Modifier.height(24.dp),
                            colors =
                                    AssistChipDefaults.assistChipColors(
                                            containerColor =
                                                    MaterialTheme.colorScheme.secondaryContainer
                                    )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Nombre
                    Text(
                            text = producto.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Descripción
                    Text(
                            text = producto.descripcion,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Indicador de Stock
                    Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                                imageVector = Icons.Default.Storage,
                                contentDescription = "Stock",
                                modifier = Modifier.size(14.dp),
                                tint = if (producto.stock > 5) MaterialTheme.colorScheme.primary
                                       else if (producto.stock > 0) MaterialTheme.colorScheme.tertiary
                                       else MaterialTheme.colorScheme.error
                        )
                        Text(
                                text = if (producto.stock > 0) "${producto.stock} disponibles"
                                       else "Sin stock",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (producto.stock > 5) MaterialTheme.colorScheme.primary
                                        else if (producto.stock > 0) MaterialTheme.colorScheme.tertiary
                                        else MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    // Precio
                    Text(
                            text = formatoPrecio(producto.precio),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                    )

                    // Botón agregar
                    FilledTonalIconButton(
                            onClick = { onAgregarCarrito() },
                            modifier = Modifier.size(40.dp),
                            enabled = producto.stock > 0
                    ) {
                        Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Agregar al carrito"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductoDetalleDialog(
        producto: Producto,
        onDismiss: () -> Unit,
        onComprar: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Imagen grande
                Box(modifier = Modifier.fillMaxWidth().height(250.dp)) {
                    Image(
                            painter = painterResource(id = producto.imagenResId),
                            contentDescription = producto.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                    )

                    // Botón cerrar
                    IconButton(
                            onClick = onDismiss,
                            modifier =
                                    Modifier.align(Alignment.TopEnd)
                                            .padding(8.dp)
                                            .background(
                                                    MaterialTheme.colorScheme.surface.copy(
                                                            alpha = 0.9f
                                                    ),
                                                    CircleShape
                                            )
                    ) { Icon(Icons.Default.Close, contentDescription = "Cerrar") }

                    // Badge de categoría
                    Surface(
                            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                                text = producto.categoria,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Contenido
                Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Nombre
                    Text(
                            text = producto.nombre,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                    )

                    // Descripción
                    Text(
                            text = producto.descripcion,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Divider()

                    // Stock
                    Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                                Icons.Outlined.CheckCircle,
                                contentDescription = null,
                                tint =
                                        if (producto.stock > 0) MaterialTheme.colorScheme.tertiary
                                        else MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                        )
                        Text(
                                text =
                                        if (producto.stock > 0)
                                                "Stock disponible: ${producto.stock} unidades"
                                        else "Sin stock",
                                style = MaterialTheme.typography.bodyMedium,
                                color =
                                        if (producto.stock > 0) MaterialTheme.colorScheme.onSurface
                                        else MaterialTheme.colorScheme.error
                        )
                    }

                    // Precio y botón
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                    text = "Precio",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                    text = formatoPrecio(producto.precio),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Button(
                                onClick = onComprar,
                                enabled = producto.stock > 0,
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                        ) {
                            Icon(
                                    Icons.Outlined.ShoppingCart,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Agregar", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingProductosCard() {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text("Cargando productos...", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun ErrorProductosCard(mensaje: String, onRetry: () -> Unit) {
    Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                    CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
            shape = RoundedCornerShape(16.dp)
    ) {
        Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                    text = "Error al cargar productos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                    text = mensaje,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                    onClick = onRetry,
                    colors =
                            ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                            )
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Reintentar")
            }
        }
    }
}

@Composable
private fun EmptyProductosCard() {
    Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                    CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
            shape = RoundedCornerShape(16.dp)
    ) {
        Column(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                    imageVector = Icons.Outlined.Inventory,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                    text = "No hay productos disponibles",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                    text = "Pronto agregaremos nuevos productos",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Función helper para formatear precio
private fun formatoPrecio(precio: Int): String {
    val formato = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    return formato.format(precio)
}
