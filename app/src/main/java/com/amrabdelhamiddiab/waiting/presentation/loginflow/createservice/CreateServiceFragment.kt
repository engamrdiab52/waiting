package com.amrabdelhamiddiab.waiting.presentation.loginflow.createservice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentCreateServiceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateServiceFragment : Fragment() {

    private lateinit var binding: FragmentCreateServiceBinding
    private val viewModel by viewModels<CreateFragmentViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_service, container, false)
        binding.buttonSave.setOnClickListener {
            val category : String = binding.editTextCategory.text.toString()
           val serviceName = binding.editTextNameOfService.text.toString()
            val text = binding.editTextPeriodOfEachService.text.toString()
            val timePeriod: Int = if (text.isEmpty()){
                0
            } else {
                text.toInt()
            }
            val service = Service(category, serviceName, "", timePeriod)
            viewModel.saveServiceInPreferences(service)
            val userId = viewModel.fetchUserId()
            if (userId != null) {
                viewModel.uploadServiceV(userId, service)
            }
            binding.editTextCategory.text?.clear()
            binding.editTextNameOfService.text?.clear()
            binding.editTextPeriodOfEachService.text?.clear()
            Log.d(TAG, service.toString())
            findNavController().navigate(R.id.action_createServiceFragment_to_serviceFragment)
        }
        // Inflate the layout for this fragment
        return binding.root
    }

}