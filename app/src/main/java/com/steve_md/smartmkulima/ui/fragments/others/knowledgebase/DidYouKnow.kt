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
import com.steve_md.smartmkulima.databinding.FragmentDidYouKnowBinding


class DidYouKnow : Fragment() {

    private lateinit var binding: FragmentDidYouKnowBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDidYouKnowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        setUpBinding()
        initData()
    }

    private fun initData() {
        binding.webViewDidYouKnow.loadUrl("https://www.roysfarm.com/onion-farming/")
        binding.webViewDidYouKnow.webViewClient = WebViewController()

        binding.webViewDidYouKnow.webViewClient = object : WebViewClient() {
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

    private fun setUpBinding() {
        binding.textViewDidYouKnow.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}

