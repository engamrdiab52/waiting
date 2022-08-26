package com.amrabdelhamiddiab.waiting.presentation.loginflow.scanqr

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentScanQrCodeBinding
import com.amrabdelhamiddiab.waiting.framework.utilis.MyImageAnalyzer
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class ScanQrCodeFragment : Fragment() {

    val viewModel by viewModels<ScanQrViewModel>()
    private lateinit var binding: FragmentScanQrCodeBinding

    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var analyzer: MyImageAnalyzer
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
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_scan_qr_code, container, false)
        // Inflate the layout for this fragment
        Log.d(TAG, "ScanQrCodeFragment called...................................")
        analyzer = MyImageAnalyzer(
            viewModel
        )
        startScanQrCode()
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
                findNavController().navigate(R.id.action_scanQrCodeFragment_to_clientFragment)
                 }else {
                Toast.makeText(requireContext(), "Wrong QR CODE", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_scanQrCodeFragment_to_homeFragment)
            }
        }

        return binding.root
    }

    private fun startScanQrCode() {
        askForCameraPermission()

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


    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview: Preview = Preview.Builder()
            .build()
        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

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

    private fun askForCameraPermission() {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

}