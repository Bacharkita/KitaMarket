package com.todoapp.kitamarket

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val CHANNEL_ID = "notification_channel"
const val CHANNEL_NAME= "com.todoapp.kitamarket"

class MyFirebaseMessagingService: FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if (remoteMessage.getNotification() != null){

            generateNotification(remoteMessage.notification!!.title!!,
                remoteMessage.notification!!.body!!)
        }
    }

    fun getRemoteView(title: String, mass: String): RemoteViews {
        val remoteView = RemoteViews(CHANNEL_NAME,R.layout.notification)

        remoteView.setTextViewText(R.id.article_fragment_name_value,title)
        remoteView.setTextViewText(R.id.mass,mass)
        remoteView.setImageViewResource(R.id.app_logo,R.drawable.gfg)

        return remoteView
    }

    fun generateNotification(title:String, mass:String){

        val code = 0
        val longArray=longArrayOf(1000,1000,1000,1000)

        val intent = Intent(this,CustomerInformation::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,
            code,intent,PendingIntent.FLAG_ONE_SHOT)

        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext,
                CHANNEL_ID)
            .setSmallIcon(R.drawable.gfg)
            .setAutoCancel(true)
            .setVibrate(longArray)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,mass))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH)

            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build())
    }
}