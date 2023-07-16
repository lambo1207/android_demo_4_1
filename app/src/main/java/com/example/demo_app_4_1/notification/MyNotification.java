package com.example.demo_app_4_1.notification;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.demo_app_4_1.R;

public class MyNotification extends Application {
    public static final String CHANNEL_ID = "CHANNEL_1";
    public static final String CHANNEL_ID_2 = "CHANNEL_2";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            CharSequence nameFavorite = getString(R.string.channel_name_2);
            String desFavorite = getString(R.string.channel_description_2);
            int impFavorite = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channelFavor = new NotificationChannel(CHANNEL_ID_2, nameFavorite, impFavorite);
            channelFavor.setDescription(desFavorite);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.createNotificationChannel(channelFavor);
        }
    }
}
