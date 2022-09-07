package com.amrabdelhamiddiab.waiting.presentation.loginflow.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.LayoutMode.WRAP_CONTENT
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentHomeBinding
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.amrabdelhamiddiab.waiting.framework.utilis.showCustomToast
import com.amrabdelhamiddiab.waiting.framework.utilis.toast
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var scanner: BarcodeScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val imageUri = data?.data
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
    ): View {
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel.service.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(R.id.action_homeFragment_to_serviceFragment)
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_createServiceFragment)
            }
        }
        viewModel.emailVerified.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.downloadServiceV()
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_nested_graph_login)
            }
        }
        binding.buttonService.setOnClickListener {
            startAsService()
        }
        binding.buttonClient.setOnClickListener {
            startAsClient()
        }
        viewModel.userId_for_client.observe(viewLifecycleOwner) {
            if (it != null) {
                //to check if it real user id before save it
                viewModel.downloadServiceV_pick(it)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error when taking the QR code",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.service_for_client.observe(viewLifecycleOwner) {
            if (it != null) {
                val userId = viewModel.userId_for_client.value!!
                viewModel.saveUserIdInPreferences(userId)
                findNavController().navigate(R.id.action_homeFragment_to_clientFragment)
            } else {
                Toast.makeText(requireContext(), "Wrong QR CODE", Toast.LENGTH_SHORT).show()
            }
        }

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE
            ).build()
        scanner = BarcodeScanning.getClient(options)
        return binding.root
    }

    private fun startAsClient() {
        if (checkInternetConnection(requireActivity().applicationContext)) {
            if (viewModel.getClientInAVisit()) {
                findNavController().navigate(R.id.action_homeFragment_to_clientFragment)
            } else {
                showBottomSheet()
            }
        } else {
            displayNoInternetConnection()
        }
    }

    private fun startToScan() {
        findNavController().navigate(R.id.action_homeFragment_to_scanQrCodeFragment)
    }

    private fun startToBrows() {
        openSomeActivityForResult()
    }

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
    }

    private fun startAsService() {
        if (checkInternetConnection(requireActivity().applicationContext)) {
            viewModel.userLoggedIn()
        } else {
            displayNoInternetConnection()
        }
    }

    private fun showBottomSheet() {
        val dialog = MaterialDialog(requireContext() , BottomSheet(WRAP_CONTENT)).show {
            customView(
                R.layout.bottomsheet_custom_view,
                scrollable = false,
                horizontalPadding = false
            )
            autoDismissEnabled
            cornerRadius(16F)
        }
        requireContext()
        val customView = dialog.getCustomView()
        val scanQrChoice = customView.findViewById<LinearLayout>(R.id.linearLayout_scanQr)
        val browsQrChoice = customView.findViewById<LinearLayout>(R.id.linearLayout_brows_qr)
        scanQrChoice.setOnClickListener {
            startToScan()
            dialog.dismiss()
        }
        browsQrChoice.setOnClickListener {
            startToBrows()
            dialog.dismiss()
        }
    }


    private fun displayNoInternetConnection() {
        MaterialDialog(requireContext()).show {
            cancelOnTouchOutside(true)
            title(R.string.no_internet_title)
            message(R.string.no_internet_message)
        }
    }


}
