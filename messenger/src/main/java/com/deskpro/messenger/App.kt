package com.deskpro.messenger

import android.app.Application
import android.content.Context

internal class App : Application() {

    companion object {
        var appContext: Context? = null
    }
}
