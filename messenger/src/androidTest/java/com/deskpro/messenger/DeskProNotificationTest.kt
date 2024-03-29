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
    private lateinit var validPushData: PushNotificationData
    private lateinit var invalidPushData: PushNotificationData

    @get:Rule
    val permissionRule: GrantPermissionApiRule = GrantPermissionApiRule.grant(
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU,
        Manifest.permission.POST_NOTIFICATIONS
    )

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        messengerConfig = MessengerConfig(
            appUrl = "test/url/data",
            appId = "",
        )
        deskPro = DeskPro(context, messengerConfig)

        validPushData = PushNotificationData(
            title = "Test Title",
            body = "Test Body",
            data = mapOf("issuer" to "deskpro-messenger")
        )

        invalidPushData = PushNotificationData(
            title = "Test Title",
            body = "Test Body",
            data = mapOf("issuer" to "some-messenger")
        )
    }

    @Test
    fun testIsDeskProPushNotificationWithValidData() {
        val result = deskPro.isDeskProPushNotification(validPushData.data)
        Assert.assertTrue("❌The notification was not valid!❌", result)
    }

    @Test
    fun testIsDeskProPushNotificationWithInvalidData() {
        val result = deskPro.isDeskProPushNotification(invalidPushData.data)
        Assert.assertFalse("❌The notification is valid!❌", result)
    }

    @Test
    fun testShowPushNotificationWithValidData() {
        val success = deskPro.handlePushNotification(validPushData)
        Assert.assertTrue("❌The notification was not sent successfully!❌", success)
    }

    @Test
    fun testShowPushNotificationWithInvalidData() {
        val success = deskPro.handlePushNotification(invalidPushData)
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
