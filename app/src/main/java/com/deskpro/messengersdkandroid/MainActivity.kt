package com.deskpro.messengersdkandroid

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.deskpro.messenger.DeskPro
import com.deskpro.messenger.data.MessengerConfig
import com.deskpro.messenger.data.PushNotificationData
import com.deskpro.messengersdkandroid.databinding.ActivityMainBinding
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Main activity hosting the DeskPro Messenger integration demonstration.
 *
 * The `MainActivity` class demonstrates the integration of DeskPro Messenger into an Android application.
 * It initializes the DeskPro Messenger instance, handles button clicks, and showcases basic functionality.
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "DeskProSample"

    private lateinit var binding: ActivityMainBinding
    private lateinit var messenger: DeskPro
    private val messengerConfig =
        MessengerConfig(
            "https://dev-pr-12730.earthly.deskprodemo.com/deskpro-messenger/deskpro/1/d",
            "",
            appIcon = R.drawable.ic_launcher_foreground
        )
    private var fcmToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        /**
         * New messanger instance creation
         */
        messenger = DeskPro(messengerConfig)

        messenger.initialize(applicationContext)

        initListeners()

        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { result ->
                fcmToken = result ?: ""
                Log.d(TAG, "Fetched FCM token successfully")
            }.addOnFailureListener { exception ->
                Log.e(TAG, "Fetching FCM registration token failed: ${exception.message}")
            }
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                btnTest -> {
                    onTestPressed()
                }

                btnOpenMessenger -> {
                    onOpenMessengerPressed()
                }
            }
        }
    }

    private fun initListeners() {
        binding.btnTest.setOnClickListener(this)
        binding.btnOpenMessenger.setOnClickListener(this)
    }

    private fun onTestPressed() {
        val result = messenger.test()
        binding.tvMessage.text = result
    }

    private fun onOpenMessengerPressed() {
        messenger.present().show()
    }

    private fun onNotificationEnablePressed() {
        messenger.setPushRegistrationToken(fcmToken)

        messenger.handlePushNotification(
            PushNotificationData(
                "Test",
                "message of the test notification",
                mapOf("issuer" to "DeskPro", "category" to "new-message")
            )
        )
    }

}
