package com.deskpro.messenger.util

import android.content.Context
import android.content.SharedPreferences
import com.deskpro.messenger.data.User
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * SharedPreferences utility class for managing user information and JWT tokens in the DeskPro Messenger module.
 *
 * The `Prefs` class provides methods for retrieving, storing, and clearing user information and JWT tokens
 * using the Android SharedPreferences API. It uses Kotlin Serialization for object serialization/deserialization.
 *
 * @param context The application context.
 * @param appId The application ID associated with the DeskPro Messenger module.
 */
@OptIn(ExperimentalSerializationApi::class)
internal class Prefs(context: Context, appId: String) {

    /**
     * Name of the SharedPreferences instance.
     */
    private val PREFS_NAME = "com.deskpro.messenger"

    /**
     * Key for storing user information in SharedPreferences.
     */
    private val USER_INFO = "user_info"

    /**
     * Key for storing JWT token in SharedPreferences.
     */
    private val JWT_TOKEN = "jwt_token"

    private val FCM_TOKEN = "fcm_token"

    /**
     * Lazy-initialized SharedPreferences instance.
     */
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("$PREFS_NAME-$appId", 0)
    }

    /**
     * Lazy-initialized Kotlin Serialization [Json] instance.
     */
    private val json: Json by lazy {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    /**
     * Retrieves the user information from SharedPreferences.
     *
     * @return The [User] object representing user information, or null if not present.
     * @see User
     */
    fun getUserInfo(): User? {
        val jsonString = prefs.getString(USER_INFO, "")
        if (jsonString.isNullOrEmpty()) return null
        return json.decodeFromString<User>(jsonString)
    }

    /**
     * Retrieves the user information JSON string from SharedPreferences.
     *
     * @return The user information JSON string, or null if not present.
     */
    fun getUserInfoJson(): String? {
        val jsonString = prefs.getString(USER_INFO, "")
        if (jsonString.isNullOrEmpty()) return null
        return jsonString
    }

    /**
     * Sets the user information in SharedPreferences.
     *
     * If the user is null, removes the user information from SharedPreferences.
     *
     * @param user The [User] object representing user information, or null.
     * @see User
     */
    fun setUserInfo(user: User?) {
        if (user == null) {
            prefs.edit().remove(USER_INFO).apply()
            return
        }
        val editor = prefs.edit()
        val jsonString = json.encodeToString(user)
        editor.putString(USER_INFO, jsonString)
        editor.apply()
    }

    /**
     * Sets the JWT token in SharedPreferences.
     *
     * If the token is null, removes the token from SharedPreferences.
     *
     * @param token The JWT token, or null.
     */
    fun setJwtToken(token: String?) {
        if (token == null) {
            prefs.edit().remove(JWT_TOKEN).apply()
            return
        }
        val editor = prefs.edit()
        editor.putString(JWT_TOKEN, token)
        editor.apply()
    }

    /**
     * Retrieves the JWT token from SharedPreferences.
     *
     * @return The JWT token, or null if not present.
     */
    fun getJwtToken(): String? {
        return prefs.getString(JWT_TOKEN, null)
    }

    /**
     * Sets the FCM token in SharedPreferences.
     *
     * If the token is null, removes the token from SharedPreferences.
     *
     * @param token The FCM token, or null.
     */
    fun setFCMToken(token: String?) {
        if (token == null) {
            prefs.edit().remove(FCM_TOKEN).apply()
            return
        }
        val editor = prefs.edit()
        editor.putString(FCM_TOKEN, token)
        editor.apply()
    }

    /**
     * Retrieves the FCM token from SharedPreferences.
     *
     * @return The FCM token, or null if not present.
     */
    fun getFCMToken(): String? {
        return prefs.getString(FCM_TOKEN, null)
    }

    /**
     * Clears all data from SharedPreferences.
     */
    fun clear() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
