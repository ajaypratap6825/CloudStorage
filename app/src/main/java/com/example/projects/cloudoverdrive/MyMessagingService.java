package com.example.projects.cloudoverdrive;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        show( remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    private void show(String title, String message) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(ch);
        }

        NotificationCompat.Builder b = new NotificationCompat.Builder(this,"MyNotifications")
                .setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.cloud).setSound(defaultSoundUri);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(0, b.build());
    }

}


