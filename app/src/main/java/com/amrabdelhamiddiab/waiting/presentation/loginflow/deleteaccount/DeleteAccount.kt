package com.amrabdelhamiddiab.waiting.presentation.loginflow.deleteaccount

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amrabdelhamiddiab.core.data.IPreferenceHelper
import com.amrabdelhamiddiab.waiting.MainActivity
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentDeleteAccountBinding
import com.amrabdelhamiddiab.waiting.framework.utilis.PreferenceManager
import com.amrabdelhamiddiab.waiting.framework.utilis.checkInternetConnection
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAccount : Fragment() {
    private lateinit var binding: FragmentDeleteAccountBinding
    private lateinit var preferenceHelper: IPreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_delete_account, container, false)
        preferenceHelper = PreferenceManager(requireContext().applicationContext)
        binding.btnDeleteAccount.setOnClickListener {
            if (checkInternetConnection(requireContext())) {
                try {
                    FirebaseAuth.getInstance().currentUser?.delete()
                    preferenceHelper.setUserServiceLoggedIn(false)
                    findNavController().navigate(R.id.nested_graph_login)
                    Log.d(MainActivity.TAG, FirebaseAuth.getInstance().currentUser.toString())

                } catch (e: Exception) {
                    Log.d(MainActivity.TAG, " error in mainActivity  " + e.message.toString())
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "No Network please turn on",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        // Inflate the layout for this fragment
        return binding.root
    }
}