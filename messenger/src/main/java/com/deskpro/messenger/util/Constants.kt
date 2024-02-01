package com.deskpro.messenger.util

/**
 * Object containing constants used within the DeskPro Messenger module.
 *
 * The `Constants` object holds key constants such as web URL, app ID, web interface key,
 * and the path to the error page.
 */
internal object Constants {

    /**
     * Key for passing the web URL through Intent extras.
     */
    const val WEB_URL = "web_url"

    /**
     * Key for passing the app ID through Intent extras.
     */
    const val APP_ID = "app_id"

    /**
     * Key for passing the app icon resource ID through Intent extras.
     */
    const val APP_ICON = "app_icon"

    /**
     * Key representing the Android web interface in JavaScript.
     */
    const val WEB_INTERFACE_KEY = "androidApp"

    /**
     * Path to the error page within the Android assets.
     */
    const val ERROR_PAGE_PATH = "file:///android_asset/error_page.html"
}
