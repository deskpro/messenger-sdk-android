package com.deskpro.messenger.data

/**
 * Data class representing the payload of a push notification.
 *
 * The `PushNotificationData` class encapsulates the information included in a push notification.
 */
data class PushNotificationData(
    val title: String,
    val body: String,
    val data: Map<String, String>
)
