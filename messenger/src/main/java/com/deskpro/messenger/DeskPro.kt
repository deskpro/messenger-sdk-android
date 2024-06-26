package com.deskpro.messenger

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.deskpro.messenger.data.LogCollector
import com.deskpro.messenger.data.Messenger
import com.deskpro.messenger.data.MessengerConfig
import com.deskpro.messenger.data.PresentBuilder
import com.deskpro.messenger.data.PushNotificationData
import com.deskpro.messenger.data.User
import com.deskpro.messenger.util.Constants
import com.deskpro.messenger.util.NotificationHelper
import com.deskpro.messenger.util.Prefs
import org.json.JSONObject
import timber.log.Timber
import java.net.URLEncoder

/**
 * Implementation of the [Messenger] interface for interacting with DeskPro messaging functionality.
 *
 * The `DeskPro` class provides methods for initializing the Messenger, managing user information,
 * handling push notifications, and presenting the DeskPro messaging interface.
 *
 * @param appContext The application context to be used for initializing the DeskPro.
 * @param messengerConfig The configuration object containing settings for the DeskPro Messenger.
 */
class DeskPro(private val appContext: Context, private val messengerConfig: MessengerConfig) : Messenger {

    /**
     * Preferences manager for storing user-related information.
     */
    private var prefs: Prefs = Prefs(appContext, messengerConfig.appId)

    /**
     * Notification helper class for handling push notifications.
     */
    private var notificationHelper: NotificationHelper = NotificationHelper(appContext)

    init {
        Timber.tag(TAG).d("Initialized")
    }

    /**
     * Performs a test operation and returns a result as a String.
     *
     * The [test] method is intended to simulate a test scenario and provide a String
     * result based on the outcome of the test.
     *
     * @return A String representing the result of the test operation.
     */
    override fun test(): String {
        return "Hello world from Messenger!"
    }

    /**
     * Sets user information for the application.
     *
     * This function updates or initializes the user information for the application.
     * Call this method when the user logs in or when user details need to be refreshed.
     *
     * @param user The [User] object containing the user information.
     */
    override fun setUserInfo(user: User) {
        //TODO Not yet implemented
        prefs.setUserInfo(user)
    }

    /**
     * Getter method for user info, should only be used for testing.
     */
    internal fun getUserInfo(): User? {
        return prefs.getUserInfo()
    }

    /**
     * Sets a user JWT token that enables Messenger to treat this user as a logged-in user.
     *
     * @param userJwt The JSON Web Token (JWT) representing the user's authentication.
     * @return `true` if the token is successfully saved, `false` otherwise.
     */
    override fun authorizeUser(userJwt: String): Boolean {
        prefs.setJwtToken(userJwt)
        return true
    }

    /**
     * Getter method for JWT token, should only be used for testing.
     */
    internal fun getJwtToken(): String? {
        return prefs.getJwtToken()
    }

    /**
     * Logs out the current user from the SDK session.
     *
     * This method performs a logout operation, ending the current user's session with the SDK.
     * After calling this method, the user will need to log in again to use the SDK features.
     *
     * @return `true` if the logout operation is successful; `false` otherwise.
     */
    override fun forgetUser(): Boolean {
        //TODO Not yet implemented
        prefs.clear()
        return true
    }

    /**
     * Sets the push registration token for the current user.
     *
     * This method associates the provided device token with the current user, enabling the SDK to
     * send push notifications to the specified device.
     *
     * @param token The push registration token obtained from the device.
     * @return `true` if the push registration token is successfully set; `false` otherwise.
     */
    override fun setPushRegistrationToken(token: String): Boolean {
        prefs.setFCMToken(token)
        return true
    }

    /**
     * Getter method for FCM token, should only be used for testing.
     */
    internal fun getPushRegistrationToken(): String? {
        return prefs.getFCMToken()
    }

    /**
     * Checks whether a push notification is related to the DeskPro SDK.
     *
     * This method examines the provided push notification data to determine whether it is
     * intended for the DeskPro SDK.
     *
     * @param pushNotification The push notification data to be analyzed.
     * @return `true` if the push notification is related to DeskPro; `false` otherwise.
     */
    override fun isDeskProPushNotification(data: Map<String, String>): Boolean {
        return data.containsKey("issuer") && data.containsValue("deskpro-messenger")
    }

    /**
     * Handles the incoming push notification data if it is related to DeskPro.
     *
     * We recommend calling [isDeskProPushNotification] before invoking this method to check
     * if the push notification is related to DeskPro. If the check is successful, this method
     * processes the provided push notification data and takes appropriate actions based on its content.
     *
     * If the push notification is not related to DeskPro, it is advisable to handle it appropriately.
     *
     * @param pushNotification The push notification data to be handled.
     * @see isDeskProPushNotification
     * @return `true` if the push notification is successfully handled; `false` otherwise.
     */
    override fun handlePushNotification(pushNotification: PushNotificationData): Boolean {
        if (!isDeskProPushNotification(pushNotification.data)) {
            Timber.tag(TAG).d("Not DeskPro push notification")
            return false
        }

        if (ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.tag(TAG).d("Notification permission not granted")
            return false
        }

        notificationHelper.showNotification(
            title = pushNotification.title,
            body = pushNotification.body,
            icon = messengerConfig.appIcon,
            url = messengerConfig.appUrl,
            appId = messengerConfig.appId
        )

        return true
    }

    /**
     * Provides a [PresentBuilder] for constructing presentation paths within the application.
     *
     * This function returns a [PresentBuilder] instance, allowing the construction of presentation paths
     * for various features within the application. The resulting paths can be used to navigate or present
     * specific content.
     *
     * Example Usage:
     * ```
     * val presentView = present().article(123).comments().show()
     * ```
     *
     * @return A [PresentBuilder] instance to start building presentation paths.
     * @see PresentBuilder
     */
    override fun present(): PresentBuilder {
        val url = with(messengerConfig) {
            buildUrl(this.appUrl, this.appId)
        }
        return PresentBuilder(appContext, url, messengerConfig.appId)
    }

    /**
     * Closes the chat view presented by the DeskPro SDK.
     *
     * This method closes the currently displayed chat view, terminating the user's interaction
     * with the DeskPro content.
     */
    override fun close() {
        //TODO Not yet implemented
        Timber.tag(TAG).d(prefs.getUserInfo()?.name ?: "")
    }

    /**
     * Retrieves the number of unread conversations in the user's inbox.
     *
     * This method returns the count of conversations marked as unread in the user's inbox.
     *
     * @return The number of unread conversations in the inbox.
     */
    override fun getUnreadConversationCount(): Int {
        //TODO Not yet implemented
        return 0
    }

    /**
     * Enables logging for the DeskPro SDK.
     *
     * This method turns on logging for the DeskPro SDK, allowing detailed information to be
     * logged for debugging and troubleshooting purposes.
     */
    override fun enableLogging() {
        LogCollector.plantTree()
    }

    override fun getLogs(): List<String> {
        return LogCollector.getLogs()
    }

    companion object {
        private const val TAG = "DeskPro"
    }

    private fun buildUrl(baseUrl: String, appId: String): String {
        val encodedPlatform = URLEncoder.encode(JSONObject(mapOf("platform" to Constants.ANDROID)).toString(), "UTF-8")
        val formattedBaseUrl = baseUrl.trimEnd('/')
        val formattedAppId = "/" + appId.trim('/')

        return "$formattedBaseUrl/$formattedAppId/$encodedPlatform"
    }
}
