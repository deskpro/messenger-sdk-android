package com.deskpro.messenger.data

/**
 * Configuration data class for initializing the Messenger feature in the application.
 *
 * This data class holds configuration parameters required for setting up and initializing the Messenger
 * feature within the application. The configuration includes the base URL of the Messenger service,
 * the application ID, and the application key.
 *
 * Example Usage:
 * ```kotlin
 * val messengerConfig = MessengerConfig(appUrl = "https://messenger.example.com", appId = "yourAppId", appKey = "yourAppKey")
 * ```
 *
 * @property appUrl The base URL of the Messenger service.
 * @property appId The unique identifier for the application using the Messenger feature.
 * @property appKey The secret key for authenticating the application with the Messenger service.
 */
data class MessengerConfig(
    val appUrl: String,
    val appId: String,
    val appKey: String? = null
)
