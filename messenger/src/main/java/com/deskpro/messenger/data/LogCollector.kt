package com.deskpro.messenger.data

import android.annotation.SuppressLint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar

object LogCollector {

    private val logList = mutableListOf<String>()

    fun plantTree() {
        val logCollectorTree = LogCollectorTree()
        Timber.plant(logCollectorTree)
    }

    fun getLogs(): List<String> {
        return logList.toList()
    }

    private class LogCollectorTree : Timber.Tree() {
        @SuppressLint("SimpleDateFormat")
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            val time = Calendar.getInstance().time
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val current = formatter.format(time)

            val logMessage = "$current [$tag] $message"
            logList.add(0, logMessage)
        }
    }
}

