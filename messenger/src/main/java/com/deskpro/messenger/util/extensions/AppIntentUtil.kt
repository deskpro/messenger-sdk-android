package com.deskpro.messenger.util.extensions

import android.content.Intent
import com.deskpro.messenger.util.Constants.WEB_URL

fun extractUrl(intent: Intent): String {
    return intent.getStringExtra(WEB_URL) ?: ""
}
