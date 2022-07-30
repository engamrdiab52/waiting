package com.amrabdelhamiddiab.waiting.presentation.loginflow.client

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amrabdelhamiddiab.core.domain.Order
import com.amrabdelhamiddiab.waiting.MainActivity.Companion.TAG
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentClientBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class clientFragment : Fragment() {
    private val viewModel by viewModels<ClientViewModel>()
    private lateinit var binding: FragmentClientBinding

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
        viewModel.service.observe(viewLifecycleOwner) {
            binding.textViewCategory.text = it?.category ?: ""
            binding.textViewNameOfService.text = it?.name_of_service ?: ""
            val text = it?.period_per_each_service ?: "--"
            binding.textViewPeiodForEachVisitor.text = "about $text minuets for each visit"
         //   binding.textViewPeriodPerEachService.text = it?.period_per_each_service.toString()
        }

        Log.d(TAG, "..............clientFragment valled...............")
        val userId = viewModel.retrieveUserIdFromPreferences()
        viewModel.notifyWhenOrderChange(userId)

        viewModel.orderValue.observe(viewLifecycleOwner) {
            binding.textViewOrder.text = it?.order.toString()

          //  it?.let { it1 -> viewModel.saveOrderInPreferences(it1) }
        }
        binding.buttonScanQrCode.setOnClickListener {
            findNavController().navigate(R.id.action_clientFragment_to_scanQrCodeFragment)
        }
        return binding.root
    }
}

/*  binding.previewView.visibility = View.VISIBLE
             binding.linearLayout.visibility = View.GONE*/

/*     if (viewModel.retrieveUserIdFromPreferences().isNotEmpty()){
            viewModel.notifyWhenOrderChange()

            binding.previewView.visibility = View.GONE
            binding.linearLayout.visibility = View.VISIBLE
        }else {
            binding.previewView.visibility = View.VISIBLE
            binding.linearLayout.visibility = View.GONE
        }
        viewModel.orderValue.observe(viewLifecycleOwner) {
            binding.textView.text = it?.order.toString()
        }*/