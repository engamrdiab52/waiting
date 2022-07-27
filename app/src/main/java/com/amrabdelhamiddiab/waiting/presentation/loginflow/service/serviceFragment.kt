package com.amrabdelhamiddiab.waiting.presentation.loginflow.service

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentServiceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class serviceFragment : Fragment() {
    private lateinit var binding: FragmentServiceBinding
    private val viewModel by viewModels<ServiceViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service, container, false)
        viewModel.service.observe(viewLifecycleOwner) {
            Log.d(MainActivity.TAG, "33333333333333333333333" + it.toString())
        }
        val service = viewModel.loadServiceFromPreferences()
        binding.textViewCategory.text = service?.category ?: ""
        binding.textViewNameOfService.text = service?.name_of_service ?: ""
        binding.textViewPeriodPerEachService.text = service?.period_per_each_service.toString()
        binding.buttonLogout.setOnClickListener {
            viewModel.removeServiceFromPreferences()
            viewModel.signOut()
            viewModel.removeUserFromPreferences()
            findNavController().navigate(R.id.action_serviceFragment_to_homeFragment)
        }

        binding.buttonLoad.setOnClickListener {
            viewModel.downloadServiceV()
        }
        binding.buttonQrcode.setOnClickListener {
            findNavController().navigate(R.id.action_serviceFragment_to_QRcodeFragment)
        }
        return binding.root
    }

}