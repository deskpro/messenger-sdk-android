package com.deskpro.messenger.ui

import android.app.Activity
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast

internal class MessengerWebInterface(
    private val context: Activity,
    private val webView: WebView,
    private val url: String,
    private val jtwToken: String,
    private val userJson: String
) {
    @JavascriptInterface
    fun close() {
        context.finish()
    }

    @JavascriptInterface
    fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun getJwtToken(): String {
        return jtwToken
    }

    @JavascriptInterface
    fun getUserInfo(): String {
        return userJson
    }

    @JavascriptInterface
    fun reloadPage() {
        context.runOnUiThread {
            webView.loadUrl(url)
        }
    }
}
