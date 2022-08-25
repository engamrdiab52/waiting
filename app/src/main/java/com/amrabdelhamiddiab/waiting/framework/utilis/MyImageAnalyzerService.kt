package com.amrabdelhamiddiab.waiting.framework.utilis

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.navigation.NavController
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.presentation.loginflow.scanqr.ScanQrViewModel
import com.amrabdelhamiddiab.waiting.presentation.loginflow.scanqrservice.ScanQrServiceViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class MyImageAnalyzerService(
    private val context: Context,
    private val scanQrServiceViewModel: ScanQrServiceViewModel,
    private val navController: NavController
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
                    //first time to get the token for client, but i need to check if it right
                    //  scanQrViewModel.checkThisString(barcode.displayValue.toString())
                    Log.d(TAG,"Barcode.TYPE_TEXT:::::::::"+ barcode.displayValue.toString())
                    scanQrServiceViewModel.takeClientToken(barcode.displayValue.toString())
                }
            }
        }
    }

}