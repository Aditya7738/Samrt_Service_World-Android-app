package com.example.serviceworld

import android.annotation.SuppressLint

import com.google.firebase.messaging.FirebaseMessagingService


//const val channelId = "notification_channel"
//const val channelName = "com.example.serviceworld"
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService: FirebaseMessagingService() {
//    @SuppressLint("ObsoleteSdkInt")
//    fun generateNotification(customerName: String, location: String){
//        val intent = Intent(this, ServiceProviderBottomNavActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
//            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
//
//        var builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.app_icon)
//            .setAutoCancel(true)
//            .setOnlyAlertOnce(true)
//            .setContentIntent(pendingIntent)
//
//        builder = builder.setContent(getRemoteView(customerName, location))
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//
//        notificationManager.notify(0, builder.build())
//    }
//
//    private fun getRemoteView(customerName: String, location: String): RemoteViews? {
//        val remoteViews = RemoteViews("com.example.serviceworld", R.layout.notifications)
//        remoteViews.setTextViewText(R.id.customerName, customerName)
//        remoteViews.setTextViewText(R.id.location, location)
//
//        return remoteViews
//    }
//
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        if (remoteMessage.notification != null) {
//            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
//
//        }
//    }
}