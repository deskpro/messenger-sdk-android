package com.deskpro.messenger.ui

import android.app.Activity
import android.webkit.JavascriptInterface

class MessengerWebInterface(private val context: Activity) {
    @JavascriptInterface
    fun close() {
        context.finish()
    }
}
