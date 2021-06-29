package com.unlock.vaccinelocator.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.unlock.vaccinelocator.MainActivity;
import com.unlock.vaccinelocator.R;

import static com.unlock.vaccinelocator.App.CHANNEL_ID_2;


public class BackgroundService extends Service {

    Handler handler = new Handler();
    Runnable sendApiRequest;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



         sendApiRequest = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BackgroundService.this,"Request has been sent",Toast.LENGTH_SHORT).show();
                handler.postDelayed(this::run,1000*10);
            }
        };
        handler.post(sendApiRequest);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID_2);
        Notification notification = builder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_baseline_build_24)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(2,notification);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(sendApiRequest);
        Toast.makeText(BackgroundService.this,"CallBack Removed",Toast.LENGTH_SHORT).show();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
