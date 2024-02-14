package com.deskpro.messenger

import android.app.Application
import android.content.Context
import com.deskpro.messenger.data.LogCollector

/**
 * Internal Application class to hold the application context.
 *
 * The `App` class is an extension of the [Application] class and serves as a container
 * for storing and accessing the application context throughout its lifecycle.
 */
internal class App : Application() {

    /**
     * A companion object holding a static reference to the application context.
     */
    companion object {
        /**
         * The application context.
         */
        var appContext: Context? = null

        fun setCollector() {
            LogCollector.plantTree()
        }
    }
}
