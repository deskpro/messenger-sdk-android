package com.deskpro.messenger.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.result.ActivityResultLauncher

@SuppressLint("SetJavaScriptEnabled")
class DeskProChromeClient(private val fileUploadLauncher: ActivityResultLauncher<Intent>) : WebChromeClient() {

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
                fileUploadLauncher.launch(intent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }

    fun handleFileUpload(resultCode: Int, data: Intent?) {
        filePathCallback = if (resultCode == Activity.RESULT_OK) {
            val result: Array<Uri> = FileChooserParams.parseResult(resultCode, data)!!
            filePathCallback?.onReceiveValue(result)
            null
        } else {
            filePathCallback?.onReceiveValue(null)
            null
        }
    }
}
