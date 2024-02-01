package com.deskpro.messenger.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.deskpro.messenger.App
import com.deskpro.messenger.R
import com.deskpro.messenger.databinding.ActivityMessengerWebViewBinding
import com.deskpro.messenger.util.Constants
import com.deskpro.messenger.util.Constants.ERROR_PAGE_PATH
import com.deskpro.messenger.util.Constants.WEB_INTERFACE_KEY
import com.deskpro.messenger.util.Prefs
import com.deskpro.messenger.util.extensions.EvaluateScriptsUtil.initAndOpenScript
import com.deskpro.messenger.util.extensions.extractAppIcon
import com.deskpro.messenger.util.extensions.extractAppId
import com.deskpro.messenger.util.extensions.extractUrl

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

    /**
     * SharedPreferences utility for managing user information and JWT tokens.
     */
    private var prefs: Prefs? = null

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(TAG, "Notification permission granted!")
        } else {
            Log.w(TAG, "Notification permission denied!")
        }
    }

    private val notificationManager by lazy { App.appContext?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager }

    private var appIcon = -1

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
        appIcon = extractAppIcon(intent)
        prefs = Prefs(this, appId)

        with(binding.webView) {
            settings.domStorageEnabled = true
            settings.javaScriptEnabled = true

            webChromeClient = object : WebChromeClient() {}

            // Add JavaScript interface for JS communication with the crucial key - androidApp
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

        createNotificationChannel()
        askNotificationPermission()
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
     * Handles the back button press to finish the activity.
     */
    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    /**
     * Loads the error HTML page in the WebView when an error occurs.
     */
    private fun loadErrorHtmlPage() {
        binding.webView.loadUrl(ERROR_PAGE_PATH)
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Notification permission granted!")
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Log.w(TAG, "Notification permission denied permanently!")
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notifChannel =
                NotificationChannel(CHANNEL_ID, App.appContext?.getString(R.string.channel_name), NotificationManager.IMPORTANCE_DEFAULT)
            notifChannel.description = App.appContext?.getString(R.string.channel_description)
            notificationManager.createNotificationChannel(notifChannel)
        }
    }

    /** Set the notification.
     * @param icon A resource ID of the icon to be displayed in the notification.
     **/
    private fun showNotification(title: String, body: String, badgeNumber: Int = 0) {
        if (App.isForeground) {
            Log.d(TAG, "App is in foreground, no need to show notification")
            return
        }

        // Create an explicit intent for an Activity in your app.
        val intent = Intent(this, MessengerWebViewActivity::class.java).apply {
            //flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("new_message", true)
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(if (appIcon != -1) appIcon else R.drawable.deskpro_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setNumber(badgeNumber)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MessengerWebViewActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    override fun onStart() {
        super.onStart()
        App.isForeground = true
    }

    override fun onStop() {
        super.onStop()
        App.isForeground = false
    }

    /**
     * Companion object providing a convenient method to start the [MessengerWebViewActivity].
     */
    companion object {
        private const val TAG = "DeskPro"
        private const val CHANNEL_ID = "68349432239241"
        private const val NOTIFICATION_ID = 397841

        /**
         * Starts the [MessengerWebViewActivity] with the specified parameters.
         *
         * @param context The context from which the activity is started.
         * @param path The web URL path to load in the WebView.
         * @param appId The application ID associated with the DeskPro Messenger module.
         */
        fun start(context: Context, path: String, appId: String, appIcon: Int) {
            val intent = Intent(context, MessengerWebViewActivity::class.java)
                .putExtra(Constants.WEB_URL, path)
                .putExtra(Constants.APP_ID, appId)
                .putExtra(Constants.APP_ICON, appIcon)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
