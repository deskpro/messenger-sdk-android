package com.deskpro.messenger.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.deskpro.messenger.R
import com.deskpro.messenger.ui.MessengerWebViewActivity

internal class NotificationHelper(private val context: Context) {

    private val notificationManager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notifChannel =
                NotificationChannel(
                    CHANNEL_ID,
                    context.getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            notifChannel.description = context.getString(R.string.channel_description)
            notificationManager.createNotificationChannel(notifChannel)
        }
    }

    /** Set the notification.
     * @param icon A resource ID of the icon to be displayed in the notification.
     **/
    fun showNotification(title: String, body: String, badgeNumber: Int? = null, icon: Int = -1, url: String, appId: String) {
        val intent = MessengerWebViewActivity.notifIntent(context, url, appId)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 684351, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(if (icon == -1) R.drawable.deskpro_logo else icon)
            .setColor(Color.parseColor("#006BA5"))
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        if (badgeNumber != null)
            builder.setNumber(badgeNumber)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        private const val TAG = "DeskPro"
        private const val CHANNEL_ID = "68349432239241"
        private const val NOTIFICATION_ID = 397841
    }
}
