package com.amrabdelhamiddiab.waiting.presentation.loginflow.qrcodeclient

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.MyFirebaseMessagingService
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentQrCodeClientBinding
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class QrCodeClientFragment : Fragment() {
    private lateinit var binding: FragmentQrCodeClientBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var bitmap: Bitmap
    private var permissionGranted: Boolean = false

    private val viewModel by viewModels<QrCodeClientViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                permissionGranted =true
                // Do if the permission is granted
                Toast.makeText(requireContext(), "permission Already granted", Toast.LENGTH_LONG)
                    .show()

            } else {
                // Do otherwise
                //   Toast.makeText(requireContext(), "permission Denied", Toast.LENGTH_LONG).show()
                showPermissionDeniedDialog()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_qr_code_client, container, false)

        binding.buttonSaveQrcodeClient.setOnClickListener {
            if (permissionGranted) {
                // Permissions are already granted, do your stuff
                saveImage(bitmap, requireActivity(), "Waiting App")
                Toast.makeText(requireContext(), "Image saved in Gallery", Toast.LENGTH_LONG).show()

            }
        }
 /*       binding.buttonSaveQrcodeClient.setOnClickListener {
            binding.imageViewClientQrcode.setImageBitmap(bitmap)
        }*/

        binding.buttonGenerateQrClient.setOnClickListener {
            binding.imageViewClientQrcode.setImageBitmap(bitmap)
        }
        askForExternalStoragePermission()

       val token = MyFirebaseMessagingService.token
            if ( token!!.isNotEmpty()){
                val encoder = BarcodeEncoder()
                bitmap = encoder.encodeBitmap(token, BarcodeFormat.QR_CODE, 400, 400)
                Log.d(MainActivity.TAG, "USER ID::::::  $token")
            }




        return binding.root
    }


    private fun askForExternalStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }else{
            permissionGranted = true
        }

    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton("App Settings",
                DialogInterface.OnClickListener { _, _ ->
                    // send to app settings if permission is denied permanently
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package",requireActivity().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                })
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveImage(
        bitmap: Bitmap,
        context: Context,
        folderName: String
    ) {
        if (Build.VERSION.SDK_INT >= 29) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            // RELATIVE_PATH and IS_PENDING are introduced in API 29.

            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                //here u don't access the file directly ,but through content resolver
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
        } else {
            val directory = File(Environment.getExternalStorageDirectory().toString() + File.separator + folderName)
            // getExternalStorageDirectory is deprecated in API 29

            if (!directory.exists()) {
                directory.mkdirs()
            }
            //here i want to add real file name that can be overridden
            val fileName = System.currentTimeMillis().toString() + ".png"
            val file = File(directory, fileName)
            saveImageToStream(bitmap, FileOutputStream(file))
            val values = contentValues()
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            // .DATA is deprecated in API 29
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        }
    }

    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}