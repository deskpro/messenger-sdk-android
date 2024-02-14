package com.deskpro.messenger.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView

@SuppressLint("SetJavaScriptEnabled")
class CustomWebChromeClient : WebChromeClient() {

    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    private var fileChooserParams: FileChooserParams? = null

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        this.filePathCallback?.onReceiveValue(null) // Cancel any existing callback

        this.filePathCallback = filePathCallback
        this.fileChooserParams = fileChooserParams

        val intent = fileChooserParams?.createIntent()
        try {
            if (intent != null) {
                (webView?.context as? MessengerWebViewActivity)?.startActivityForResult(
                    intent,
                    REQUEST_FILE_UPLOAD
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_FILE_UPLOAD) {
            handleFileUpload(resultCode, data)
        }
    }

    private fun handleFileUpload(resultCode: Int, data: Intent?) {
        filePathCallback = if (resultCode == Activity.RESULT_OK) {

            val result: Array<Uri> = FileChooserParams.parseResult(resultCode, data)!!

            filePathCallback?.onReceiveValue(result)
            null
        } else {
            filePathCallback?.onReceiveValue(null)
            null
        }
    }

    companion object {
        private const val REQUEST_FILE_UPLOAD = 1
    }
}
