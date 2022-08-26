package com.amrabdelhamiddiab.waiting.framework.utilis

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.amrabdelhamiddiab.waiting.presentation.loginflow.scanqrservice.ScanQrServiceViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class MyImageAnalyzerService(
    private val scanQrServiceViewModel: ScanQrServiceViewModel
) :
    ImageAnalysis.Analyzer {
    override fun analyze(imageProxy: ImageProxy) {
        scanBarcode(imageProxy)
    }


    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    private fun scanBarcode(imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
            val inputImage = InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)
            val scanner = BarcodeScanning.getClient()
            scanner.process(inputImage)
                .addOnCompleteListener {
                    imageProxy.close()
                    if (it.isSuccessful) {
                        readBarcodeData(it.result as List<Barcode>)
                    } else {
                        it.exception?.printStackTrace()
                    }
                }
        }
    }

    private fun readBarcodeData(barcodes: List<Barcode>) {
        for (barcode in barcodes) {
            when (barcode.valueType) {
                Barcode.TYPE_TEXT -> {
                    scanQrServiceViewModel.takeClientToken(barcode.displayValue.toString())
                }
            }
        }
    }
}