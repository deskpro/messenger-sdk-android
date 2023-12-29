package com.deskpro.messenger.data

import android.content.Intent
import com.deskpro.messenger.App
import com.deskpro.messenger.ui.MessengerWebViewActivity
import com.deskpro.messenger.util.Constants

class PresentBuilder(url: String) {
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
        App.appContext?.startActivity(
            Intent(App.appContext, MessengerWebViewActivity::class.java)
                .putExtra(Constants.WEB_URL, path.toString())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
