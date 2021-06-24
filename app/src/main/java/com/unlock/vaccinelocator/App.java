package com.unlock.vaccinelocator;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_ID = "channel1";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Alert !", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Vaccine slot availaible");
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
    }
}