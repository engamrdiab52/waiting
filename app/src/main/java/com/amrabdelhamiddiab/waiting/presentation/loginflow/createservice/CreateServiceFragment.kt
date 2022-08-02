package com.amrabdelhamiddiab.waiting.presentation.loginflow.createservice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentCreateServiceBinding
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
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
            if (checkInternetConnection(requireActivity().applicationContext)) {

                val category: String = binding.editTextCategory.text.toString()
                val serviceName = binding.editTextNameOfService.text.toString()
                val text = binding.editTextPeriodOfEachService.text.toString()
                val timePeriod: Int =text.toInt()
                val service = Service(category, serviceName, "", timePeriod)
                viewModel.uploadServiceV(service)
                viewModel.uploadOrderValueFirstTime()
              //  viewModel.saveServiceInPreferences(service)
                binding.editTextCategory.text?.clear()
                binding.editTextNameOfService.text?.clear()
                binding.editTextPeriodOfEachService.text?.clear()
                findNavController().navigate(R.id.action_createServiceFragment_to_serviceFragment)
            } else {
                displayNoInternerConnection()
            }
        }
        return binding.root
    }
    private fun displayNoInternerConnection() {
        MaterialDialog(requireContext()).show {
            cancelOnTouchOutside(true)
            title(R.string.no_internet_title)
            message(R.string.no_internet_message)
        }
    }
}