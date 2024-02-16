package com.deskpro.messengersdkandroid

import android.app.Application
import com.deskpro.messenger.DeskPro

class App : Application() {

    companion object {
        var messenger: DeskPro? = null
    }
}