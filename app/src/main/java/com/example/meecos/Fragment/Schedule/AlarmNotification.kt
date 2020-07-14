package com.example.meecos.Fragment.Schedule

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Process
import android.util.Log
import com.example.meecos.Activity.MainActivity
import com.example.meecos.R
import java.text.SimpleDateFormat
import java.util.*


class AlarmNotification : BroadcastReceiver() {
    // データを受信した
    override fun onReceive(context: Context, intent: Intent) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        Log.d(
            "AlarmBroadcastReceiver",
            "onReceive() pid=" + Process.myPid()
        )
        val requestCode = intent.getIntExtra("RequestCode", 0)
        //val contents = intent.getStringExtra("contents")
        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val channelId = "default"
        // app name
        val title = context.getString(R.string.app_name)
        val currentTime = System.currentTimeMillis()
        val dataFormat =
            SimpleDateFormat("HH:mm:ss", Locale.JAPAN)
        val cTime = dataFormat.format(currentTime)

        // メッセージ　+ 11:22:331
        val message = "予定時刻１時間前です。$cTime"
        //TODO:予定の内容を通知のメッセージに入れたい
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val defaultSoundUri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // Notification　Channel 設定
        val channel = NotificationChannel(
            channelId, title, NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = message
        channel.enableVibration(true)
        channel.canShowBadge()
        channel.enableLights(true)
        channel.lightColor = Color.BLUE
        // the channel appears on the lockscreen
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        channel.setSound(defaultSoundUri, null)
        channel.setShowBadge(true)
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel)
            val notification =
                Notification.Builder(context, channelId)
                    .setContentTitle(title) // android標準アイコンから
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                .build()

            // 通知
            notificationManager.notify(R.string.app_name, notification)
        }
    }
}