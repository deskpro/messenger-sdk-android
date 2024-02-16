package com.deskpro.messenger.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.deskpro.messenger.databinding.ActivityMessengerWebViewBinding
import com.deskpro.messenger.util.Constants
import com.deskpro.messenger.util.Constants.ERROR_PAGE_PATH
import com.deskpro.messenger.util.Constants.WEB_INTERFACE_KEY
import com.deskpro.messenger.util.Prefs
import com.deskpro.messenger.util.extensions.EvaluateScriptsUtil.initAndOpenScript
import com.deskpro.messenger.util.extensions.extractAppId
import com.deskpro.messenger.util.extensions.extractUrl
import timber.log.Timber

/**
 * Activity hosting the WebView for DeskPro Messenger functionality.
 *
 * The `MessengerWebViewActivity` class manages the WebView, handles WebView configuration, and sets up
 * communication with JavaScript through the [MessengerWebInterface]. It also handles WebView lifecycle events
 * and error handling.
 */
internal class MessengerWebViewActivity : AppCompatActivity() {

    /**
     * View binding instance for the activity layout.
     */
    private lateinit var binding: ActivityMessengerWebViewBinding

    private lateinit var deskProChromeClient: DeskProChromeClient

    /**
     * SharedPreferences utility for managing user information and JWT tokens.
     */
    private var prefs: Prefs? = null

    /**
     * Activity result launcher for handling file uploads.
     */
    private val fileUploadLauncher = this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data: Intent? = result.data
        deskProChromeClient.handleFileUpload(result.resultCode, data)
    }

    /**
     * Handles the creation of the activity.
     */
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

            deskProChromeClient = DeskProChromeClient(fileUploadLauncher)
            webChromeClient = deskProChromeClient

            // Add JavaScript interface for JS communication with the crucial key - androidApp
            addJavascriptInterface(
                MessengerWebInterface(
                    context = this@MessengerWebViewActivity,
                    webView = binding.webView,
                    url = url,
                    jtwToken = prefs?.getJwtToken() ?: "",
                    fcmToken = prefs?.getFCMToken() ?: "",
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

        //Handles the back button press to finish the activity.
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Timber.tag(TAG).d("onBackPressed")
                this@MessengerWebViewActivity.finish()
            }
        })
    }

    /**
     * Handles saving the WebView state when the activity is paused.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.webView.saveState(outState)
    }

    /**
     * Handles restoring the WebView state when the activity is resumed.
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.webView.restoreState(savedInstanceState)
    }

    /**
     * Loads the error HTML page in the WebView when an error occurs.
     */
    private fun loadErrorHtmlPage() {
        binding.webView.loadUrl(ERROR_PAGE_PATH)
    }

    /**
     * Companion object providing a convenient method to start the [MessengerWebViewActivity].
     */
    companion object {
        private const val TAG = "DeskPro"

        /**
         * Starts the [MessengerWebViewActivity] with the specified parameters.
         *
         * @param context The context from which the activity is started.
         * @param path The web URL path to load in the WebView.
         * @param appId The aaspplication ID associated with the DeskPro Messenger module.
         */
        fun start(context: Context, path: String, appId: String) {
            val intent = Intent(context, MessengerWebViewActivity::class.java)
                .putExtra(Constants.WEB_URL, path)
                .putExtra(Constants.APP_ID, appId)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        fun notifIntent(context: Context, path: String, appId: String): Intent {
            //.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            return Intent(context, MessengerWebViewActivity::class.java)
                .putExtra(Constants.NEW_MESSAGE, true)
                .putExtra(Constants.WEB_URL, path)
                .putExtra(Constants.APP_ID, appId)
        }
    }
}
