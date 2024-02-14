package com.deskpro.messenger.util.extensions

import android.content.Intent
import com.deskpro.messenger.util.Constants.APP_ID
import com.deskpro.messenger.util.Constants.WEB_URL

/**
 * Extracts the web URL from the provided [intent].
 *
 * This function retrieves the web URL from the extras of the given [intent].
 * If the URL is not present, an empty string is returned.
 *
 * @param intent The Intent containing potential extras, including the web URL.
 * @return The extracted web URL or an empty string if not present.
 */
internal fun extractUrl(intent: Intent): String {
    return intent.getStringExtra(WEB_URL) ?: ""
}

/**
 * Extracts the application ID from the provided [intent].
 *
 * This function retrieves the application ID from the extras of the given [intent].
 * If the application ID is not present, an empty string is returned.
 *
 * @param intent The Intent containing potential extras, including the application ID.
 * @return The extracted application ID or an empty string if not present.
 */
internal fun extractAppId(intent: Intent): String {
    return intent.getStringExtra(APP_ID) ?: ""
}
