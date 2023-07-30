package com.example.trackflix.notification

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.trackflix.R

class NotificationReceiver : BroadcastReceiver() {

    //The notification Receiver receives the notification and then uses the showNotification() method to create one on the phone
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra("notification_id",0)
        val title = intent.getStringExtra("notification_title") ?: ""
        val message = intent.getStringExtra("notification_message") ?: ""
        showNotification(context, id, title, message)

    }
}

//shows the notification to the user. The contents of the message are set here
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun showNotification(context: Context, id:Int, title: String, message: String) {
    val channelId = "NotifyOnRelease"
    val builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(R.drawable.icon)
        .setContentTitle(title)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    with(NotificationManagerCompat.from(context)) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val PERMISSION_REQUEST_CODE = 1001
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                PERMISSION_REQUEST_CODE
            )
        }
        notify(/* notificationId */ id, builder.build())
    }
}