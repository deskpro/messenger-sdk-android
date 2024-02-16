package com.deskpro.messenger.data

internal interface Messenger {

    /**
     * Performs a test operation and returns a result as a String.
     *
     * The [test] method is intended to simulate a test scenario and provide a String
     * result based on the outcome of the test.
     *
     * @return A String representing the result of the test operation.
     */
    fun test(): String

    /**
     * Sets user information for the application.
     *
     * This function updates or initializes the user information for the application.
     * Call this method when the user logs in or when user details need to be refreshed.
     *
     * @param user The [User] object containing the user information.
     */
    fun setUserInfo(user: User): Unit

    /**
     * Sets a user JWT token that enables Messenger to treat this user as a logged-in user.
     *
     * @param userJwt The JSON Web Token (JWT) representing the user's authentication.
     * @return `true` if the token is successfully saved, `false` otherwise.
     */
    fun authorizeUser(userJwt: String): Boolean

    /**
     * Logs out the current user from the SDK session.
     *
     * This method performs a logout operation, ending the current user's session with the SDK.
     * After calling this method, the user will need to log in again to use the SDK features.
     *
     * @return `true` if the logout operation is successful; `false` otherwise.
     */
    fun forgetUser(): Boolean

    /**
     * Sets the push registration token for the current user.
     *
     * This method associates the provided device token with the current user, enabling the SDK to
     * send push notifications to the specified device.
     *
     * @param token The push registration token obtained from the device.
     * @return `true` if the push registration token is successfully set; `false` otherwise.
     */
    fun setPushRegistrationToken(token: String): Boolean

    /**
     * Checks whether a push notification is related to the DeskPro SDK.
     *
     * This method examines the provided push notification data to determine whether it is
     * intended for the DeskPro SDK.
     *
     * @param pushNotification The push notification data to be analyzed.
     * @return `true` if the push notification is related to DeskPro; `false` otherwise.
     */
    fun isDeskProPushNotification(data: Map<String, String>): Boolean

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
    fun handlePushNotification(pushNotification: PushNotificationData): Boolean

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
    fun present(): PresentBuilder

    /**
     * Closes the chat view presented by the DeskPro SDK.
     *
     * This method closes the currently displayed chat view, terminating the user's interaction
     * with the DeskPro content.
     */
    fun close(): Unit

    /**
     * Retrieves the number of unread conversations in the user's inbox.
     *
     * This method returns the count of conversations marked as unread in the user's inbox.
     *
     * @return The number of unread conversations in the inbox.
     */
    fun getUnreadConversationCount(): Int

    /**
     * Enables logging for the DeskPro SDK.
     *
     * This method turns on logging for the DeskPro SDK, allowing detailed information to be
     * logged for debugging and troubleshooting purposes.
     */
    fun enableLogging(): Unit

    fun getLogs(): List<String>
}
