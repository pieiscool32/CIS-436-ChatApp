package com.example.cmunte.chatapp;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FirebaseMessage extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("Message", "From: " + remoteMessage.getFrom());


        if (remoteMessage.getNotification() != null) {
            Log.d("Message", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            createNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    private void createNotification(String title, String message) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "ChatApp",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notifications for chatting users");
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message);

        Random rand = new Random();
        manager.notify(rand.nextInt(12345)+73453, notification.build());
    }
}
