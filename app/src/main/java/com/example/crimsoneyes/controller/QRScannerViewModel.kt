package com.example.crimsoneyes.controller

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
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

    private val _isScanning = mutableStateOf(true)
    val isScanning: State<Boolean> = _isScanning

    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
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
        previewView: androidx.camera.view.PreviewView
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()

                // Ejecutar en el hilo principal para acceder a PreviewView
                Handler(Looper.getMainLooper()).post {
                    try {
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(cameraExecutor) { image ->
                                    processImageProxy(image)
                                }
                            }

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            cameraProvider?.unbindAll()
                            camera = cameraProvider?.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalysis
                            )
                            _isScanning.value = true
                        } catch (exc: Exception) {
                            Log.e("QRScanner", "Error binding camera", exc)
                            _cameraError.value = "Error al iniciar cámara: ${exc.message}"
                        }
                    } catch (exc: Exception) {
                        Log.e("QRScanner", "Error setting surface provider", exc)
                        _cameraError.value = "Error al configurar preview: ${exc.message}"
                    }
                }
            } catch (exc: Exception) {
                Log.e("QRScanner", "Error getting camera provider", exc)
                _cameraError.value = "Error al obtener provider: ${exc.message}"
            }
        }, cameraExecutor)
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(image: androidx.camera.core.ImageProxy) {
        if (isProcessing) {
            image.close()
            return
        }

        try {
            isProcessing = true
            val mediaImage = image.image ?: return

            val inputImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)

            barcodeScanner.process(inputImage)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        val barcode = barcodes[0]
                        val qrValue = barcode.rawValue ?: ""
                        if (qrValue.isNotEmpty()) {
                            Log.d("QRScanner", "QR detectado: $qrValue")
                            _scanResult.value = QRScanResult(
                                value = qrValue,
                                isSuccess = true
                            )
                            stopCamera()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("QRScanner", "Error scanning barcode", e)
                }
                .addOnCompleteListener {
                    isProcessing = false
                    image.close()
                }
        } catch (e: Exception) {
            Log.e("QRScanner", "Error processing image", e)
            isProcessing = false
            image.close()
        }
    }

    fun stopCamera() {
        cameraProvider?.unbindAll()
        _isScanning.value = false
    }

    fun resetScan() {
        _scanResult.value = QRScanResult()
        _cameraError.value = null
    }

    override fun onCleared() {
        super.onCleared()
        cameraExecutor.shutdown()
        stopCamera()
    }
}

