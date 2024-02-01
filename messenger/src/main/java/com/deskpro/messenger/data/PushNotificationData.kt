package com.deskpro.messenger.data

/**
 * Data class representing the payload of a push notification.
 *
 * The `PushNotificationData` class encapsulates the information included in a push notification.
 */
data class PushNotificationData(
    //val topic: String? = null,
    val title: String,
    val body: String,
    val data: Map<String, String>
) {
    fun isPushNotificationDataValid(): Boolean {
        val hasIssuer =  data.containsKey("issuer") && data.containsValue("deskpro-messenger")
        val hasCategory =  data.containsKey("category") && data.containsValue("new-message")

        return hasCategory && hasIssuer
    }

    fun getNotificationNumber(): Int? {
        return data["notif_number"]?.toIntOrNull()
    }
}