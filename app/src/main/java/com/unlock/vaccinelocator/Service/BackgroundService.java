package com.unlock.vaccinelocator.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unlock.vaccinelocator.MainActivity;
import com.unlock.vaccinelocator.R;
import com.unlock.vaccinelocator.SlotAvailaibility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static com.unlock.vaccinelocator.App.CHANNEL_ID;
import static com.unlock.vaccinelocator.App.CHANNEL_ID_2;


public class BackgroundService extends Service {

    private Handler handler = new Handler();
    private Runnable backgroundRequest;
    private long currTime,diff,submittedTime;
    private String age="",vaccine="",pincode="",date="";
    int distr_id;
    private NotificationManagerCompat notificationManagerCompat;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManagerCompat = NotificationManagerCompat.from(BackgroundService.this);
        getDataFromIntent(intent);
        RunningRepeatedRequest(intent);
        return makeServiceForeground();

    }

    private int makeServiceForeground() {

        Intent notificationIntent = new Intent(BackgroundService.this, MainActivity.class);
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
        return START_REDELIVER_INTENT;
    }

    private void RunningRepeatedRequest(Intent intent) {

        backgroundRequest = new Runnable() {
            @Override
            public void run() {

                currTime = Calendar.getInstance().getTimeInMillis();
                diff = (submittedTime-currTime)/(1000*60*60);
                Log.e("Diff in hr","New Difference is "+diff);
                Log.e("Diff in sec","New Difference is "+(submittedTime-currTime)/(1000));
                if(diff>47){
                    if(intent.hasExtra("pincode"))
                    {
                        String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode="+pincode+"&date="+date;
                        sendApiRequest(age,vaccine,url);
                    }
                    else if(intent.hasExtra("distr_id"))
                    {
                        String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id="+distr_id+"&date="+date;
                        sendApiRequest(age,vaccine,url);
                    }
                    handler.postDelayed(this::run,1000*60*30);
                    Toast.makeText(BackgroundService.this,"Request has been sent and will sent again after 30 min",Toast.LENGTH_SHORT).show();
                }
                else if(diff>=0 && diff<=47)
                {
                    if(intent.hasExtra("pincode"))
                    {
                        String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode="+pincode+"&date="+date;
                        sendApiRequest(age,vaccine,url);
                    }
                    else if(intent.hasExtra("distr_id"))
                    {
                        String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id="+distr_id+"&date="+date;
                        sendApiRequest(age,vaccine,url);
                    }
                    handler.postDelayed(this::run,1000*60*15);
                    Toast.makeText(BackgroundService.this,"Request has been sent and will sent again after 15 min",Toast.LENGTH_SHORT).show();
                }
                else if(diff<0)
                {
                    String msg = "Unfortunately Slots of "+vaccine+" for age group "+age+" cannot be made availaible..Please Set Schedule for another day";
                    String title = "Try Another Day";
                    SendNotif(msg,title);
                    stopForeground(true);
                    stopSelf();
                }

            }
        };
        handler.post(backgroundRequest);

    }

    private void getDataFromIntent(Intent intent) {
        diff = intent.getLongExtra("difference",-1);
        age = intent.getStringExtra("age");
        vaccine = intent.getStringExtra("vaccine");
        if(intent.hasExtra("pincode"))
        {
            pincode = intent.getStringExtra("pincode");
        }
        if(intent.hasExtra("distr_id"))
        {
            distr_id = intent.getIntExtra("distr_id",-1);
        }
        submittedTime = intent.getLongExtra("submittedTime",-1);
        date = intent.getStringExtra("date");
    }

    private void sendApiRequest(String age, String mvaccine, String url) {
        RequestQueue queue = Volley.newRequestQueue(BackgroundService.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                        Log.e("response",response);
                try {
                    Toast.makeText(getApplicationContext(),"We got response",Toast.LENGTH_SHORT).show();
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("sessions");
                    long count=0;
                    for (int i =0 ; i<jsonArray.length();i++)
                    {

                        String vaccine = jsonArray.getJSONObject(i).get("vaccine").toString();
                        String dose1 = jsonArray.getJSONObject(i).get("available_capacity_dose1").toString();
                        String dose2 = jsonArray.getJSONObject(i).get("available_capacity_dose2").toString();
                        String min_age = jsonArray.getJSONObject(i).get("min_age_limit").toString();
                        String CentreName = jsonArray.getJSONObject(i).get("name").toString();
                        String CentreAddr = jsonArray.getJSONObject(i).get("address").toString();
                        String cost_vac = jsonArray.getJSONObject(i).get("fee").toString();
                        int slotAvail = Integer.parseInt(jsonArray.getJSONObject(i).get("available_capacity").toString());
                        if(min_age.equals(age) && vaccine.equals(mvaccine) && slotAvail >0 )
                        {
                            count++;
                            Log.e("vaccine",vaccine);
                            Log.e("dose1",dose1);
                            Log.e("slot_avail", String.valueOf(slotAvail));
                            Log.e("dose2",dose2);
                        }

                    }
                    if(count>0)
                    {
                        String msg = "Slots are Availaible of "+vaccine+" for age group "+age;
                        String title = "Vaccine Slot Availaible";
                        SendNotif(msg,title);
                        stopForeground(true);
                        stopSelf();

                    }
                    else
                        {
                            String msg = "Oops!! Unavailaible slots for "+vaccine+" for age group "+age;
                            String title = "Unavailaible slots!!";
                            SendNotif(msg,title);
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error",e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error",error.getMessage());
            }
        });
        queue.add(request);
    }

    private void SendNotif(String msg,String title) {
        Intent intent = new Intent(this, SlotAvailaibility.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,intent,0);
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle(title)
                .setContentText(msg)
                .setColor(Color.BLUE)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setAutoCancel(true);
        notificationManagerCompat.notify(1,builder.build());
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(backgroundRequest);
        Toast.makeText(BackgroundService.this,"CallBack Removed",Toast.LENGTH_SHORT).show();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
