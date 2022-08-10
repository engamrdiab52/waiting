package com.amrabdelhamiddiab.waiting.presentation.loginflow.service

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.amrabdelhamiddiab.core.domain.NotificationData
import com.amrabdelhamiddiab.core.domain.PushNotification
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentServiceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class serviceFragment : Fragment() {
    private var passwordForDeleteAccount: String = ""
    private lateinit var binding: FragmentServiceBinding
    private val viewModel by viewModels<ServiceViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.downloadTokenV()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service, container, false)
        //    FirebaseMessaging.getInstance().subscribeToTopic()
        viewModel.notifyWhenServiceChange()
        viewModel.notifyWhenOrderChange()

        viewModel.downloading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingIndecator.visibility = View.VISIBLE
            } else {
                binding.loadingIndecator.visibility = View.GONE
            }
        }

        // val uidString = "/" + viewModel.uid + "/"
        //   Log.d(TAG, uidString + "2222222222222222222222222222222222222")
        /*    viewModel.downloadServiceV()
            viewModel.downloadOrderV()*/
        viewModel.service.observe(viewLifecycleOwner) {
            binding.textViewCategory.text = it?.category ?: ""
            binding.textViewNameOfService.text = it?.name_of_service ?: ""
            if (it != null) {
                binding.textViewPeriodPerEachService.text = it.period_per_each_service.toString()
            } else {
                binding.textViewPeriodPerEachService.text = "0"
            }
        }

        viewModel.orderValue.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.textViewCurrentNumber.text = it.order.toString()
                val token = viewModel.tokenDownloaded.value?.token ?: ""
                if (token.isNotEmpty()) {
                    PushNotification(
                        NotificationData(
                            "Current Serving Number",
                            it.order.toString()
                        ),
                        token
                    ).also { pushNotification ->
                        viewModel.sendNotification(pushNotification)
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "...No Clients Registered to get notifications",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
            }


        }
        viewModel.dataBaseError.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
        }
        //*****************************
        viewModel.userSignedOut.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(R.id.action_serviceFragment_to_homeFragment)
            }
        }
        //*********************************
        viewModel.tokenDownloaded.observe(viewLifecycleOwner) {
            if (it == null){
                Toast.makeText(requireContext(), "NO Client is waiting......", Toast.LENGTH_LONG).show()
            }

        }
        binding.buttonServiceIncrementOrder.setOnClickListener {
            val currentOrderValueFromTextView = binding.textViewCurrentNumber.text.toString()
            viewModel.incrementCurrentOrderValue(currentOrderValueFromTextView)
        }
        binding.buttonServiceDecreaseOrder.setOnClickListener {
            val currentOrderValueFromTextView = binding.textViewCurrentNumber.text.toString()
            viewModel.decrementCurrentOrderValue(currentOrderValueFromTextView)
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
            displayDialogEditOrder()
        }
        binding.buttonServiceResetOrder.setOnClickListener {
            binding.textViewCurrentNumber.text = "0"
            viewModel.changeOrderValueV(0L)
        }
        viewModel.userDeleted.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(R.id.action_serviceFragment_to_homeFragment)
            }
        }
        viewModel.serviceDeleted.observe(viewLifecycleOwner) {
            if (it == true) {
                if (passwordForDeleteAccount.isNotEmpty()) {
                    viewModel.deleteAccountV(passwordForDeleteAccount)
                }
            }
        }
        //***********************************************************************************

        return binding.root
    }

    private fun displayDialogDeleteAccount() {
        var myValue: CharSequence = ""
        MaterialDialog(requireContext()).show {
            title(R.string.dialog_delete_account_title)
            message(R.string.dialog_delete_account_message)
            val input = input(
                hint = "Enter Your Password here",
                allowEmpty = false,
                inputType = InputType.TYPE_CLASS_TEXT
            ) { _, password ->
                myValue = password
            }
            //*********************************
            positiveButton(R.string.delete_account) {
                if (myValue.isNotEmpty()) {
                    viewModel.deleteServiceV()
                    passwordForDeleteAccount = myValue.toString()
                } else {
                    it.dismiss()
                }
            }
            negativeButton(R.string.cancel) {
                it.dismiss()
            }
        }
    }


    private fun displayDialogEditOrder() {
        var myValue: CharSequence = ""
        MaterialDialog(requireContext()).show {
            val input = input(
                hint = "Enter current number here",
                allowEmpty = false,
                maxLength = 3,
                inputType = InputType.TYPE_CLASS_NUMBER
            ) { _, currentNumber ->
                myValue = currentNumber
            }
            positiveButton(R.string.edit) {
                if (myValue.isNotEmpty()) {
                    binding.textViewCurrentNumber.text = myValue.toString()
                    viewModel.changeOrderValueV(myValue.toString().toLong())
                } else {
                    it.dismiss()
                }
            }
            negativeButton(R.string.cancel) {
                it.dismiss()
            }
        }

    }

    private fun displayDialogLogOut() {
        MaterialDialog(requireContext()).show {
            title(R.string.dialog_logout_title)
            message(R.string.dialog_logout_message)
            positiveButton(R.string.yes) {
                Log.d(TAG, "Logged out ")
                viewModel.signOut()
            }
            negativeButton(R.string.cancel) {
                it.dismiss()
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