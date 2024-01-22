package com.deskpro.messenger.util

import android.content.Context
import android.content.SharedPreferences
import com.deskpro.messenger.data.User
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
class Prefs(context: Context, appId: String) {

    private val PREFS_NAME = "com.deskpro.messenger"
    private val USER_INFO = "user_info"
    private val JWT_TOKEN = "jwt_token"
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("$PREFS_NAME-$appId", 0)
    }
    private val json: Json by lazy {
        Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    /**
     * Get the user info from SharedPreferences.
     * @return The user info object.
     * @see User
     */
    fun getUserInfo(): User? {
        val jsonString = prefs.getString(USER_INFO, "")
        if (jsonString.isNullOrEmpty()) return null
        return json.decodeFromString<User>(jsonString)
    }

    /**
     * Set the user info in SharedPreferences.
     * If the user is null, removes the user info from SharedPreferences.
     * @param user The user info to save.
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
     * Set the JWT token in SharedPreferences.
     * If the token is null, removes the token from SharedPreferences.
     * @param token The JWT token to save.
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
     * Get the JWT token from SharedPreferences.
     * @return The JWT token.
     */
    fun getJwtToken(): String? {
        return prefs.getString(JWT_TOKEN, null)
    }

    /**
     * Clear all data from SharedPreferences.
     */
    fun clear() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}