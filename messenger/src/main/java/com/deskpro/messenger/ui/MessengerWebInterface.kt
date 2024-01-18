package com.deskpro.messenger.ui

import android.app.Activity
import android.webkit.JavascriptInterface
import android.webkit.WebView

internal class MessengerWebInterface(
    private val context: Activity,
    private val webView: WebView,
    private val url: String
) {
    @JavascriptInterface
    fun close() {
        context.finish()
    }

    @JavascriptInterface
    fun reloadPage() {
        context.runOnUiThread {
            webView.loadUrl(url)
        }
    }
}
