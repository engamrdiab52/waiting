package com.amrabdelhamiddiab.waiting.presentation.loginflow.home

import android.icu.text.DisplayContext
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.ViewCompat.getWindowInsetsController
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentHomeBinding
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      //  requireActivity().setTheme(R.style.Theme_Waiting_NoActionBar_Fragment)
       // requireContext().theme.applyStyle(R.style.Theme_Waiting_NoActionBar, true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        // WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
// Hide the status bar.

        //**************************************
        //Service part
        //************************
        viewModel.service.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(R.id.action_homeFragment_to_serviceFragment)
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_createServiceFragment)
            }
        }
        viewModel.emailVerified.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.downloadServiceV()
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_nested_graph_login)
            }
        }
        binding.buttonService.setOnClickListener {
            if (checkInternetConnection(requireActivity().applicationContext)) {
                viewModel.userLoggedIn()
            } else {
                displayNoInternerConnection()
            }
        }
        //*************************************************
        // Client PART
        //*****************************************
        binding.buttonClient.setOnClickListener {
            //==============================================================
            //original part
    /*        if (checkInternetConnection(requireActivity().applicationContext)) {
                if (viewModel.getClientInAVisit()) {
                    findNavController().navigate(R.id.action_homeFragment_to_clientFragment)
                } else {
                    //change to scan or pick
                    //  findNavController().navigate(R.id.action_homeFragment_to_scanQrCodeFragment)
                    findNavController().navigate(R.id.action_homeFragment_to_scanOrPickQrCode)
                }
            } else {
                displayNoInternerConnection()
            }*/
            //==========================================================
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
/*    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity ).hideStatusBar()
    }

    override fun onStop() {
        (requireActivity() as MainActivity ).showStatusBar()
        super.onStop()
    }
}*/
