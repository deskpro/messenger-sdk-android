package com.deskpro.messenger

import android.content.Context
import android.util.Log
import com.deskpro.messenger.data.DeskProStatusCallback
import com.deskpro.messenger.data.Messenger
import com.deskpro.messenger.data.MessengerConfig
import com.deskpro.messenger.data.PresentBuilder
import com.deskpro.messenger.data.PushNotificationData
import com.deskpro.messenger.data.User
import com.deskpro.messenger.util.Prefs

class DeskPro(private val messengerConfig: MessengerConfig) : Messenger {
    override fun initialize(context: Context) {
        App.appContext = context
        App.prefs = Prefs(context, messengerConfig.appId)
        Log.d(TAG, "Initialized")
    }

    override fun test(): String {
        return "Hello world from Messenger!"
    }

    override fun loginUser(user: User, deskProCallback: DeskProStatusCallback) {
        //TODO Not yet implemented
        App.prefs?.setUserInfo(user)
    }

    override fun updateUser(user: User, deskProCallback: DeskProStatusCallback) {
        //TODO Not yet implemented
        App.prefs?.setUserInfo(user)
    }

    override fun logout(): Boolean {
        //TODO Not yet implemented
        App.prefs?.clear()
        return true
    }

    override fun setPushRegistrationToken(deviceToken: String): Boolean {
        //TODO Not yet implemented
        App.prefs?.setJwtToken(deviceToken)
        return true
    }

    override fun isDeskProPushNotification(pushNotification: PushNotificationData): Boolean {
        //TODO Not yet implemented
        return true
    }

    override fun handlePushNotification(pushNotification: PushNotificationData) {
        //TODO Not yet implemented
    }

    override fun present(): PresentBuilder {
        val url = with(messengerConfig) {
            this.appUrl.plus(this.appId)
        }
        return PresentBuilder(url)
    }

    override fun close() {
        //TODO Not yet implemented
        App.prefs?.clear()
    }

    override fun getUnreadConversationCount(): Int {
        //TODO Not yet implemented
        return 0
    }

    override fun enableLogging() {
        //TODO Not yet implemented
    }

    companion object {
        private const val TAG = "DeskPro"
    }
}
