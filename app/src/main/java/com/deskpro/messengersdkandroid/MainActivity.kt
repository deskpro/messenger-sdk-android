package com.deskpro.messengersdkandroid

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.deskpro.messenger.DeskPro
import com.deskpro.messenger.data.MessengerConfig
import com.deskpro.messenger.data.PushNotificationData
import com.deskpro.messenger.data.User
import com.deskpro.messengersdkandroid.databinding.ActivityMainBinding

/**
 * Main activity hosting the DeskPro Messenger integration demonstration.
 *
 * The `MainActivity` class demonstrates the integration of DeskPro Messenger into an Android application.
 * It initializes the DeskPro Messenger instance, handles button clicks, and showcases basic functionality.
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private var messenger: DeskPro? = null
    private var appUrl = "https://dev-pr-12730.earthly.deskprodemo.com/deskpro-messenger/deskpro/1/d"
    private var jwtToken = ""
    private var user: User? = null
    private var userInfo = "{\n" +
            "  \"name\": \"John Doe\",\n" +
            "  \"first_name\": \"John\",\n" +
            "  \"last_name\": \"Doe\",\n" +
            "  \"email\": \"john.doe@mail.com\"\n" +
            "}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initListeners()

        binding.etUrl.setText(appUrl)
        binding.etUserInfo.setText(userInfo)
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                btnUrl -> {
                    appUrl = etUrl.text.toString()
                }

                btnJWT -> {
                    jwtToken = etJWT.text.toString()
                    messenger?.authorizeUser(jwtToken)
                }

                btnUserInfo -> {
                    userInfo = etUserInfo.text.toString()
                    //TODO: parse userInfo string and set user info
                    user = User()
                }

                btnNotifications -> {
                    //TODO check for permission
                    //messenger?.handlePushNotification(PushNotificationData())
                }

                btnOpenMessenger -> {
                    messenger = DeskPro(MessengerConfig(appUrl = appUrl, ""))
                    messenger?.initialize(applicationContext)
                    messenger?.present()?.show()
                    user?.let {
                        messenger?.setUserInfo(it)
                    }
                }

                btnOpenMessengerNewChat -> {
                    messenger = DeskPro(MessengerConfig(appUrl = appUrl, ""))
                    messenger?.initialize(applicationContext)
                    messenger?.present()?.show()
                    user?.let {
                        messenger?.setUserInfo(it)
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.btnUrl.setOnClickListener(this)
        binding.btnJWT.setOnClickListener(this)
        binding.btnUserInfo.setOnClickListener(this)
        binding.btnNotifications.setOnClickListener(this)
        binding.btnOpenMessenger.setOnClickListener(this)
        binding.btnOpenMessengerNewChat.setOnClickListener(this)
    }
}
