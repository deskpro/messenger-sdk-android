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
    fun testMultipleDeskProInstances() {
        val messengerConfig1 = MessengerConfig(
            appUrl = "https://dev-pr-13019.earthly.deskprodemo.com/deskpro-messenger/deskpro/1/d",
            appId = "1",
        )
        val deskPro1 = DeskPro(messengerConfig1)
        deskPro1.initialize(context)

        deskPro1.setUserInfo(
            User(
                name = "John Doe 1",
                firstName = "John 1",
                lastName = "Doe 1",
                email = "john.doe1@email.com"
            )
        )
        deskPro1.authorizeUser("test_token_1")
        deskPro1.setPushRegistrationToken("test_device_token_1")

        val messengerConfig2 = MessengerConfig(
            appUrl = "https://dev-pr-13019.earthly.deskprodemo.com/deskpro-messenger/deskpro/1/d",
            appId = "2",
        )
        val deskPro2 = DeskPro(messengerConfig2)
        deskPro2.initialize(context)

        deskPro2.setUserInfo(
            User(
                name = "John Doe 2",
                firstName = "John 21",
                lastName = "Doe 2",
                email = "john.doe2@email.com"
            )
        )
        deskPro2.authorizeUser("test_token_2")
        deskPro2.setPushRegistrationToken("test_device_token_2")

        Assert.assertNotEquals("❌The users match.❌", deskPro1.getUserInfo()?.toJson(), deskPro2.getUserInfo()?.toJson())
        Assert.assertNotEquals("❌The jwt tokens match.❌", deskPro1.getJwtToken(), deskPro2.getJwtToken())
        Assert.assertNotEquals("❌The device tokens match.❌", deskPro1.getPushRegistrationToken(), deskPro2.getPushRegistrationToken())
    }

    @Test
    fun testPresentBuilder() {
        val presentBuilder = PresentBuilder("https://dev-pr-13019.earthly.deskprodemo.com/deskpro-messenger/deskpro/1/d", "1")
        presentBuilder.chatHistory(1)
        presentBuilder.article(1)
        presentBuilder.comments()

        Assert.assertEquals(
            "❌The paths do not match.❌",
            "https://dev-pr-13019.earthly.deskprodemo.com/deskpro-messenger/deskpro/1/d/chat_history/1/article/1/comments",
            presentBuilder.getPath()
        )
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