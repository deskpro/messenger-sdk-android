package com.deskpro.messenger.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.deskpro.messenger.databinding.ActivityMessengerWebViewBinding
import com.deskpro.messenger.util.Constants.WEB_INTERFACE_KEY
import com.deskpro.messenger.util.extensions.EvaluateScriptsUtil.initAndOpenScript
import com.deskpro.messenger.util.extensions.extractUrl

internal class MessengerWebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessengerWebViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessengerWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding.webView) {
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true

            webChromeClient = object : WebChromeClient() {}

            /**
             * Add javascript interface for JS communication with crucial key - androidApp
             */
            addJavascriptInterface(
                MessengerWebInterface(this@MessengerWebViewActivity),
                WEB_INTERFACE_KEY
            )

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    Uri.parse(request?.url.toString())
                    return false
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.progressBar.visibility = View.VISIBLE
                    view?.evaluateJavascript(initAndOpenScript(), null)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressBar.visibility = View.GONE
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                }
            }
        }

        val url = extractUrl(intent)
        url.let { binding.webView.loadUrl(url) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.webView.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.webView.restoreState(savedInstanceState)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}
