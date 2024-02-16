package com.deskpro.messengersdkandroid

import android.util.Log
import com.deskpro.messenger.data.PushNotificationData
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class DeskProFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "DeskPro"

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.from)

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification: " + remoteMessage.notification!!.body)
            val title = remoteMessage.notification!!.title
            val message = remoteMessage.notification!!.body
            if (!title.isNullOrEmpty() && !message.isNullOrEmpty()) {
                if (App.messenger?.isDeskProPushNotification(remoteMessage.data) == true) {
                    //Send notification to the deskpro messenger
                    App.messenger?.handlePushNotification(PushNotificationData(title, message, remoteMessage.data))
                } else {
                    //TODO Send your own notification
                }
            }
        }
    }

    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }
}

