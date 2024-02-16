package com.deskpro.messengersdkandroid

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import com.deskpro.messenger.DeskPro
import com.deskpro.messenger.data.MessengerConfig
import com.deskpro.messenger.data.PushNotificationData
import com.deskpro.messenger.data.User
import com.deskpro.messengersdkandroid.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Main activity hosting the DeskPro Messenger integration demonstration.
 *
 * The `MainActivity` class demonstrates the integration of DeskPro Messenger into an Android application.
 * It initializes the DeskPro Messenger instance, handles button clicks, and showcases basic functionality.
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    //region INITIALIZATION

    private lateinit var binding: ActivityMainBinding


    private var appUrl = "https://dev-pr-12730.earthly.deskprodemo.com/deskpro-messenger/deskpro/1/d"
    private var appId = ""
    private var jwtToken = ""
    private var fcmToken = ""
    private var user: User? = null
    private var userInfo = "{\n" +
            "  \"name\": \"John Doe\",\n" +
            "  \"first_name\": \"John\",\n" +
            "  \"last_name\": \"Doe\",\n" +
            "  \"email\": \"john.doe@mail.com\"\n" +
            "}"

    private val stringBuilder = StringBuilder()

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            Log.d(TAG, "Notification permission granted!")
        } else {
            Log.w(TAG, "Notification permission denied!")
        }
    }

    //endregion

    //region Lifecycle Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initListeners()

        binding.etUrl.setText(appUrl)
        binding.etUserInfo.setText(userInfo)
        binding.tvLogs.movementMethod = ScrollingMovementMethod()

        lifecycleScope.launch(Dispatchers.IO) {
            Runtime.getRuntime().exec("logcat -c") // Clear logs
            Runtime.getRuntime().exec("logcat") // Start to capture new logs
                .inputStream
                .bufferedReader()
                .useLines { lines ->
                    // Note that this forEach loop is an infinite loop until this job is cancelled.
                    lines.forEach { newLine ->
                        // Check whether this job is cancelled, since a coroutine must
                        // cooperate to be cancellable.
                        ensureActive()
                        if (newLine.contains("DeskPro")) {
                            //stringBuilder.append("<font color='blue'>$newLine</font><br>")
                            stringBuilder.insert(0, "<font color='blue'>$newLine</font><br>")
                        }
                        if (newLine.contains("chromium")) {
                            //stringBuilder.append("<font color='purple'>$newLine</font><br>")
                            stringBuilder.insert(0, "<font color='purple'>$newLine</font><br>")
                        }
                        withContext(Dispatchers.Main) {
                            binding.tvLogs.text = HtmlCompat.fromHtml(
                                stringBuilder.toString(),
                                HtmlCompat.FROM_HTML_MODE_LEGACY
                            )
                        }
                    }
                }
        }
    }

    //endregion

    //region Click Handling

    override fun onClick(v: View?) {
        binding.apply {
            when (v) {
                btnNotifications -> {
                    onNotificationEnablePressed()
                }

                btnOpenMessenger -> {
                    appUrl = etUrl.text.toString()
                    appId = etAppId.text.toString()
                    jwtToken = etJWT.text.toString()
                    userInfo = etUserInfo.text.toString()

                    if (appUrl.isEmpty() || !appUrl.contains("https://")) {
                        Toast.makeText(this@MainActivity, "Invalid URL!", Toast.LENGTH_SHORT).show()
                        return
                    }

                    if (userInfo.isEmpty()) {
                        Toast.makeText(this@MainActivity, "Invalid User Info!", Toast.LENGTH_SHORT).show()
                        return
                    }

                    try {
                        user = User.fromJson(userInfo)
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "Invalid User Info!", Toast.LENGTH_SHORT).show()
                        return
                    }

                    startDeskPro()
                }

                btnEvents -> {
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setTitle("Messenger app events")
                        .setMessage(App.messenger?.getLogs()?.filter { it.contains("AppEvent") }
                            ?.joinToString("\n\n"))
                        .setPositiveButton(
                            "OK"
                        ) { _: DialogInterface?, _: Int -> }
                        .show()
                }

                btnNotifyMe -> {
                    onNotifyMePressed()
                }
            }
        }
    }

    //endregion

    //region Private methods

    private fun initListeners() {
        binding.btnNotifications.setOnClickListener(this)
        binding.btnOpenMessenger.setOnClickListener(this)
        binding.btnEvents.setOnClickListener(this)
        binding.btnNotifyMe.setOnClickListener(this)

        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { result ->
                fcmToken = result ?: ""
                Log.d(TAG, "Fetched FCM token successfully: $fcmToken")
            }.addOnFailureListener { exception ->
                Log.e(TAG, "Fetching FCM registration token failed: ${exception.message}")
            }
    }

    private fun startDeskPro() {
        App.messenger = DeskPro(applicationContext,
            MessengerConfig(
                appUrl = appUrl,
                appId = appId,
                appIcon = R.drawable.ic_launcher_foreground
            )
        )
        App.messenger?.enableLogging()
        App.messenger?.authorizeUser(jwtToken)
        App.messenger?.setPushRegistrationToken(fcmToken)

        App.messenger?.setUserInfo(user!!)

        App.messenger?.present()?.show()
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Notification permission granted!")
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Log.w(TAG, "Notification permission denied permanently!")
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun onNotificationEnablePressed() {
        askNotificationPermission()
    }

    private fun onNotifyMePressed() {
        App.messenger ?: run {
            App.messenger = DeskPro(this,
                MessengerConfig(
                    appUrl = appUrl,
                    appId = appId,
                    appIcon = R.drawable.ic_launcher_foreground
                )
            )
        }

        App.messenger?.handlePushNotification(
            PushNotificationData(
                "Test",
                "Message of the test notification",
                mapOf("issuer" to "deskpro-messenger", "category" to "new-message")
            )
        )
    }

    companion object {
        private const val TAG = "DeskPro"
    }

    //endregion
}
