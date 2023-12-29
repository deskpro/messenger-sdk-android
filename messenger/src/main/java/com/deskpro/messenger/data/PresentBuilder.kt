package com.deskpro.messenger.data

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
}
