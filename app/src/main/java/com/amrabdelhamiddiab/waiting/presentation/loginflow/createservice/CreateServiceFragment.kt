package com.amrabdelhamiddiab.waiting.presentation.loginflow.createservice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amrabdelhamiddiab.core.domain.Service
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentCreateServiceBinding
import com.amrabdelhamiddiab.waiting.framework.utilis.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateServiceFragment : Fragment() {

    private lateinit var binding: FragmentCreateServiceBinding
    private val viewModel by viewModels<CreateFragmentViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_create_service, container, false)
        binding.editTextCategory.text?.clear()
        binding.editTextNameOfService.text?.clear()
        binding.editTextPeriodOfEachService.text?.clear()

        viewModel.downloading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingIndecator.visibility = View.VISIBLE
            } else {
                binding.loadingIndecator.visibility = View.GONE
            }
        }


        viewModel.serviceCreated.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.editTextCategory.text?.clear()
                binding.editTextNameOfService.text?.clear()
                binding.editTextPeriodOfEachService.text?.clear()
                findNavController().navigate(R.id.action_createServiceFragment_to_serviceFragment)
            }
        }
        binding.buttonSave.setOnClickListener {
            val category: String = binding.editTextCategory.text.toString()
            val serviceName = binding.editTextNameOfService.text.toString()
            val text = binding.editTextPeriodOfEachService.text.toString()
            if (category.isNotEmpty() && serviceName.isNotEmpty() && text.isNotEmpty()) {
                val timePeriod: Int = text.toInt()
                val service = Service(category, serviceName, "", timePeriod)
                viewModel.uploadServiceV(service)
            } else {
                requireContext().toast(getString(R.string.provide_all_fields))
            }
        }
        return binding.root
    }
}