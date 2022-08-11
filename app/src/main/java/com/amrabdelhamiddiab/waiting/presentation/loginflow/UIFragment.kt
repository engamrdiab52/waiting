package com.amrabdelhamiddiab.waiting.presentation.loginflow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentUIBinding

class UIFragment : Fragment() {
    private lateinit var binding: FragmentUIBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_u_i, container, false)


        return binding.root
    }
}