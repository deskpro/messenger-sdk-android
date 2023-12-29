package com.deskpro.messenger.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.deskpro.messenger.util.extensions.extractUrl
import com.deskpro.messenger.util.extensions.initScript
import com.deskpro.messenger.util.extensions.openScript
import kotlinx.coroutines.runBlocking

internal class MessengerWebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptEnabled = true

        webView.webChromeClient = object : WebChromeClient() {}

        /**
         * Add javascript interface for JS communication with crucial key - androidApp
         */
        webView.addJavascriptInterface(MessengerWebInterface(this), "androidApp")

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                Uri.parse(request?.url.toString())
                return false
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                /**
                 * This will be refactored on JS, there will be only one script
                 */
                runBlocking {
                    view?.evaluateJavascript(initScript(), null)
                }

                val handler = Handler()
                handler.postDelayed({
                    view?.evaluateJavascript(openScript(), null)
                }, 500)
            }
        }

        setContentView(webView)

        val url = extractUrl(intent)
        url.let { webView.loadUrl(url) }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}
