package com.example.inventario

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.navigation.NavHostController
import com.example.inventario.data.InventarioDatabase
import com.example.inventario.data.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ScannerScreen(
    navController: NavHostController,
    db: InventarioDatabase
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val onCodeScanned: (String) -> Unit = { result ->
        val producto = Producto(
            codigoQR = result,
            nombre = "Producto escaneado",
            cantidad = 1,
            tipoOperacion = "ingreso",
            fecha = System.currentTimeMillis()
        )

        CoroutineScope(Dispatchers.IO).launch {
            db.productoDao().insertarProducto(producto)
        }

        navController.navigate("qr_result/${result}")
    }

    if (!hasPermission) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Se requiere permiso de cámara para escanear QR.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                androidx.activity.ComponentActivity::class.java.cast(context)?.requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    100
                )
            }) {
                Text("Solicitar permiso")
            }
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx: Context ->
                val previewView = PreviewView(ctx)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val barcodeScanner = BarcodeScanning.getClient()
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                        val mediaImage = imageProxy.image
                        if (mediaImage != null) {
                            val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                            barcodeScanner.process(inputImage)
                                .addOnSuccessListener { barcodes ->
                                    for (barcode in barcodes) {
                                        barcode.rawValue?.let { onCodeScanned(it) }
                                    }
                                }
                                .addOnCompleteListener { imageProxy.close() }
                        } else {
                            imageProxy.close()
                        }
                    }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        // Marco con esquinas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .align(Alignment.Center)
                .padding(horizontal = 48.dp)
                .drawBehind {
                    val lineLength = 30.dp.toPx()
                    val strokeWidth = 4.dp.toPx()
                    val color = Color.White

                    drawLine(color, Offset(0f, 0f), Offset(lineLength, 0f), strokeWidth)
                    drawLine(color, Offset(0f, 0f), Offset(0f, lineLength), strokeWidth)
                    drawLine(color, Offset(size.width, 0f), Offset(size.width - lineLength, 0f), strokeWidth)
                    drawLine(color, Offset(size.width, 0f), Offset(size.width, lineLength), strokeWidth)
                    drawLine(color, Offset(0f, size.height), Offset(0f, size.height - lineLength), strokeWidth)
                    drawLine(color, Offset(0f, size.height), Offset(lineLength, size.height), strokeWidth)
                    drawLine(color, Offset(size.width, size.height), Offset(size.width - lineLength, size.height), strokeWidth)
                    drawLine(color, Offset(size.width, size.height), Offset(size.width, size.height - lineLength), strokeWidth)
                }
        )

        Text(
            text = "Coloca el código dentro del recuadro",
            modifier = Modifier.align(Alignment.Center).padding(top = 280.dp),
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}