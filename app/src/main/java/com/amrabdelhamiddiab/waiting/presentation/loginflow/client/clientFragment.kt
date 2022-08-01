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
        viewModel.downloadServiceV()
        viewModel.retrieveOrderFromPreferences()
        binding.cardViewAddMyNumber.setOnClickListener {
            displayDialog()
        }
        binding.buttonEndVisit.setOnClickListener {
            displayDialogAreYouSure()

        }
        binding.buttonAddMyNumber.setOnClickListener {
            displayDialog()
        }
        viewModel.myNumber.observe(viewLifecycleOwner) {
            binding.textViewMyNumber.text = it.toString()

        }
        viewModel.service.observe(viewLifecycleOwner) {
            if (it != null){
                binding.textViewCategory.text = it.category
                binding.textViewNameOfService.text = it.name_of_service
                val text = it.period_per_each_service
                binding.textViewPeiodForEachVisitor.text = "about $text minuets for each visit"
                Log.d(TAG,"323232323232323.................... $it")
            }else {
                displayDialogWrongQrcode()
                Log.d(TAG,"323232323232323.................... Service is NUll")
            }


        }
        val userId = viewModel.retrieveUserIdFromPreferences()
        viewModel.notifyWhenOrderChange(userId)

        viewModel.orderValue.observe(viewLifecycleOwner) {
            if (it == null){
                binding.textViewOrder.text = "0"
            }
            binding.textViewOrder.text = it?.order.toString()

            orderNumber = it?.order?.toInt() ?: 0
            //  it?.let { it1 -> viewModel.saveOrderInPreferences(it1) }
        }
        binding.buttonScanQrCode.setOnClickListener {
            Log.d(TAG, "buttonScanQrCode called")
            viewModel.saveMyNumberInPreferences(0)
            findNavController().navigate(R.id.action_clientFragment_to_scanQrCodeFragment)

        }
        return binding.root
    }


    private fun displayDialog() {
        MaterialDialog(requireContext()).show {
            input(
                hint = "Enter Your Order Here",
                allowEmpty = false,
                maxLength = 3,
                inputType = InputType.TYPE_CLASS_NUMBER
            ) { _, myNumber ->
                if (myNumber.toString().toInt() > orderNumber) {
                    viewModel.saveMyNumberInPreferences(myNumber.toString().toInt())
                    binding.textViewMyNumber.text = myNumber.toString()
                } else {
                    Toast.makeText(requireContext(), "Invalid Number", Toast.LENGTH_SHORT).show()
                }
            }
            positiveButton(R.string.ok)
        }
    }

    private fun displayDialogAreYouSure() {
        MaterialDialog(requireContext()).show {
            title(R.string.end_visit)
            message(R.string.are_you_sure)
            positiveButton(R.string.yes) {
                viewModel.saveMyNumberInPreferences(0)
              //  viewModel.resetUserIdForClientInPreferences()
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
}
