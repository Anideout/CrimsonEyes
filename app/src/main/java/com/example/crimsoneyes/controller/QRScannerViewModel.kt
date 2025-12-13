package com.example.crimsoneyes.controller

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

data class QRScanResult(
    val value: String = "",
    val isSuccess: Boolean = false
)

class QRScannerViewModel : ViewModel() {
    private val _scanResult = mutableStateOf(QRScanResult())
    val scanResult: State<QRScanResult> = _scanResult

    private val _cameraError = mutableStateOf<String?>(null)
    val cameraError: State<String?> = _cameraError

    private val _isScanning = mutableStateOf(false)
    val isScanning: State<Boolean> = _isScanning

    private val _cameraReady = mutableStateOf(false)
    val cameraReady: State<Boolean> = _cameraReady

    private val _isInitializing = mutableStateOf(true)
    val isInitializing: State<Boolean> = _isInitializing

    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var cameraStarted = false
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private val barcodeScanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    )
    private var isProcessing = false

    fun startCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView
    ) {
        if (cameraStarted) {
            Log.d("QRScanner", "Cámara ya está inicializada ")
            return
        }

        cameraStarted = true
        _isInitializing.value = true
        _cameraError.value = null

        Log.d("QRScanner", "INICIANDO CÁMARA...")
        Log.d("QRScanner", "Context: ${context.javaClass.simpleName}")
        Log.d("QRScanner", "LifecycleOwner: ${lifecycleOwner.javaClass.simpleName}")

        // Timeout de seguridad
        viewModelScope.launch {
            delay(10000) // 10 segundos
            if (_isInitializing.value && !_cameraReady.value) {
                Log.e("QRScanner", "La cámara no se inicializó en 10 segundos")
                _cameraError.value = "La cámara tardó demasiado en iniciar..."
                _isInitializing.value = false
                cameraStarted = false
            }
        }

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        Log.d("QRScanner", "CameraProviderFuture creado")

        cameraProviderFuture.addListener({
            Log.d("QRScanner", "Listener ejecutándose...")

            try {
                Log.d("QRScanner", "Obteniendo CameraProvider...")
                cameraProvider = cameraProviderFuture.get()
                Log.d("QRScanner", "CameraProvider obtenido exitosamente")

                // Verificar si hay cámaras disponibles
                val cameraInfo = cameraProvider?.availableCameraInfos
                Log.d("QRScanner", "Cámaras disponibles: ${cameraInfo?.size ?: 0}")

                // Configurar el preview
                Log.d("QRScanner", "Configurando Preview...")
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                Log.d("QRScanner", "Preview configurado")

                // Configurar el análisis de imagen
                Log.d("QRScanner", "Configurando ImageAnalysis...")
                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor) { imageProxy ->
                            processImageProxy(imageProxy)
                        }
                    }
                Log.d("QRScanner", "ImageAnalysis configurado")

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                Log.d("QRScanner", "CameraSelector configurado (Cámara trasera)")

                try {
                    // Desenlazar casos de uso anteriores
                    Log.d("QRScanner", "Desenlazando casos de uso anteriores...")
                    cameraProvider?.unbindAll()
                    Log.d("QRScanner", "Casos de uso desenlazados")

                    // Enlazar casos de uso a la cámara
                    Log.d("QRScanner", "Enlazando cámara al lifecycle...")
                    camera = cameraProvider?.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )

                    Log.d("QRScanner", "¡CÁMARA ENLAZADA EXITOSAMENTE!")
                    Log.d("QRScanner", "Camera info: ${camera?.cameraInfo}")

                    _cameraReady.value = true
                    _isInitializing.value = false
                    _isScanning.value = true

                    Log.d("QRScanner", "CÁMARA COMPLETAMENTE LISTA")

                } catch (exc: Exception) {
                    Log.e("QRScanner", "Error al enlazar cámara", exc)
                    Log.e("QRScanner", "Tipo de error: ${exc.javaClass.simpleName}")
                    Log.e("QRScanner", "Mensaje: ${exc.message}")
                    Log.e("QRScanner", "Stack trace completo:")
                    exc.printStackTrace()

                    _cameraError.value = "Error al iniciar: ${exc.message}"
                    _isScanning.value = false
                    _cameraReady.value = false
                    _isInitializing.value = false
                    cameraStarted = false
                }

            } catch (exc: Exception) {
                Log.e("QRScanner", "Error al obtener CameraProvider", exc)
                Log.e("QRScanner", "Tipo de error: ${exc.javaClass.simpleName}")
                Log.e("QRScanner", "Mensaje: ${exc.message}")
                Log.e("QRScanner", "Stack trace completo:")
                exc.printStackTrace()

                _cameraError.value = "No se pudo acceder a la cámara: ${exc.message}"
                _isScanning.value = false
                _cameraReady.value = false
                _isInitializing.value = false
                cameraStarted = false
            }
        }, ContextCompat.getMainExecutor(context))

        Log.d("QRScanner", "Listener registrado, esperando ejecución...")
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: androidx.camera.core.ImageProxy) {
        // Si ya estamos procesando o ya se escaneó un QR, saltar
        if (isProcessing || _scanResult.value.isSuccess) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        try {
            isProcessing = true

            val inputImage = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty() && !_scanResult.value.isSuccess) {
                        val barcode = barcodes[0]
                        val qrValue = barcode.rawValue

                        if (!qrValue.isNullOrEmpty()) {
                            Log.d("QRScanner", "QR DETECTADO: $qrValue")
                            _scanResult.value = QRScanResult(
                                value = qrValue,
                                isSuccess = true
                            )
                            // Detener el escaneo
                            _isScanning.value = false
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("QRScanner", "Error al escanear código: ${e.message}", e)
                }
                .addOnCompleteListener {
                    isProcessing = false
                    imageProxy.close()
                }

        } catch (e: Exception) {
            Log.e("QRScanner", "Error al procesar imagen", e)
            isProcessing = false
            imageProxy.close()
        }
    }

    fun stopCamera() {
        Log.d("QRScanner", "Deteniendo cámara")
        try {
            cameraProvider?.unbindAll()
            _isScanning.value = false
            _cameraReady.value = false
        } catch (e: Exception) {
            Log.e("QRScanner", "Error al detener cámara", e)
        }
    }

    fun resetScan() {
        Log.d("QRScanner", "Reseteando scanner")
        _scanResult.value = QRScanResult()
        _cameraError.value = null
        _isScanning.value = false
        _cameraReady.value = false
        _isInitializing.value = false
        cameraStarted = false
        isProcessing = false
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("QRScanner", "ViewModel.onCleared() llamado - ESTO NO DEBERÍA PASAR TAN RÁPIDO")
        Log.d("QRScanner", "Stack trace de onCleared:")
        Thread.currentThread().stackTrace.forEach {
            Log.d("QRScanner", "  at $it")
        }
        stopCamera()
        cameraExecutor.shutdown()
        barcodeScanner.close()
    }
}