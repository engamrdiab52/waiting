package com.amrabdelhamiddiab.waiting.presentation.loginflow.scanqrservice

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.Size
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.amrabdelhamiddiab.core.domain.Token
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.MyFirebaseMessagingService
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentScanQrServiceBinding
import com.amrabdelhamiddiab.waiting.framework.utilis.MyImageAnalyzer
import com.amrabdelhamiddiab.waiting.framework.utilis.MyImageAnalyzerService
import com.google.android.material.textfield.TextInputEditText
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanQrServiceFragment : Fragment() {

    val viewModel by viewModels<ScanQrServiceViewModel>()
    private lateinit var binding: FragmentScanQrServiceBinding

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var analyzer: MyImageAnalyzerService
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // ??
            } else {
                showPermissionDeniedDialog()
            }
        }
        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_scan_qr_service, container, false)

        analyzer = MyImageAnalyzerService(
            requireContext(),
            viewModel,
            findNavController()
        )
        startScanQrCode()
        viewModel.clientTokenString.observe(viewLifecycleOwner) {
            if (it != null) {
                //show bottomSheet

            } else {
                Toast.makeText(requireContext(), "Wrong QR Code", Toast.LENGTH_SHORT).show()
            }
        }
/*        viewModel.tokenUploaded.observe(viewLifecycleOwner){
            if (it == true){
                viewModel.sayIfClientIsInAVisit(true)
                findNavController().navigate(R.id.action_scanQrCodeFragment_to_clientFragment)
            }

        }
        viewModel.userId.observe(viewLifecycleOwner){
            if (it != null) {
                viewModel.downloadServiceV(it)
            }else{
                Toast.makeText(requireContext(), "Error when taking the QR code", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.service.observe(viewLifecycleOwner){
            if (it != null){
                val userId = viewModel.userId.value!!
                viewModel.saveUserIdInPreferences(userId)
                viewModel.uploadMyClientToken(Token(MyFirebaseMessagingService.token.toString()) )
            }else {
                Toast.makeText(requireContext(), "Wrong QR CODE", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_scanQrCodeFragment_to_homeFragment)
            }
        }*/
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun showBottomSheet() {
        val dialog = MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            customView(
                R.layout.bottomshhet_custom_view_client_order,
                scrollable = false,
                horizontalPadding = false
            )
            autoDismissEnabled

            cornerRadius(16F)
        }
        requireContext()
        val customView = dialog.getCustomView()
        //    val scanQrChoice = customView.findViewById<LinearLayout>(R.id.linearLayout_scanQr)
        //    val browsQrChoice = customView.findViewById<LinearLayout>(R.id.linearLayout_brows_qr)
        val clientOrderEditText =
            customView.findViewById<TextInputEditText>(R.id.edit_text_bottom_sheet_client_order)
        val button = customView.findViewById<Button>(R.id.button_save_bottom_sheet)
        button.setOnClickListener {
            //CALL viewModel to create token Object
            val clientOrder = clientOrderEditText.text.toString().toInt()
            viewModel.uploadTokenObject(clientOrder)
            Log.d(TAG, clientOrder.toString())
        }

    }

    private fun startScanQrCode() {
        askForCameraPermission()

    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview: Preview = Preview.Builder()
            .build()
        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        preview.setSurfaceProvider(binding.previewViewScanQrcodeService.surfaceProvider)

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        imageAnalysis.setAnalyzer(cameraExecutor, analyzer)

        cameraProvider.bindToLifecycle(
            this as LifecycleOwner,
            cameraSelector,
            imageAnalysis,
            preview
        )
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                // send to app settings if permission is denied permanently
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun askForCameraPermission() {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }
}