# 🔗 Ejemplos de Integración del QR Scanner

## Ejemplo 1: Llenar Automáticamente la Receta

```kotlin
// En RecetaView.kt - HomeScreen

// Observar cambios del QR escaneado
LaunchedEffect(Unit) {
    val qrValue = navController.currentBackStackEntry?.savedStateHandle?.get<String>("qr_value")
    qrValue?.let { value ->
        // Llenar automáticamente el título con el valor del QR
        viewModel.onTitleChange(value)
        
        // Limpiar el savedStateHandle
        navController.currentBackStackEntry?.savedStateHandle?.remove<String>("qr_value")
    }
}
```

---

## Ejemplo 2: Parsear Datos de un QR Estructurado

```kotlin
// Suponiendo que el QR contiene datos separados por |
// Ejemplo: "OD:+1.00|-0.50|EJE:180|DIST:62"

fun parseRecetaQR(qrValue: String): RecetaData {
    val parts = qrValue.split("|")
    return RecetaData(
        esferaOD = parts.getOrNull(0)?.removePrefix("OD:") ?: "",
        cilindroOD = parts.getOrNull(1) ?: "",
        ejeOD = parts.getOrNull(2)?.removePrefix("EJE:") ?: "",
        distancia = parts.getOrNull(3)?.removePrefix("DIST:") ?: ""
    )
}

// Usar en HomeScreen
LaunchedEffect(Unit) {
    val qrValue = navController.currentBackStackEntry?.savedStateHandle?.get<String>("qr_value")
    qrValue?.let { value ->
        val receta = parseRecetaQR(value)
        viewModel.onTitleChange("Receta: ${receta.esferaOD} ${receta.cilindroOD}")
        viewModel.onBodyChange("EJE: ${receta.ejeOD}\nDistancia: ${receta.distancia}")
    }
}

data class RecetaData(
    val esferaOD: String,
    val cilindroOD: String,
    val ejeOD: String,
    val distancia: String
)
```

---

## Ejemplo 3: Validar QR Antes de Procesar

```kotlin
// En RecetaView.kt

fun isValidRecetaQR(qrValue: String): Boolean {
    // Validaciones básicas
    return qrValue.isNotEmpty() && 
           qrValue.length > 5 &&
           (qrValue.contains("OD") || qrValue.contains("RECETA") || qrValue.startsWith("REC"))
}

// Usar con confirmación
LaunchedEffect(Unit) {
    val qrValue = navController.currentBackStackEntry?.savedStateHandle?.get<String>("qr_value")
    qrValue?.let { value ->
        if (isValidRecetaQR(value)) {
            viewModel.onTitleChange(value)
            // Mostrar snackbar de éxito
            Log.d("QRValidation", "QR válido: $value")
        } else {
            // Mostrar error
            Log.e("QRValidation", "QR inválido: $value")
            // Puedes mostrar un Snackbar o Dialog
        }
    }
}
```

---

## Ejemplo 4: Guardar Historial de QRs Escaneados

```kotlin
// En tu ViewModel
class RecetaViewModel(
    private val recetaRepository: RecetaRepository,
    private val email: String
) : ViewModel() {
    
    private val _qrHistory = mutableStateOf<List<String>>(emptyList())
    val qrHistory: State<List<String>> = _qrHistory
    
    fun addToQRHistory(qrValue: String) {
        val currentHistory = _qrHistory.value.toMutableList()
        currentHistory.add(0, qrValue) // Agregar al inicio
        if (currentHistory.size > 50) {
            currentHistory.removeAt(currentHistory.size - 1) // Limitar a 50 items
        }
        _qrHistory.value = currentHistory
        
        // Opcionalmente guardar en SharedPreferences o Base de Datos
        saveQRToPreferences(qrValue)
    }
    
    private fun saveQRToPreferences(qrValue: String) {
        // Implementar guardado en SharedPreferences
        // context.getSharedPreferences("qr_history", Context.MODE_PRIVATE)
        //     .edit()
        //     .putString("last_qr", qrValue)
        //     .apply()
    }
}

// En HomeScreen
LaunchedEffect(Unit) {
    val qrValue = navController.currentBackStackEntry?.savedStateHandle?.get<String>("qr_value")
    qrValue?.let { value ->
        viewModel.addToQRHistory(value)
        viewModel.onTitleChange(value)
    }
}
```

---

## Ejemplo 5: Buscar Productos por QR

```kotlin
// En ProductoViewModel
class ProductoViewModel : ViewModel() {
    
    suspend fun searchProductByQR(qrCode: String): Producto? {
        return try {
            ApiRepository.productosApi.searchByQR(qrCode)
        } catch (e: Exception) {
            null
        }
    }
}

// En HomeScreen
LaunchedEffect(Unit) {
    val qrValue = navController.currentBackStackEntry?.savedStateHandle?.get<String>("qr_value")
    qrValue?.let { value ->
        viewModelScope.launch {
            val producto = productoViewModel.searchProductByQR(value)
            if (producto != null) {
                // Mostrar producto encontrado
                navController.navigate(Screen.Producto.route)
            } else {
                // Producto no encontrado
                Log.w("ProductSearch", "Producto no encontrado para QR: $value")
            }
        }
    }
}
```

---

## Ejemplo 6: Botón de Compra Rápida (Desde QR)

```kotlin
// En HomeScreen - agregar UI para acción rápida
// Cuando se detecta un QR de producto

@Composable
fun QuickBuyBottomSheet(
    qrValue: String,
    producto: Producto?,
    onAddToCart: (Producto) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {
        if (producto != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Producto encontrado",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Text(
                    "QR: $qrValue",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Detalles del producto
                Text(
                    producto.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                
                Text(
                    "Precio: ${producto.precio}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Button(
                    onClick = {
                        onAddToCart(producto)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar al Carrito")
                }
                
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}

// Usar en HomeScreen
var showQuickBuySheet by remember { mutableStateOf(false) }
var scannedProduct by remember { mutableStateOf<Producto?>(null) }
var scannedQR by remember { mutableStateOf("") }

// En LaunchedEffect del QR
LaunchedEffect(Unit) {
    val qrValue = navController.currentBackStackEntry?.savedStateHandle?.get<String>("qr_value")
    qrValue?.let { value ->
        scannedQR = value
        val product = productoViewModel.searchProductByQR(value)
        if (product != null) {
            scannedProduct = product
            showQuickBuySheet = true
        }
    }
}

// Renderizar el BottomSheet
if (showQuickBuySheet && scannedProduct != null) {
    QuickBuyBottomSheet(
        qrValue = scannedQR,
        producto = scannedProduct,
        onAddToCart = { product ->
            carritoViewModel.addItem(product)
        },
        onDismiss = { showQuickBuySheet = false }
    )
}
```

---

## Ejemplo 7: Escaneo Múltiple (Recetas o Productos)

```kotlin
// Clase para guardar QRs multiples
data class QRBatch(
    val timestamp: Long = System.currentTimeMillis(),
    val qrValues: List<String> = emptyList()
)

// En ViewModel
var qrBatch by mutableStateOf(QRBatch())
    private set

fun addToQRBatch(qrValue: String) {
    val updated = qrBatch.copy(
        qrValues = qrBatch.qrValues + qrValue
    )
    qrBatch = updated
}

fun processQRBatch() {
    // Procesar todos los QR escaneados
    qrBatch.qrValues.forEach { qrValue ->
        crearReceta(qrValue)
    }
    qrBatch = QRBatch() // Limpiar
}

// En HomeScreen
Button(
    onClick = {
        viewModel.processQRBatch()
        // Mostrar éxito
    },
    enabled = viewModel.qrBatch.qrValues.isNotEmpty()
) {
    Text("Procesar ${viewModel.qrBatch.qrValues.size} Recetas")
}
```

---

## Ejemplo 8: Manejo de Errores Mejorado

```kotlin
// Sealed class para resultados del QR
sealed class QRResult {
    data class Success(val value: String) : QRResult()
    data class Error(val message: String) : QRResult()
    object Cancelled : QRResult()
}

// En ViewModel
private val _qrResult = mutableStateOf<QRResult?>(null)
val qrResult: State<QRResult?> = _qrResult

fun handleQRResult(result: QRResult) {
    when (result) {
        is QRResult.Success -> {
            // Procesar valor
            addToQRHistory(result.value)
            viewModel.onTitleChange(result.value)
        }
        is QRResult.Error -> {
            Log.e("QR", "Error: ${result.message}")
            // Mostrar error al usuario
        }
        is QRResult.Cancelled -> {
            Log.d("QR", "Escaneo cancelado")
        }
    }
    _qrResult.value = null // Limpiar
}

// En HomeScreen
LaunchedEffect(Unit) {
    val qrValue = navController.currentBackStackEntry?.savedStateHandle?.get<String>("qr_value")
    qrValue?.let { 
        viewModel.handleQRResult(QRResult.Success(it))
    }
}
```

---

## 🔗 Integración Recomendada

Para la aplicación de óptica CrimsonEyes, recomiendo:

1. **Recetas Médicas:** Usar Ejemplo 2 (parsear datos estructurados)
2. **Búsqueda de Productos:** Usar Ejemplo 5 (buscar por QR)
3. **Compra Rápida:** Usar Ejemplo 6 (agregar directo al carrito)
4. **Historial:** Usar Ejemplo 4 (guardar QRs escaneados)

---

## 📋 Checklist de Integración

- [ ] Copiar el código de ejemplo apropiado
- [ ] Ajustarlo a tu estructura de datos
- [ ] Probar en emulador
- [ ] Probar en dispositivo real
- [ ] Manejar casos edge (QR vacío, inválido, duplicado)
- [ ] Agregar validaciones específicas del negocio
- [ ] Documentar el formato de QR esperado
- [ ] Entrenar a usuarios sobre cómo usar

¡Listo para integrar! 🚀

