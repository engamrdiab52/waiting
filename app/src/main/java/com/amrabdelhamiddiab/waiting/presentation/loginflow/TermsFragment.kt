package com.amrabdelhamiddiab.waiting.presentation.loginflow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.amrabdelhamiddiab.waiting.R
import com.amrabdelhamiddiab.waiting.databinding.FragmentTermsBinding

class TermsFragment : Fragment() {

    private lateinit var binding: FragmentTermsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_terms, container, false)
        binding.webView.webViewClient = WebViewClient()
        true.also { binding.webView.settings.javaScriptEnabled = it }
        binding.webView.loadUrl("https://engamrdiab52.github.io/termsofuse")
        return binding.root
    }

}