package com.example.testcase

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlarmManager.ELAPSED_REALTIME_WAKEUP
import android.app.AlarmManager.INTERVAL_FIFTEEN_MINUTES
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*


class BootCompletedBroadcastReceiver : BroadcastReceiver() {

    //TODO:handle runtime permissions
    //TODO: Check if
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action != null) {
            if (action == Intent.ACTION_BOOT_COMPLETED) {
                context.storage().storeCurrentTimestamp()
            } else if (action == SCHEDULE_ACTION) {
                val intentToFire = Intent(
                    context,
                    BootCompletedBroadcastReceiver::class.java
                )
                intentToFire.action = SCHEDULE_ACTION

                val alarmIntent = PendingIntent.getBroadcast(context, 0, intentToFire, 0)

                val c: Calendar = Calendar.getInstance()
                c.add(Calendar.MINUTE, 15)
                val after15min: Long = c.timeInMillis

                (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager)
                    .setRepeating(
                        ELAPSED_REALTIME_WAKEUP,
                        after15min,
                        INTERVAL_FIFTEEN_MINUTES,
                        alarmIntent
                    )

                val pendingIntent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle("Wake up")
                    .setContentText(
                        getBody(
                            context.storage().getLastTimestamp(),
                            context.storage().getDelta()
                        )
                    )
                    .setContentIntent(
                        PendingIntent.getActivity(
                            context,
                            0,
                            pendingIntent,
                            PendingIntent.FLAG_IMMUTABLE
                        )
                    )
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                with(NotificationManagerCompat.from(context)) {
                    notify(NOTIFY_ID, builder.build())
                }
            }
        }
    }

    companion object {
        const val SCHEDULE_ACTION = "com.example.SCHEDULE_ALARM"
        const val CHANNEL_ID = "1001"
        const val NOTIFY_ID = 101
    }
}

fun getBody(lastTimestamp: Long?, delta: Long?): String {
    return when {
        delta != null -> {
            "“Last boots time delta = $delta"
        }
        lastTimestamp != null -> {
            "The boot was detected with the timestamp = $lastTimestamp"
        }
        else -> "No boots detected"
    }
}