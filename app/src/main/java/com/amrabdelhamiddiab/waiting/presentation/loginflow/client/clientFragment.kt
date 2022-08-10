package com.amrabdelhamiddiab.waiting.presentation.loginflow.client

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
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentClientBinding
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class clientFragment : Fragment() {
    private val viewModel by viewModels<ClientViewModel>()
    private lateinit var binding: FragmentClientBinding
    private var orderNumber: Int = 0

    var order: Order? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_client, container, false)
        val userId = viewModel.retrieveUserIdFromPreferences()
        viewModel.notifyWhenOrderChange(userId)
        viewModel.downloadServiceV(userId)
        viewModel.retrieveClientNumberFromPreferences()
        binding.cardViewAddMyNumber.setOnClickListener {
            if (checkInternetConnection(requireActivity().applicationContext)) {
                displayDialog()
            } else {
                displayNoInternerConnection()
            }
        }
        binding.buttonEndVisit.setOnClickListener {
            if (checkInternetConnection(requireActivity().applicationContext)) {
                displayDialogAreYouSure()
            } else {
                displayNoInternerConnection()
            }
        }
        binding.buttonAddMyNumber.setOnClickListener {
            if (checkInternetConnection(requireActivity().applicationContext)) {
                displayDialog()
            } else {
                displayNoInternerConnection()
            }
        }
        viewModel.myNumber.observe(viewLifecycleOwner) {
            binding.textViewMyNumber.text = it.toString()

        }
        viewModel.service.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.textViewCategory.text = it.category
                binding.textViewNameOfService.text = it.name_of_service
                val text = it.period_per_each_service
                binding.textViewPeiodForEachVisitor.text = "about $text minuets for each visit"
            } else {
                displayDialogWrongQrcode()
            }


        }


        viewModel.orderValue.observe(viewLifecycleOwner) {
            if (it == null) {
                binding.textViewOrder.text = "0"
            }
            binding.textViewOrder.text = it?.order.toString()

            orderNumber = it?.order?.toInt() ?: 0
            //  it?.let { it1 -> viewModel.saveOrderInPreferences(it1) }
        }
        binding.buttonScanQrCode.setOnClickListener {
            if (checkInternetConnection(requireActivity().applicationContext)) {
                Log.d(TAG, "buttonScanQrCode called")
                viewModel.saveMyNumberInPreferences(0)
                findNavController().navigate(R.id.action_clientFragment_to_scanQrCodeFragment)

            } else {
                displayNoInternerConnection()
            }
        }
        return binding.root
    }


    private fun displayDialog() {
        var myValue :CharSequence = ""
        MaterialDialog(requireContext()).show {
          val input =  input(
                hint = "Enter Your Order Here",
                allowEmpty = false,
                maxLength = 3,
                inputType = InputType.TYPE_CLASS_NUMBER
            ) { _, myNumber ->
              myValue = myNumber
            }
            positiveButton(R.string.add){
                if (myValue.isNotEmpty()){
                    if (myValue.toString().toInt() > orderNumber) {
                        viewModel.saveMyNumberInPreferences(myValue.toString().toInt())
                        binding.textViewMyNumber.text = myValue.toString()
                    } else {
                        Toast.makeText(requireContext(), "Invalid Number", Toast.LENGTH_SHORT).show()
                    }
                }else {
                    it.dismiss()
                }
            }
            negativeButton(R.string.cancel) {
                it.dismiss()
            }
        }
    }

    private fun displayDialogAreYouSure() {
        MaterialDialog(requireContext()).show {
            title(R.string.end_visit)
            message(R.string.are_you_sure)
            positiveButton(R.string.yes) {
                viewModel.saveMyNumberInPreferences(0)
                viewModel.sayIfClientIsInAVisit(false)
                findNavController().navigate(R.id.action_clientFragment_to_homeFragment)
            }
            negativeButton(R.string.no) {

            }
        }
    }

    private fun displayDialogWrongQrcode() {
        MaterialDialog(requireContext()).show {
            title(R.string.qrcode_fake_title)
            message(R.string.qrcode_fake_message)
            positiveButton(R.string.yes) {
                findNavController().navigate(R.id.action_clientFragment_to_homeFragment)
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
