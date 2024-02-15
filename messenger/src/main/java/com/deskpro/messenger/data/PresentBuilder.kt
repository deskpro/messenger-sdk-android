package com.deskpro.messenger.data

import android.util.Log
import com.deskpro.messenger.DeskProApp
import com.deskpro.messenger.ui.MessengerWebViewActivity

/**
 * Builder class for constructing URLs and presenting content in a WebView.
 *
 * This class allows the construction of URLs by chaining various methods to build a specific path.
 * The final constructed path can then be presented in a WebView using the [show] method.
 *
 * @param url The base URL for constructing the path.
 * @param appId The application ID used for presentation.
 */
class PresentBuilder(url: String, private val appId: String) {
    /**
     * Represents the constructed path for the URL.
     */
    private var path = StringBuilder().append(url)

    /**
     * Appends the chat history path to the constructed URL path.
     *
     * @param chatId The identifier of the chat for which the history is requested.
     * @return The [PresentBuilder] instance for method chaining.
     */
    fun chatHistory(chatId: Int): PresentBuilder {
        path.append("/chat_history/$chatId")
        return this
    }

    /**
     * Appends the article path to the constructed URL path.
     *
     * @param articleId The identifier of the article to be presented.
     * @return The [PresentBuilder] instance for method chaining.
     */
    fun article(articleId: Int): PresentBuilder {
        path.append("/article/$articleId")
        return this
    }

    /**
     * Appends the comments path to the constructed URL path.
     *
     * @return The [PresentBuilder] instance for method chaining.
     */
    fun comments(): PresentBuilder {
        path.append("/comments")
        return this
    }

    internal fun getPath(): String {
        return path.toString()
    }

    /**
     * Shows the WebView with the constructed path.
     *
     * If the application context is null, a message is logged, and the method returns early.
     * Otherwise, the WebView is started with the constructed path and application ID.
     */
    fun show() {
        if (DeskProApp.appContext == null) {
            Log.d("PresentBuilder", "Context is null")
            return
        }
        DeskProApp.appContext?.let {
            MessengerWebViewActivity.start(context = it, path = path.toString(), appId = appId)
        }
    }
}
