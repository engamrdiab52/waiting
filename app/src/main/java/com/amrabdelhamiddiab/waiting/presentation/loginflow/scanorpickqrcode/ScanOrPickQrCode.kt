package com.amrabdelhamiddiab.waiting.presentation.loginflow.scanorpickqrcode

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentScanOrPickQrCodeBinding
import com.amrabdelhamiddiab.waiting.databinding.FragmentScanQrCodeBinding

class ScanOrPickQrCode : Fragment() {
    private lateinit var binding: FragmentScanOrPickQrCodeBinding
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
            Log.d(TAG, "")
        }
        binding.buttonChoosePick.setOnClickListener {
            Log.d(TAG, "")
        }


        return binding.root
    }
}