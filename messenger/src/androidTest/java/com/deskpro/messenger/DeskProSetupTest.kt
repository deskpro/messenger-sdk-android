package com.deskpro.messenger

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.deskpro.messenger.data.MessengerConfig
import com.deskpro.messenger.data.PresentBuilder
import com.deskpro.messenger.data.User
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
open class DeskProSetupTest {

    private lateinit var context: Context
    private lateinit var messengerConfig: MessengerConfig
    private lateinit var deskPro: DeskPro
    private val appUrl = "https://dev-pr-12730.earthly.deskprodemo.com/deskpro-messenger/deskpro/1/d"

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        messengerConfig = MessengerConfig(
            appUrl = appUrl,
            appId = "",
        )
        deskPro = DeskPro(context, messengerConfig)
    }

    @Test
    fun testSetAndGetUserInfo() {
        val user = User(
            name = "John Doe",
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@email.com"
        )
        deskPro.setUserInfo(user)

        Assert.assertEquals(
            "❌The users do not match.❌",
            user.toJson(),
            deskPro.getUserInfo()?.toJson()
        )
    }

    @Test
    fun testSetAndGetJwtToken() {
        val jwtToken = "test_token"
        deskPro.authorizeUser(jwtToken)

        Assert.assertEquals("❌The tokens do not match.❌", jwtToken, deskPro.getJwtToken())
    }

    @Test
    fun testSetAndGetPushRegistrationToken() {
        val deviceToken = "test_device_token"
        deskPro.setPushRegistrationToken(deviceToken)

        Assert.assertEquals(
            "❌The tokens do not match.❌",
            deviceToken,
            deskPro.getPushRegistrationToken()
        )
    }

    @Test
    fun testConfigClearData() {
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

        Assert.assertNull("❌The user info is not nil.❌", deskPro.getUserInfo())
        Assert.assertNull("❌The jwt token is not nil.❌", deskPro.getJwtToken())
        Assert.assertNull("❌The device token is not nil.❌", deskPro.getPushRegistrationToken())
    }

    @Test
    fun testSetAndGetMultipleDeskProInstances() {
        //set first instance config data
        val messengerConfig1 = MessengerConfig(
            appUrl = appUrl,
            appId = "1",
        )
        val user1 = User(
            name = "John Doe 1",
            firstName = "John 1",
            lastName = "Doe 1",
            email = "john.doe1@email.com"
        )
        val jwtToken1 = "test_token_1"
        val fcmToken1 = "test_device_token_1"

        val deskPro1 = DeskPro(context, messengerConfig1)

        deskPro1.setUserInfo(user1)
        deskPro1.authorizeUser(jwtToken1)
        deskPro1.setPushRegistrationToken(fcmToken1)

        //set second instance config data
        val messengerConfig2 = MessengerConfig(
            appUrl = appUrl,
            appId = "2",
        )
        val user2 = User(
            name = "John Doe 2",
            firstName = "John 2",
            lastName = "Doe 2",
            email = "john.doe2@email.com"
        )
        val jwtToken2 = "test_token_2"
        val fcmToken2 = "test_device_token_2"

        val deskPro2 = DeskPro(context, messengerConfig2)

        deskPro2.setUserInfo(user2)
        deskPro2.authorizeUser(jwtToken2)
        deskPro2.setPushRegistrationToken(fcmToken2)

        Assert.assertEquals(
            "❌The users don't match.❌",
            user1.toJson(),
            deskPro1.getUserInfo()?.toJson()
        )
        Assert.assertEquals(
            "❌The users don't match.❌",
            user2.toJson(),
            deskPro2.getUserInfo()?.toJson()
        )


        Assert.assertEquals("❌The jwt tokens don't match.❌", jwtToken1, deskPro1.getJwtToken())
        Assert.assertEquals("❌The jwt tokens don't match.❌", jwtToken2, deskPro2.getJwtToken())

        Assert.assertEquals(
            "❌The device tokens don't match.❌",
            fcmToken1,
            deskPro1.getPushRegistrationToken()
        )
        Assert.assertEquals(
            "❌The device tokens don't match.❌",
            fcmToken2,
            deskPro2.getPushRegistrationToken()
        )
    }

    @Test
    fun testPresentBuilder() {
        val presentBuilder = PresentBuilder(
            context,
            appUrl,
            "1"
        )

        Assert.assertEquals(
            "❌The paths do not match.❌",
            appUrl,
            presentBuilder.getPath()
        )

        presentBuilder.chatHistory(1)

        Assert.assertEquals(
            "❌The paths do not match.❌",
            "$appUrl/chat_history/1",
            presentBuilder.getPath()
        )

        presentBuilder.article(1)

        Assert.assertEquals(
            "❌The paths do not match.❌",
            "$appUrl/chat_history/1/article/1",
            presentBuilder.getPath()
        )

        presentBuilder.comments()

        Assert.assertEquals(
            "❌The paths do not match.❌",
            "$appUrl/chat_history/1/article/1/comments",
            presentBuilder.getPath()
        )
    }
}
