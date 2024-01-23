package com.deskpro.messenger.data

import android.util.Log
import com.deskpro.messenger.App
import com.deskpro.messenger.ui.MessengerWebViewActivity

class PresentBuilder(url: String, private val appId: String) {
    private var path = StringBuilder().append(url)

    fun chatHistory(chatId: Int): PresentBuilder {
        path.append("/chat_history/$chatId")
        return this
    }

    fun article(articleId: Int): PresentBuilder {
        path.append("/article/$articleId")
        return this
    }

    fun comments(): PresentBuilder {
        path.append("/comments")
        return this
    }

    fun show() {
        if (App.appContext == null) {
            Log.d("PresentBuilder", "Context is null")
            return
        }
        App.appContext?.let {
            MessengerWebViewActivity.start(context = it, path = path.toString(), appId = appId)
        }
    }
}
