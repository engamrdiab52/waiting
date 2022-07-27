package com.amrabdelhamiddiab.waiting.presentation.loginflow.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentClientBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class clientFragment : Fragment() {
    private val viewModel by viewModels<ClientViewModel>()
    private lateinit var binding: FragmentClientBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_client, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

}