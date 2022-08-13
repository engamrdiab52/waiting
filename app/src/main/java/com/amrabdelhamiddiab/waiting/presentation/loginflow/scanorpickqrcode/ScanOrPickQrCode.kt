package com.amrabdelhamiddiab.waiting.presentation.loginflow.scanorpickqrcode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amrabdelhamiddiab.core.domain.Token
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.MyFirebaseMessagingService
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentScanOrPickQrCodeBinding
import com.amrabdelhamiddiab.waiting.databinding.FragmentScanQrCodeBinding
import com.amrabdelhamiddiab.waiting.presentation.loginflow.scanqr.ScanQrViewModel
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class ScanOrPickQrCode : Fragment() {
    val viewModel by viewModels<ScanOrPickQrcodeViewModel>()
    private lateinit var binding: FragmentScanOrPickQrCodeBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var scanner: BarcodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
// for activity result
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_scan_or_pick_qr_code,
            container,
            false
        )
        binding.buttonChooseScan.setOnClickListener {
            findNavController().navigate(R.id.action_scanOrPickQrCode_to_scanQrCodeFragment)
        }
        binding.buttonChoosePick.setOnClickListener {
            openSomeActivityForResult()
        }

        viewModel.userId.observe(viewLifecycleOwner) {
            if (it != null) {
                //to check if it real user id before save it
                viewModel.downloadServiceV(it)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error when taking the QR code",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.tokenUploaded.observe(viewLifecycleOwner) {
            if (it == true) {

                viewModel.sayIfClientIsInAVisit(true)
                findNavController().navigate(R.id.action_scanOrPickQrCode_to_clientFragment)

            }
        }
        viewModel.service.observe(viewLifecycleOwner) {
            if (it != null) {
                val userId = viewModel.userId.value!!
                viewModel.saveUserIdInPreferences(userId)
                viewModel.uploadMyClientToken(
                    Token(MyFirebaseMessagingService.token.toString())
                )
            } else {
                Toast.makeText(requireContext(), "Wrong QR CODE", Toast.LENGTH_SHORT).show()
                //findNavController().navigate(R.id.action_scanQrCodeFragment_to_homeFragment)
            }
        }

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE
            ).build()
        scanner = BarcodeScanning.getClient(options)


        return binding.root
    }

    // GET THE QR CODE VALUE
    //*******************************************************
    private fun openSomeActivityForResult() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
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
        }.addOnFailureListener {
            Log.d(TAG, "Failure")
        }.addOnCanceledListener {
            Log.d(TAG, "Canceled")
        }
    }}
//*************************************************
/*
override fun onResume() {
    super.onResume()
    (requireActivity() as MainActivity).hideStatusBar()
}

    override fun onStop() {
        (requireActivity() as MainActivity).showStatusBar()
        super.onStop()
    }
}*/
