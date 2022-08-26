package com.amrabdelhamiddiab.waiting.presentation.loginflow.resetpassword

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentResetPasswordBinding
import com.amrabdelhamiddiab.waiting.presentation.loginflow.LoginFlowViewModel
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {
    private val viewModel: LoginFlowViewModel by hiltNavGraphViewModels(R.id.nested_graph_login)
    private lateinit var binding: FragmentResetPasswordBinding
    private var validEmail: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_reset_password, container, false)
        viewModel.downloading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loadingIndecatorResetPassword.visibility = View.VISIBLE
            } else {
                binding.loadingIndecatorResetPassword.visibility = View.GONE
            }
        }
        viewModel.passwordChanged.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(requireContext(), "Email sent successfully", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_resetPasswordFragment_to_loginFragment)
            }
        }
        binding.btnSendVerificationEmail.setOnClickListener {
            validateEmailField()
            if (validEmail) {
                val email = binding.editTextEmailResetPassword.text.toString()
                viewModel.resetPassword(email)
            } else {
                Toast.makeText(requireContext(), "** INVALID CREDENTIALS **", Toast.LENGTH_LONG)
                    .show()
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun validateEmailField() {
        binding.editTextEmailResetPassword.validator().nonEmpty().validEmail().addErrorCallback {
            validEmail = false
            binding.textLayoutEmailResetPassword.error = it
            Log.d(MainActivity.TAG, it)
        }.addSuccessCallback {
            validEmail = true
            binding.textLayoutEmailResetPassword.error = null
        }.check()
    }
    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setDrawerLocked()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).setDrawerUnlocked()
    }
}