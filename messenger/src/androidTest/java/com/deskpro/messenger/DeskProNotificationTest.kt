package com.deskpro.messenger

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.deskpro.messenger.data.MessengerConfig
import com.deskpro.messenger.data.PushNotificationData
import com.deskpro.messenger.ui.MessengerWebViewActivity
import com.deskpro.messenger.util.Constants
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

        val dataValid = deskPro.isDeskProPushNotification(pushNotificationData.data)
        Assert.assertTrue("❌The notification was not valid!❌", dataValid)

        val success = deskPro.handlePushNotification(pushNotificationData)
        Assert.assertTrue("❌The notification was not sent successfully!❌", success)
    }

    @Test
    fun testInvalidContextPushNotification() {
        val deskProNotif = DeskPro(messengerConfig)

        val pushNotificationData = PushNotificationData(
            title = "Test Title",
            body = "Test Body",
            data = mapOf("issuer" to "some-messenger")
        )

        val success = deskProNotif.handlePushNotification(pushNotificationData)
        Assert.assertFalse("❌The notification is send and it should not be!❌", success)
    }

    @Test
    fun testInvalidPushNotificationData() {
        val pushNotificationData = PushNotificationData(
            title = "Test Title",
            body = "Test Body",
            data = mapOf("issuer" to "some-messenger")
        )

        val success = deskPro.handlePushNotification(pushNotificationData)
        Assert.assertFalse("❌The notification is send and it should not be!❌", success)
    }

    @Test
    fun testNotificationIntent() {
        val intent = MessengerWebViewActivity.notifIntent(context, "url", "appId")
        Assert.assertNotNull(intent)
        Assert.assertEquals("❌The intent url is not correct!❌", "url", intent.getStringExtra(Constants.WEB_URL))
        Assert.assertEquals("❌The intent appId is not correct!❌", "appId", intent.getStringExtra(Constants.APP_ID))
        Assert.assertTrue("❌The intent new message flag is not correct!❌", intent.getBooleanExtra(Constants.NEW_MESSAGE, false))
    }
}