package com.deskpro.messenger.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import com.deskpro.messenger.util.Constants
import com.deskpro.messenger.util.Constants.ERROR_PAGE_PATH
import com.deskpro.messenger.util.Constants.WEB_INTERFACE_KEY
import com.deskpro.messenger.util.Prefs
import com.deskpro.messenger.util.extensions.EvaluateScriptsUtil.initAndOpenScript
import com.deskpro.messenger.util.extensions.extractAppId
import com.deskpro.messenger.util.extensions.extractUrl

internal class MessengerWebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessengerWebViewBinding

    private var prefs: Prefs? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessengerWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = extractUrl(intent)
        val appId = extractAppId(intent)
        prefs = Prefs(this, appId)

        with(binding.webView) {
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true

            webChromeClient = object : WebChromeClient() {}

            /**
             * Add javascript interface for JS communication with crucial key - androidApp
             */
            addJavascriptInterface(
                MessengerWebInterface(
                    context = this@MessengerWebViewActivity,
                    webView = binding.webView,
                    url = url,
                    jtwToken = prefs?.getJwtToken() ?: "",
                    userJson = prefs?.getUserInfoJson() ?: ""
                ),
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
                    binding.progressBar.visibility = View.GONE
                    loadErrorHtmlPage()
                }
            }
        }

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

    private fun loadErrorHtmlPage() {
        binding.webView.loadUrl(ERROR_PAGE_PATH)
    }

    companion object {
        fun start(context: Context, path: String, appId: String) {
            val intent = Intent(context, MessengerWebViewActivity::class.java)
                .putExtra(Constants.WEB_URL, path)
                .putExtra(Constants.APP_ID, appId)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
