package com.amrabdelhamiddiab.waiting.presentation.loginflow.scanorpickqrcode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class ScanOrPickQrCode : Fragment() {
    val viewModel by viewModels<ScanOrPickQrcodeViewModel>()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var scanner: BarcodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    val imageUri = data?.data
                    // here i have the uri and will create the input image
                    try {
                        val inputImage =
                            imageUri?.let { InputImage.fromFilePath(requireContext(), it) }!!
                        getQrCodeValue(inputImage)
                    } catch (e: IOException) {
                        Log.d(TAG, e.message.toString())
                    }
                }
            }
    }

    private fun getQrCodeValue(inputImage: InputImage) {
        scanner.process(inputImage).addOnSuccessListener { barcodes ->
            for (barcode in barcodes) {
                when (barcode.valueType) {
                    Barcode.TYPE_TEXT -> {
                        // Here i will continue to do the same as in scanQR CODE
                        viewModel.checkThisString(barcode.displayValue.toString())
                    }
                }
            }
        }
    }
}
