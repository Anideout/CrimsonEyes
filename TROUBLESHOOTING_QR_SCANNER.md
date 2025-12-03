# 🔧 Troubleshooting y FAQ - QR Scanner

## Preguntas Frecuentes

### ¿Dónde está el botón del QR Scanner?
📍 **Respuesta:** En la TopAppBar (barra superior) de la pantalla HomeScreen, a la izquierda de todos los botones. Busca el ícono 🎥 (cámara).

```
┌─────────────────────────────────────────┐
│ 🎥 👤 🛍️ 🌙 🚪    ← El botón 🎥 es aquí
└─────────────────────────────────────────┘
```

---

### ¿Qué versión mínima de Android necesita?
📍 **Respuesta:** Android 8.0 (API 26) o superior.

Para verificar en tu dispositivo:
- Abre Configuración
- Ve a "Acerca de"
- Busca "Versión de Android"

---

### ¿Funciona sin conexión a internet?
📍 **Respuesta:** **SÍ**, el escaneo de QR funciona completamente offline. No necesita conexión.

---

## Problemas Comunes y Soluciones

### Problema #1: No veo el botón 🎥

**Síntoma:** 
- La TopAppBar no muestra el ícono de cámara

**Causas Posibles:**
1. Cambios no guardados
2. Caché del IDE no actualizada
3. Archivo no compilado correctamente

**Soluciones:**
```bash
# Opción 1: Limpiar caché
File → Invalidate Caches... → Invalidate and Restart

# Opción 2: Sincronizar
File → Sync Now

# Opción 3: Reconstruir
Build → Clean Project
Build → Rebuild Project

# Opción 4: Reinstalar
Run → Run (reinstala la app)
```

---

### Problema #2: Permiso de Cámara Denegado

**Síntoma:**
- Mensaje: "Permiso de Cámara Requerido"
- Botón: "Otorgar Permiso" no funciona

**Soluciones:**

**Para Emulador:**
1. Abre la app
2. Ve a Configuración del Emulador
3. Apps → Permisos → Cámara
4. Marca como "Permitir"
5. Reinicia la app

**Para Dispositivo Real:**
1. Abre Configuración
2. Apps y Notificaciones
3. Busca "CrimsonEyes"
4. Permisos → Cámara
5. Marca como "Permitir solo durante esta sesión" o "Permitir siempre"

---

### Problema #3: Pantalla Negra, No se Ve la Cámara

**Síntoma:**
- Se abre QRScannerScreen
- Pero la pantalla está completamente negra
- El texto de instrucciones sí se ve

**Causas:**
1. La cámara está siendo usada por otra app
2. Problema de drivers de cámara
3. CameraX no inicializó correctamente

**Soluciones:**

```bash
# Solución 1: Cerrar otras apps que usen cámara
# Cierra: Cámara, Zoom, Google Meet, etc.

# Solución 2: Reiniciar el emulador
# En Android Studio: AVD Manager → Wipe Data → Start

# Solución 3: Reiniciar dispositivo físico
# Apaga y enciende

# Solución 4: Verificar que el dispositivo tiene cámara
# Settings → About → Hardware
```

---

### Problema #4: No Detecta el QR

**Síntoma:**
- La cámara se abre correctamente
- Acercas el QR pero no lo detecta
- Esperas mucho tiempo sin resultado

**Causas:**
1. QR es muy pequeño (< 2cm)
2. QR está borroso o dañado
3. Luz insuficiente
4. Ángulo muy inclinado

**Soluciones:**

```
✓ Acerca más el QR
✓ Asegúrate que el QR esté en el centro del marco
✓ Alinea el QR horizontalmente (no de lado)
✓ Mejora la iluminación
✓ Limpia la lente de la cámara
✓ Intenta con otro código QR
```

**Prueba con un QR de Prueba:**
1. Ve a: https://www.qr-code-generator.com/
2. Crea un QR simple (Ej: "PRUEBA123")
3. Descarga e imprime
4. Escanea con CrimsonEyes

---

### Problema #5: Error: "Unresolved Reference"

**Síntoma:**
```
Error: Unresolved reference 'QRScannerViewModel'
Error: Unresolved reference 'QRScannerScreen'
```

**Causas:**
1. Archivos no creados correctamente
2. Imports no sincronizados
3. Caché IDE

**Soluciones:**

```bash
# Paso 1: Verificar archivos existen
# ✓ app/src/main/java/com/example/crimsoneyes/controller/QRScannerViewModel.kt
# ✓ app/src/main/java/com/example/crimsoneyes/view/QRScannerScreen.kt

# Paso 2: Sincronizar
File → Sync Now

# Paso 3: Limpiar caché
File → Invalidate Caches... → Invalidate and Restart

# Paso 4: Reconstruir
Build → Rebuild Project
```

---

### Problema #6: Compilación Fallida

**Síntoma:**
```
Error: Compilation failed
Build failed with exception
```

**Soluciones Comunes:**

```bash
# Solución 1: Dependencias
# Asegurar que build.gradle.kts tiene:
# - Accompanist Permissions
# - Guava
# - CameraX
# - ML Kit Vision

# Solución 2: Gradle Sync
File → Sync Now
Wait 30 segundos para completar

# Solución 3: Limpiar build
# En terminal:
gradlew clean

# Solución 4: Actualizar Gradle
# En build.gradle.kts:
# classpath "com.android.tools.build:gradle:8.0.0"
```

---

### Problema #7: App se Congela

**Síntoma:**
- La app no responde al tocar botones
- "App Not Responding" (ANR)

**Causa:**
- Procesamiento en el thread principal

**Solución:**
- Verificar que `cameraExecutor` se está usando correctamente
- El código ya está optimizado, pero si aún ocurre:

```kotlin
// En QRScannerViewModel - verificar que esto existe:
private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

// Y que se usa en setAnalyzer:
it.setAnalyzer(cameraExecutor) { image ->
    processImageProxy(image)
}
```

---

### Problema #8: Batería se Agota Rápido

**Síntoma:**
- La batería disminuye rápidamente mientras se usa el scanner

**Causa:**
- Cámara y ML Kit requieren bastante poder

**Soluciones:**
1. No dejes el scanner abierto sin usarlo
2. Ciérralo cuando termines de escanear
3. Reduce el brillo de la pantalla
4. En dispositivos antiguos, cierra otras apps

---

### Problema #9: ML Kit No Funciona

**Síntoma:**
```
Error: Failed to download ML Kit model
```

**Causa:**
- ML Kit necesita descargar modelos en primera ejecución

**Soluciones:**
1. Conecta a internet (WiFi recomendado)
2. Abre la app por primera vez
3. Espera a que descargue los modelos (2-5 minutos)
4. Desconecta cuando termine
5. Ahora funciona sin internet

---

### Problema #10: QR se Escanea Múltiples Veces

**Síntoma:**
- El mismo QR se detecta varias veces seguidas
- Se regresa a HomeScreen, pero el valor aparece múltiples veces

**Causa:**
- `isProcessing` flag no se resetea correctamente

**Solución:**
- El código ya incluye protección, pero verifica que en `QRScannerViewModel`:

```kotlin
private var isProcessing = false

// Debe estar en processImageProxy():
if (isProcessing) {
    image.close()
    return
}
isProcessing = true
// ... código ...
isProcessing = false
```

---

## Logs y Debugging

### Ver Logs de Debug

En Android Studio, abre la ventana **Logcat**:
```
View → Tool Windows → Logcat
```

Filtra por:
```
Filter: "QRScanner"
```

Verás mensajes como:
```
D/QRScanner: QR detectado: VALOR_DEL_QR
D/QRScanner: Cámara iniciada
E/QRScanner: Error binding camera
```

---

### Agregar Logs Personalizados

En `QRScannerViewModel.kt`, agregar después de detectar QR:

```kotlin
Log.d("QRScanner", "QR valor completo: ${barcode.rawValue}")
Log.d("QRScanner", "QR tipo: ${barcode.valueType}")
Log.d("QRScanner", "QR formato: ${barcode.format}")
```

---

## Verificación Final

Crea este checklist antes de reportar problemas:

- [ ] ¿Sincronizaste el proyecto? (File → Sync Now)
- [ ] ¿Reconstruiste? (Build → Rebuild Project)
- [ ] ¿Reinstalaste la app? (Run → Run 'app')
- [ ] ¿Otorgaste permiso de cámara?
- [ ] ¿La cámara funciona en otras apps?
- [ ] ¿Tienes código QR válido?
- [ ] ¿El dispositivo tiene Android 8.0+?
- [ ] ¿La batería está por encima del 20%?
- [ ] ¿Revisaste Logcat para errores?

---

## Contacto y Escalada

Si ninguna solución funciona:

1. **Revisa Logcat** completo
2. **Captura pantalla** del error
3. **Anota el mensaje exacto** del error
4. **Verifica el archivo** en disco existe
5. **Prueba en emulador** si es dispositivo, o viceversa

---

## Recursos Útiles

- **CameraX Docs:** https://developer.android.com/training/camerax
- **ML Kit QR:** https://developers.google.com/ml-kit/vision/barcode-scanning
- **Compose Docs:** https://developer.android.com/jetpack/compose
- **Accompanist:** https://google.github.io/accompanist/

---

## Última Solución (Nuclear Option)

Si todo falla:

```bash
# 1. Limpia TODO
File → Invalidate Caches... → Invalidate and Restart

# 2. Espera que reinicie (5 minutos)

# 3. Sincroniza todo
File → Sync Now

# 4. Limpia y reconstruye
gradlew clean build

# 5. Ejecuta
Run → Run 'app'
```

---

✨ **Con estos pasos deberías resolver casi cualquier problema.** 

Si aún tienes dudas, consulta los archivos de documentación:
- `QR_SCANNER_IMPLEMENTATION.md`
- `GUIA_RAPIDA_QR_SCANNER.md`
- `EJEMPLOS_INTEGRACION_QR.md`

¡Buena suerte! 🎯📱

