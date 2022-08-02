package com.amrabdelhamiddiab.waiting.presentation.loginflow.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentHomeBinding
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
       //**************************************
        viewModel.service.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(R.id.action_homeFragment_to_serviceFragment)
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_createServiceFragment)
            }
        }
        binding.buttonService.setOnClickListener {
            if (checkInternetConnection(requireActivity().applicationContext)) {
                if (viewModel.userLoggedIn() == null) {
                    findNavController().navigate(R.id.action_homeFragment_to_nested_graph_login)
                } else {
                    viewModel.downloadServiceV()
                }
            } else {
                displayNoInternerConnection()
            }
        }
        binding.buttonClient.setOnClickListener {
            if (checkInternetConnection(requireActivity().applicationContext)) {
                if (viewModel.getClientInAVisit()) {
                    findNavController().navigate(R.id.action_homeFragment_to_clientFragment)
                } else {
                    findNavController().navigate(R.id.action_homeFragment_to_scanQrCodeFragment)
                }
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
