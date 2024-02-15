package com.deskpro.messenger

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.deskpro.messenger.data.MessengerConfig
import com.deskpro.messenger.data.PushNotificationData
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
open class DeskProNotificationTest {

    private lateinit var context: Context
    private lateinit var messengerConfig: MessengerConfig
    private lateinit var deskPro: DeskPro

    @get:Rule
    val permissionRule: GrantPermissionApiRule = GrantPermissionApiRule.grant(
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU,
        Manifest.permission.POST_NOTIFICATIONS
    )

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        messengerConfig = MessengerConfig(
            appUrl = "https://dev-pr-13019.earthly.deskprodemo.com/deskpro-messenger/deskpro/1/d",
            appId = "",
        )
        deskPro = DeskPro(messengerConfig)
        deskPro.initialize(context)
    }

    @Test
    fun testValidPushNotification() {
        deskPro.setPushRegistrationToken("123321")

        val pushNotificationData = PushNotificationData(
            title = "Test Title",
            body = "Test Body",
            data = mapOf("issuer" to "deskpro-messenger")
        )

        val success = deskPro.handlePushNotification(pushNotificationData)
        Assert.assertTrue(success)
    }

    @Test
    fun testInvalidPushNotification() {
        val pushNotificationData = PushNotificationData(
            title = "Test Title",
            body = "Test Body",
            data = mapOf("issuer" to "some-messenger")
        )

        val success = deskPro.handlePushNotification(pushNotificationData)
        Assert.assertFalse(success)
    }
}