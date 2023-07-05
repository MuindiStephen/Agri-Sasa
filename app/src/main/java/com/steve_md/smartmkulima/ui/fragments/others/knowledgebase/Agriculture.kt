package com.steve_md.smartmkulima.ui.fragments.others.knowledgebase

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.steve_md.smartmkulima.databinding.FragmentAgricultureBinding


class Agriculture : Fragment() {

    private lateinit var binding:FragmentAgricultureBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAgricultureBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        setUpBinding()
        initData()
    }

    private fun setUpBinding() {
        binding.apply {
            GoBackFromAgriculture.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
    private fun initData() {
        binding.webViewAgriculture.loadUrl("https://www.roysfarm.com/onion-farming/")
        binding.webViewAgriculture.webViewClient = WebViewController()

        binding.webViewAgriculture.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                if (view?.contentHeight == 0) {
                    view.reload()
                } else {
                    super.onPageFinished(view, url)
                }
            }

        }
    }
}