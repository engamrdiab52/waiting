package com.amrabdelhamiddiab.waiting.presentation.loginflow.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()

    // private lateinit var preferenceHelper: IPreferenceHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        //  preferenceHelper = PreferenceManager(requireActivity().applicationContext)
        if (viewModel.getUserStateFromPreferences()) {
            findNavController().navigate(R.id.action_homeFragment_to_serviceFragment)
        }
        binding.buttonService.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_nested_graph_login)
        }
        binding.buttonClient.setOnClickListener {
            if (viewModel.loadClientOrder() != null){
                findNavController().navigate(R.id.action_homeFragment_to_clientFragment)
            }else {
                findNavController().navigate(R.id.action_homeFragment_to_scanQrCodeFragment)
            }

        }


        // Inflate the layout for this fragment
        return binding.root
    }

}