package com.amrabdelhamiddiab.waiting.presentation.loginflow.service

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
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
            binding.textViewCategory.text = it?.category ?: ""
            binding.textViewNameOfService.text = it?.name_of_service ?: ""
            binding.textViewPeriodPerEachService.text = it?.period_per_each_service.toString()
        }
        viewModel.orderValue.observe(viewLifecycleOwner) {
            binding.textViewCurrentNumber.text = it.order.toString()
        }

        val service = viewModel.loadServiceFromPreferences()
        val order = viewModel.retrieveCurrentOrderOfService()
        binding.textViewCurrentNumber.text = order.ifEmpty { "0" }
        binding.textViewCategory.text = service?.category ?: ""
        binding.textViewNameOfService.text = service?.name_of_service ?: ""
        binding.textViewPeriodPerEachService.text = service?.period_per_each_service.toString()
        binding.buttonServiceIncrementOrder.setOnClickListener {
            Log.d(TAG, "INCREMENT")
        }
        binding.buttonServiceDecreaseOrder.setOnClickListener {
            Log.d(TAG, "DECREMENT")
        }

        //********************************************************************************

        binding.buttonLogout.setOnClickListener {
            displayDialogLogOut()
        }
        //********************************************************************************
        binding.buttonServiceDeleteAccount.setOnClickListener {
            displayDialogDeleteAccount()
        }
        //*******************************************************************************
        binding.buttonQrcode.setOnClickListener {
            findNavController().navigate(R.id.action_serviceFragment_to_QRcodeFragment)
        }
        //*******************************************************************************
        binding.buttonEditService.setOnClickListener {
            findNavController().navigate(R.id.action_serviceFragment_to_createServiceFragment)
        }
        //*******************************************************************************
        binding.buttonServiceEditOrder.setOnClickListener {
            displayDialog()
        }
        binding.buttonServiceResetOrder.setOnClickListener {
            viewModel.saveCurrentOrderOfService("0")
            binding.textViewCurrentNumber.text = "0"
            viewModel.changeOrderValueV(0L)
        }
        //***********************************************************************************

        return binding.root
    }

    private fun displayDialogDeleteAccount() {
        MaterialDialog(requireContext()).show {
            title(R.string.dialog_delete_account_title)
            message(R.string.dialog_delete_account_message)
            positiveButton(R.string.yes) {
                viewModel.deleteAccountV()
                viewModel.removeServiceFromPreferences()
                viewModel.removeUserFromPreferences()
                findNavController().navigate(R.id.action_serviceFragment_to_homeFragment)
            }
            negativeButton(R.string.no) {

            }
        }
    }

    private fun displayDialog() {
        MaterialDialog(requireContext()).show {
            input(
                hint = "Enter Current Number Here",
                allowEmpty = false,
                maxLength = 3,
                inputType = InputType.TYPE_CLASS_NUMBER
            ) { _, currentNumber ->
                viewModel.saveCurrentOrderOfService(currentNumber.toString())
                binding.textViewCurrentNumber.text = currentNumber.toString()
                viewModel.changeOrderValueV(currentNumber.toString().toLong())
            }
            positiveButton(R.string.ok)
        }
    }

    private fun displayDialogLogOut() {
        MaterialDialog(requireContext()).show {
            title(R.string.dialog_logout_title)
            message(R.string.dialog_logout_message)
            positiveButton(R.string.yes) {
                viewModel.removeServiceFromPreferences()
                viewModel.signOut()
                viewModel.removeUserFromPreferences()
                findNavController().navigate(R.id.action_serviceFragment_to_homeFragment)
            }
            negativeButton(R.string.no) {

            }
        }
    }

}