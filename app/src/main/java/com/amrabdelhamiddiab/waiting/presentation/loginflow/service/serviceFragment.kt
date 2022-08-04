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
import com.amrabdelhamiddiab.core.domain.NotificationData
import com.amrabdelhamiddiab.core.domain.PushNotification
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TOPIC
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentServiceBinding
import com.amrabdelhamiddiab.waiting.framework.firebase.fcm.FcmService
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class serviceFragment : Fragment() {
    private lateinit var binding: FragmentServiceBinding
    private val viewModel by viewModels<ServiceViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service, container, false)
        viewModel.downloadServiceV()
        viewModel.downloadOrderV()
        viewModel.service.observe(viewLifecycleOwner) {
            binding.textViewCategory.text = it?.category ?: ""
            binding.textViewNameOfService.text = it?.name_of_service ?: ""
            binding.textViewPeriodPerEachService.text = it?.period_per_each_service.toString()
        }
        viewModel.orderValue.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.textViewCurrentNumber.text = it.order.toString()
            } else {
                binding.textViewCurrentNumber.text = "0"
            }

        }


        binding.buttonServiceIncrementOrder.setOnClickListener {
            if (checkInternetConnection(requireActivity().applicationContext)) {
                val currentOrderValueFromTextView = binding.textViewCurrentNumber.text.toString()
                viewModel.incrementCurrentOrderValue(currentOrderValueFromTextView)
                Log.d(TAG, "increment ${binding.textViewCurrentNumber.text}")

                PushNotification(
                    NotificationData(
                        binding.textViewCurrentNumber.text.toString(),
                        "you are the after next"
                    ), TOPIC
                ).also {
                    sendNotification(it)
                }

            } else {
                displayNoInternerConnection()
            }
        }
        binding.buttonServiceDecreaseOrder.setOnClickListener {
            if (checkInternetConnection(requireActivity().applicationContext)) {
                val currentOrderValueFromTextView = binding.textViewCurrentNumber.text.toString()
                viewModel.decrementCurrentOrderValue(currentOrderValueFromTextView)
                Log.d(TAG, "increment ${binding.textViewCurrentNumber.text}")
            } else {
                displayNoInternerConnection()
            }
        }

        //********************************************************************************

        binding.buttonLogout.setOnClickListener {
            if (checkInternetConnection(requireActivity().applicationContext)) {
                displayDialogLogOut()
            } else {
                displayNoInternerConnection()
            }

        }
        //********************************************************************************
        binding.buttonServiceDeleteAccount.setOnClickListener {
            if (checkInternetConnection(requireActivity().applicationContext)) {
                displayDialogDeleteAccount()
            } else {
                displayNoInternerConnection()
            }

        }
        //*******************************************************************************
        binding.buttonQrcode.setOnClickListener {
            findNavController().navigate(R.id.action_serviceFragment_to_QRcodeFragment)
        }
        //*******************************************************************************
        binding.buttonEditService.setOnClickListener {

            if (checkInternetConnection(requireActivity().applicationContext)) {
                findNavController().navigate(R.id.action_serviceFragment_to_createServiceFragment)
            } else {
                displayNoInternerConnection()
            }

        }
        //*******************************************************************************
        binding.buttonServiceEditOrder.setOnClickListener {
            if (checkInternetConnection(requireActivity().applicationContext)) {
                displayDialog()
            } else {
                displayNoInternerConnection()
            }

        }
        binding.buttonServiceResetOrder.setOnClickListener {
            if (checkInternetConnection(requireActivity().applicationContext)) {
                binding.textViewCurrentNumber.text = "0"
                viewModel.changeOrderValueV(0L)
            } else {
                displayNoInternerConnection()
            }


        }
        //***********************************************************************************

        return binding.root
    }

    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = FcmService.create().postNotification(notification)
                if (response.isSuccessful) {
                    Log.d(TAG, "Response: ${Gson().toJson(response.body())}")
                } else {
                    Log.e(TAG, response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }

    private fun displayDialogDeleteAccount() {
        MaterialDialog(requireContext()).show {
            title(R.string.dialog_delete_account_title)
            message(R.string.dialog_delete_account_message)
            positiveButton(R.string.ok) {
                viewModel.deleteAccountV()
                findNavController().navigate(R.id.action_serviceFragment_to_homeFragment)
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
                Log.d(TAG, "Logged out ")
                viewModel.signOut()
                findNavController().navigate(R.id.action_serviceFragment_to_homeFragment)
            }
            negativeButton(R.string.no) {

            }
        }
    }

    private fun displayNoInternerConnection() {
        MaterialDialog(requireContext()).show {
            cancelOnTouchOutside(true)
            title(R.string.no_internet_title)
            message(R.string.no_internet_message)
        }
    }
}