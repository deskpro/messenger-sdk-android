package com.deskpro.messenger.data

import android.content.Context

internal interface Messenger {
    /**
     * Initializes the functionality of the application.
     *
     * This method should be called at the beginning of the application to set up
     * necessary configurations and prepare for the execution of other features.
     *
     * @param context The application context to be used for initialization.
     * @throws SomeInitializationException If an error occurs during initialization.
     */
    fun initialize(context: Context): Unit

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
     * Logs in a user and notifies the provided callback of the login status.
     *
     * This method attempts to log in the specified user and invokes the callback to
     * communicate the login result.
     *
     * @param user The user to be logged in.
     * @param deskProCallback The callback to receive the login status.
     *   - If the login is successful, [DeskProStatusCallback.onLoginSuccess] will be called.
     *   - If the login fails, [DeskProStatusCallback.onLoginFailure] will be called with the
     *     appropriate error message.
     * @see User
     * @see DeskProStatusCallback
     */
    fun loginUser(user: User, deskProCallback: DeskProStatusCallback)

    /**
     * Updates user information and notifies the provided callback of the update status.
     *
     * This method attempts to update the information of the specified user and invokes the
     * callback to communicate the update result.
     *
     * @param user The user with updated information.
     * @param deskProCallback The callback to receive the update status.
     *   - If the update is successful, [DeskProStatusCallback.onUpdateSuccess] will be called.
     *   - If the update fails, [DeskProStatusCallback.onUpdateFailure] will be called with the
     *     appropriate error message.
     * @see User
     * @see DeskProStatusCallback
     */
    fun updateUser(user: User, deskProCallback: DeskProStatusCallback)

    /**
     * Logs out the current user from the SDK session.
     *
     * This method performs a logout operation, ending the current user's session with the SDK.
     * After calling this method, the user will need to log in again to use the SDK features.
     *
     * @return `true` if the logout operation is successful; `false` otherwise.
     */
    fun logout(): Boolean

    /**
     * Sets the push registration token for the current user.
     *
     * This method associates the provided device token with the current user, enabling the SDK to
     * send push notifications to the specified device.
     *
     * @param deviceToken The push registration token obtained from the device.
     * @return `true` if the push registration token is successfully set; `false` otherwise.
     */
    fun setPushRegistrationToken(deviceToken: String): Boolean

    /**
     * Checks whether a push notification is related to the DeskPro SDK.
     *
     * This method examines the provided push notification data to determine whether it is
     * intended for the DeskPro SDK.
     *
     * @param pushNotification The push notification data to be analyzed.
     * @return `true` if the push notification is related to DeskPro; `false` otherwise.
     */
    fun isDeskProPushNotification(pushNotification: PushNotificationData): Boolean

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
     * @throws PushNotificationHandlingException If an error occurs while handling the DeskPro push notification.
     * @see isDeskProPushNotification
     */
    fun handlePushNotification(pushNotification: PushNotificationData): Unit

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
}

class PushNotificationData
class DeskProError

interface DeskProStatusCallback {
    fun onFailure(deskProError: DeskProError)
    fun onSuccess()
}
