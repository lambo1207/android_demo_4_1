package com.example.demo_app_4_1.service;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.demo_app_4_1.MainActivity;
import com.example.demo_app_4_1.R;
import com.example.demo_app_4_1.notification.MyNotification;

import java.util.Date;

public class NotifiService extends Service {
    private static final String TITLE_NOTIFI = "Update Success";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String contentNotifiGet = intent.getStringExtra("key_content_intent");
        sendNotification(contentNotifiGet);

        return START_REDELIVER_INTENT;
    }

    private void sendNotification(String contentNotifiGet) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.picture_notifi);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(getNotificationId(),
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, MyNotification.CHANNEL_ID)
                .setContentTitle(TITLE_NOTIFI)
                .setContentText(contentNotifiGet)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(CONTENT_NOTIFI))
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null))
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setLargeIcon(bitmap)
                .setSound(uri)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setColor(getResources().getColor(R.color.green))
                .build();

//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        notificationManagerCompat.notify(getNotificationId(), notification);

        startForeground(1, notification);
    }

    private int getNotificationId(){
        return (int) new Date().getTime();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
