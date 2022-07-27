package com.amrabdelhamiddiab.waiting.presentation.loginflow.qrcode

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentQRcodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QRcodeFragment : Fragment() {

    private lateinit var binding: FragmentQRcodeBinding
    private val viewModel by viewModels<QrCodeViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_q_rcode, container, false)
        val userId = viewModel.retrieveUserIdFromPreferences()
        if ( userId.isNotEmpty()){
            Log.d(TAG, "USER ID:  $userId")
        }
            // Inflate the layout for this fragment
            return binding.root
    }

}