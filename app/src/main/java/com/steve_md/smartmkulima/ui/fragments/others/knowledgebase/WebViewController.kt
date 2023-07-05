package com.steve_md.smartmkulima.ui.fragments.others.knowledgebase

import android.webkit.WebView
import android.webkit.WebViewClient

class WebViewController : WebViewClient() {
    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return true
    }
}