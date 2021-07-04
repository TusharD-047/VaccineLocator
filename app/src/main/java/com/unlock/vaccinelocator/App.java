package com.unlock.vaccinelocator;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

public class App extends Application {

    public static final String CHANNEL_ID_2 = "channel2";
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
            channel.enableVibration(true);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);


            String ChannelName = "My Foreground Service";
            NotificationChannel chan = new NotificationChannel(CHANNEL_ID_2,ChannelName, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(chan);

        }
    }
}