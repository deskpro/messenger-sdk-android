package com.deskpro.messenger

import android.app.Application
import android.content.Context
import com.deskpro.messenger.util.Prefs

internal class App : Application() {

    companion object {
        var appContext: Context? = null
        var prefs: Prefs? = null
    }
}
