package com.deskpro.messenger

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.deskpro.messenger.data.MessengerConfig
import com.deskpro.messenger.data.User
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
open class DeskProSetupTest {

    private lateinit var context: Context
    private lateinit var messengerConfig: MessengerConfig
    private lateinit var deskPro: DeskPro

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

    @After
    fun tearDown() {
    }

    @Test
    fun testUserInfo() {
        val user = User(
            name = "John Doe",
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@email.com"
        )
        deskPro.setUserInfo(user)

        Assert.assertEquals("❌The users do not match.❌", user.toJson(), deskPro.getUserInfo()?.toJson())
    }

    @Test
    fun testJwtToken() {
        val jwtToken = "test_token"
        deskPro.authorizeUser(jwtToken)

        Assert.assertEquals("❌The tokens do not match.❌", jwtToken, deskPro.getJwtToken())
    }

    @Test
    fun testPushRegistrationToken() {
        val deviceToken = "test_device_token"
        deskPro.setPushRegistrationToken(deviceToken)

        Assert.assertEquals("❌The tokens do not match.❌", deviceToken, deskPro.getPushRegistrationToken())
    }

    @Test
    fun testClearData() {
        deskPro.setUserInfo(
            User(
                name = "John Doe",
                firstName = "John",
                lastName = "Doe",
                email = "john.doe@email.com"
            )
        )
        deskPro.authorizeUser("test_token")
        deskPro.setPushRegistrationToken("test_device_token")

        deskPro.forgetUser()

        Assert.assertNull("❌The user info is not nil.❌", deskPro.getUserInfo()?.toJson())
        Assert.assertNull("❌The jwt token is not nil.❌", deskPro.getJwtToken())
        Assert.assertNull("❌The device token is not nil.❌", deskPro.getPushRegistrationToken())
    }

}