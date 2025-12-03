# 🎯 Guía Rápida: Usar el QR Scanner

## Paso 1: Compilar la App
```bash
cd "C:\Users\CETECOM\Documents\App Moviles\eva03\CrimsonEyes\CrimsonEyes\CrimsonEyes"
gradlew.bat build
```

## Paso 2: Ejecutar en Emulador o Dispositivo
- Abre Android Studio
- Ejecuta la app en un emulador o dispositivo físico

## Paso 3: Usar el QR Scanner

### Acceso al Scanner:
1. Inicia sesión en la app
2. Ve a la pantalla principal (RecetasView)
3. Toca el botón de **cámara 🎥** en la parte superior izquierda de la TopAppBar

### Escanear un QR:
1. Se abrirá la pantalla de escáner
2. Si es la primera vez, otorga permiso de cámara
3. Apunta la cámara hacia el código QR
4. Alinea el código dentro del marco visual
5. El escáner detectará automáticamente el QR
6. Volverá a la pantalla anterior con el valor capturado

---

## 📍 Ubicación del Botón QR

```
┌─────────────────────────────────────────────┐
│  🎥  👤   🛍️   🌙   🚪                      │  ← TopAppBar
├─────────────────────────────────────────────┤
│                                             │
│           HomeScreen / RecetasView          │
│                                             │
│  • Nueva Receta                             │
│  • Total de Recetas: X                      │
│  • Lista de mis Recetas                     │
│                                             │
└─────────────────────────────────────────────┘

🎥 = QR Scanner (NUEVO)
```

---

## 🔧 Integración con Campos de Receta

Para llenar automáticamente los campos con el valor del QR:

```kotlin
// En HomeScreen, después de navegar desde QRScannerScreen
val qrValue = navController.currentBackStackEntry?.savedStateHandle?.get<String>("qr_value")

LaunchedEffect(qrValue) {
    qrValue?.let {
        // Opción 1: Llenar título
        viewModel.onTitleChange(it)
        
        // Opción 2: Llenar descripción
        viewModel.onBodyChange(it)
        
        // Opción 3: Combinar con otros datos
        val recetaCompleta = "QR: $it"
        viewModel.onTitleChange(recetaCompleta)
    }
}
```

---

## ✨ Características Implementadas

✅ Detección automática de QR  
✅ Manejo de permisos  
✅ UI responsiva  
✅ Tema oscuro/claro compatible  
✅ Mensajes de error claros  
✅ Frame visual de alineación  
✅ Integración con navegación Compose  
✅ Soporte para múltiples dispositivos  

---

## 🚨 Requisitos

- **Dispositivo:** Con cámara trasera
- **Android:** Mínimo API 26 (Android 8.0)
- **Permisos:** Cámara (solicitado en la app)
- **Dependencias:** Automaticamente incluidas en build.gradle.kts

---

## 💡 Casos de Uso en CrimsonEyes

### 1. **Recetas Médicas Codificadas**
Hospitales/clínicas pueden generar recetas con QR que contengan datos como:
- Código de receta
- ID del paciente
- Prescripción de lentes

### 2. **Productos con QR**
Gafas pueden tener QR en el empaque con:
- Código de lote
- Modelo
- Especificaciones

### 3. **Validación de Autenticidad**
Verificar que los productos sean originales escaneando el QR del fabricante

---

## 📱 Prueba Rápida

Puedes generar un QR de prueba en: https://www.qr-code-generator.com/

Ejemplo de valores que puedes escanear:
- "OD: +1.00 -0.50 x 180"
- "RECETA-12345"
- "https://example.com/receta"
- Cualquier texto simple

---

## 🔍 Verificar que Funciona

1. Ejecuta la app
2. Inicia sesión
3. Toca 🎥 en la TopAppBar
4. Debería ver:
   - ✅ Solicitud de permiso (primera vez)
   - ✅ Pantalla negra con marco visual
   - ✅ Texto "Alinea el código QR"
   - ✅ Indicaciones "Acerca el código hacia la cámara"

---

## 🐛 Si Hay Problemas

### Problema: "Unresolved reference"
**Solución:** 
- Sincroniza el proyecto: `File → Sync Now`
- Reconstruye: `Build → Clean Project` luego `Build → Rebuild Project`

### Problema: Permiso denegado
**Solución:**
- En Configuración del Dispositivo → Permisos → Otorga acceso a cámara

### Problema: La cámara no muestra nada
**Solución:**
- Cierra otras apps que usen la cámara
- Reinicia el emulador/dispositivo
- Comprueba que el dispositivo tiene cámara

---

## 📚 Archivos Relacionados

| Archivo | Ruta | Descripción |
|---------|------|-------------|
| QRScannerViewModel.kt | controller/ | Lógica del scanner |
| QRScannerScreen.kt | view/ | UI del scanner |
| Screen.kt | navigation/ | Ruta de navegación |
| AppNavigation.kt | navigation/ | Configuración de nav |
| RecetaView.kt | view/ | HomeScreen con botón |
| QR_SCANNER_IMPLEMENTATION.md | root/ | Documentación completa |

---

## 🎓 Próximos Pasos

1. **Integración Backend:** Guardar los valores escaneados en la base de datos
2. **Validación:** Verificar que el QR sea válido antes de procesar
3. **Historial:** Mostrar historial de QR escaneados
4. **Búsqueda:** Usar el QR para buscar productos/recetas

¡Happy Scanning! 📱✨

