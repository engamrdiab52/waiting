package com.amrabdelhamiddiab.waiting.framework.utilis

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.navigation.NavController
import com.amrabdelhamiddiab.waiting.presentation.loginflow.scanqr.ScanQrViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class MyImageAnalyzer(
    private val context: Context,
    private val scanQrViewModel: ScanQrViewModel,
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
                    Toast.makeText(context, barcode.displayValue, Toast.LENGTH_SHORT).show()
                    //   barcode.displayValue?.let { Log.d(TAG, it) }
                    //   navController.navigate(nav)
                    /*     preview.visibility = View.GONE
                         linearLayout.visibility = View.VISIBLE*/
                    // textView.text = barcode.displayValue
                    scanQrViewModel.saveUserIdInPreferences(barcode.displayValue.toString())
                    //here i want to navigate
                    // navController.navigate(R.id.clientFragment)
                    scanQrViewModel.navigateToClientFragment()
                    //HERE I WANT TO SAVE IT IN PREFRENCES
                    // val userId = order?.order
                    //  order?.order = barcode.displayValue!!.toLong()

                }
            }
        }
    }
}

