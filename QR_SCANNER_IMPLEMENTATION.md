# 📱 Implementación de Lector QR en CrimsonEyes

## 🎯 Resumen de la Implementación

Se ha implementado un **lector de códigos QR** completo en la aplicación CrimsonEyes utilizando **ML Kit Vision** y **CameraX**. El escáner está integrado en la pantalla principal de recetas (HomeScreen) y permite capturar valores QR que pueden utilizarse para llenar automáticamente campos en las recetas.

---

## 📂 Archivos Creados

### 1. **QRScannerViewModel.kt**
**Ubicación:** `app/src/main/java/com/example/crimsoneyes/controller/`

**Responsabilidades:**
- Maneja la lógica de la cámara usando CameraX
- Procesa imágenes en tiempo real con ML Kit Barcode Scanner
- Detecta códigos QR en formato QR_CODE
- Proporciona estados reactivos para la UI

**Clase Principal:**
```kotlin
class QRScannerViewModel : ViewModel()
```

**Funciones Clave:**
- `startCamera()` - Inicia la vista previa de la cámara
- `processImageProxy()` - Procesa cada frame de la cámara
- `stopCamera()` - Detiene la captura
- `resetScan()` - Reinicia el estado del escaneo

**Estados Reactivos:**
- `scanResult` - Contiene el valor QR capturado
- `cameraError` - Errores de la cámara
- `isScanning` - Indica si la cámara está activa

---

### 2. **QRScannerScreen.kt**
**Ubicación:** `app/src/main/java/com/example/crimsoneyes/view/`

**Responsabilidades:**
- Renderiza la UI del escáner QR
- Maneja permisos de cámara con Accompanist Permissions
- Muestra la vista previa de la cámara
- Proporciona retroalimentación visual durante el escaneo

**Características:**
- ✅ Solicita permiso de cámara automáticamente
- ✅ Muestra marco visual de alineación
- ✅ Mensajes de instrucción claros
- ✅ Manejo de errores elegante
- ✅ Tema oscuro/claro compatible

**Estados de la Pantalla:**
1. Sin permiso → Botón para solicitar permiso
2. Error de cámara → Mensaje de error y botón para volver
3. Cámara activa → Vista previa con marco indicador
4. Cargando → Spinner de carga

---

## 🔄 Archivos Modificados

### 1. **Screen.kt** (Navigation)
**Cambio:** Se agregó nueva ruta de navegación
```kotlin
data object QRScanner: Screen("qr_scanner")
```

---

### 2. **AppNavigation.kt**
**Cambios:**
- ✅ Import agregado: `import com.example.crimsoneyes.view.QRScannerScreen`
- ✅ Import agregado: `import com.example.crimsoneyes.view.ProfileScreen`
- ✅ Se agregó composable para QRScanner:
```kotlin
composable(Screen.QRScanner.route) {
    QRScannerScreen(
        onQRScanned = { qrValue ->
            navController.previousBackStackEntry?.savedStateHandle?.set("qr_value", qrValue)
            navController.popBackStack()
        },
        onBackClick = { navController.popBackStack() },
        isDarkMode = isDarkMode
    )
}
```

- ✅ Se agregó parámetro en HomeScreen:
```kotlin
onNavigateToQRScanner = {
    navController.navigate(Screen.QRScanner.route)
}
```

---

### 3. **RecetaView.kt** (HomeScreen)
**Cambios:**
- ✅ Parámetro agregado a función: `onNavigateToQRScanner: () -> Unit`
- ✅ Botón de cámara agregado en TopAppBar (antes del perfil):
```kotlin
FilledTonalIconButton(
    onClick = onNavigateToQRScanner,
    modifier = Modifier.size(40.dp)
) {
    Icon(
        painter = painterResource(id = android.R.drawable.ic_menu_camera),
        contentDescription = "Escanear QR",
        modifier = Modifier.size(20.dp)
    )
}
```

---

### 4. **build.gradle.kts** (Dependencias)
**Dependencias Agregadas:**
```kotlin
// Accompanist Permissions (manejo de permisos)
implementation("com.google.accompanist:accompanist-permissions:0.34.0")

// Guava para MoreExecutors
implementation("com.google.guava:guava:32.1.3-android")
```

**Nota:** Las dependencias de CameraX y ML Kit ya estaban presentes

---

## 📝 Permisos Requeridos

**AndroidManifest.xml** (ya estaban configurados):
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature
    android:name="android.hardware.camera"
    android:required="false" />
```

---

## 🚀 Uso en la App

### Flujo de Usuario:
1. Usuario abre la app y va a la pantalla principal (HomeScreen)
2. Usuario toca el botón de cámara (🎥) en la TopAppBar
3. Se navega a QRScannerScreen
4. Se solicita permiso de cámara (si no lo tiene)
5. Se muestra la vista previa de la cámara
6. Usuario alinea el código QR en el marco
7. Se detecta el QR automáticamente
8. El valor se pasa a la pantalla anterior mediante `SavedStateHandle`
9. Se retorna a HomeScreen con el valor QR

### Para Usar el Valor QR en HomeScreen:
```kotlin
val qrValue = navController.currentBackStackEntry?.savedStateHandle?.get<String>("qr_value")
qrValue?.let {
    // Usar el valor para llenar campos
    viewModel.onTitleChange(it)
}
```

---

## 🔐 Características de Seguridad

- ✅ Validación de permisos con Accompanist
- ✅ Manejo de excepciones en todas las operaciones de cámara
- ✅ Limpieza de recursos en `onCleared()`
- ✅ Executor service separado para procesamiento de imágenes
- ✅ Protección contra procesamiento múltiple simultáneo

---

## 🎨 Interfaz de Usuario

### TopAppBar Buttons (de izquierda a derecha):
1. 🎥 **QR Scanner** (NUEVO)
2. 👤 Perfil
3. 🛍️ Productos
4. 🌙 Tema
5. 🚪 Logout

### Pantalla QR Scanner:
- **Marco Visual:** Cuadrado de 280dp con borde primario
- **Instrucciones:** "Alinea el código QR" y "Acerca el código hacia la cámara"
- **Fondo:** Negro semi-transparente para mejor contraste
- **Estado de Carga:** Spinner circular con texto "Inicializando cámara..."

---

## 🐛 Solución de Problemas

### "Error: Permiso de cámara requerido"
- Toca el botón "Otorgar Permiso"
- Acepta el permiso en el diálogo del sistema

### "Error de cámara"
- Asegúrate de que el dispositivo tiene cámara
- Cierra otras apps que usen la cámara
- Reinicia la app

### QR no se detecta
- Asegúrate de que el código QR sea válido
- Acerca más el código a la cámara
- Alinea el código dentro del marco visual

---

## 📊 Dependencias Utilizadas

| Dependencia | Versión | Propósito |
|-------------|---------|----------|
| CameraX | 1.3.4 | Captura de imágenes |
| ML Kit Vision | 17.2.0 | Detección de QR |
| Accompanist | 0.34.0 | Manejo de permisos |
| Guava | 32.1.3 | Executors |

---

## 🔮 Posibles Extensiones Futuras

1. **Escaneo de Múltiples Formatos:** Agregar soporte para códigos de barras
2. **Historial de Escaneos:** Guardar QRs escaneados
3. **Vibración y Sonido:** Feedback háptico al detectar QR
4. **Flashlight:** Activar linterna automáticamente en poca luz
5. **OCR Integrado:** Combinar con reconocimiento de texto
6. **Búsqueda de Productos:** Usar QR para buscar productos en el catálogo

---

## ✅ Checklist de Implementación

- ✅ ViewModel creado con lógica de cámara
- ✅ Screen UI creado con PreviewView
- ✅ Navegación configurada
- ✅ TopAppBar button agregado
- ✅ Permisos manejados
- ✅ Dependencias agregadas
- ✅ AndroidManifest configurado
- ✅ Errores compilados resueltos
- ✅ Estados reactivos implementados
- ✅ UI responsive y compatible con temas

---

## 📞 Notas Importantes

- El scanner se ejecuta en un executor service separado para no bloquear la UI
- El procesamiento de imágenes está optimizado para mantener solo el último frame
- La limpieza de recursos es automática cuando el ViewModel se destruye
- El valor del QR se pasa de forma segura mediante SavedStateHandle


