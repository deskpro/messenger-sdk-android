package com.deskpro.messenger.ui

import android.app.Activity
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import timber.log.Timber

/**
 * Internal class representing a JavaScript interface for communication between the WebView
 * and the Android application in the Messenger module.
 *
 * The `MessengerWebInterface` class is designed to handle JavaScript calls from the WebView
 * and provides methods for interacting with the Android application.
 *
 * @property context The hosting activity context.
 * @property webView The WebView associated with the interface.
 * @property url The URL associated with the WebView.
 * @property jtwToken The JSON Web Token (JWT) used for authentication.
 * @property userJson The JSON representation of the user information.
 */
internal class MessengerWebInterface(
    private val context: Activity,
    private val webView: WebView,
    private val url: String,
    private val jtwToken: String,
    private val fcmToken: String,
    private val userJson: String
) {
    /**
     * Closes the hosting activity.
     */
    @JavascriptInterface
    fun close() {
        Timber.tag("AppEvent").d("close")
        context.finish()
    }

    /**
     * Displays a toast message with the provided [message].
     *
     * @param message The message to be displayed in the toast.
     */
    @JavascriptInterface
    fun showMessage(message: String) {
        Timber.tag("AppEvent").d("showMessage: $message")
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Retrieves the JSON Web Token (JWT) used for authentication.
     *
     * @return The JSON Web Token (JWT) as a string.
     */
    @JavascriptInterface
    fun getJwtToken(): String {
        Timber.tag("AppEvent").d("getJwtToken: $jtwToken")
        return jtwToken
    }

    @JavascriptInterface
    fun getFcmToken(): String {
        Timber.tag("AppEvent").d("getFcmToken: $fcmToken")
        return fcmToken
    }

    /**
     * Retrieves the JSON representation of the user information.
     *
     * @return The JSON representation of the user information as a string.
     */
    @JavascriptInterface
    fun getUserInfo(): String {
        Timber.tag("AppEvent").d("getUserInfo: $userJson")
        return userJson
    }

    /**
     * Reloads the WebView page associated with the specified [url].
     */
    @JavascriptInterface
    fun reloadPage() {
        Timber.tag("AppEvent").d("reloadPage")
        context.runOnUiThread {
            webView.loadUrl(url)
        }
    }

    @JavascriptInterface
    fun appEvent(event: String) {
        Timber.tag("AppEvent").d(event)
    }
}
