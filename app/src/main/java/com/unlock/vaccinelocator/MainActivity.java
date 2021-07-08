package com.unlock.vaccinelocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unlock.vaccinelocator.Adapters.SlotAdapter;
import com.unlock.vaccinelocator.Models.CasesDistrict;
import com.unlock.vaccinelocator.Models.Doses;
import com.unlock.vaccinelocator.Service.BackgroundService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import static com.unlock.vaccinelocator.App.CHANNEL_ID;

public class MainActivity extends AppCompatActivity {

    TextView t31,t32,t33,t34,t35;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView c1 = findViewById(R.id.casesFull);
        CardView c2 = findViewById(R.id.slot_availcard);
        CardView c3 = findViewById(R.id.vaccine_schedulercard);

        t31 = findViewById(R.id.home_act_value);
        t32 = findViewById(R.id.home_conf_value);
        t33 = findViewById(R.id.home_dec_value);
        t34 = findViewById(R.id.home_rec_value);
        t35 = findViewById(R.id.current_location);

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Cases.class));
                finish();
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SlotAvailaibility.class));
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,VaccineNotifSchedule.class));
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e("funcCal",preferences.getInt("DistrictNumber",0)+"");

        if(preferences.contains("DistrictNumber") && preferences.contains("StateCodeName")) {
            getHomePageCases(preferences.getInt("DistrictNumber", 0), preferences.getString("StateCodeName", "TT"));
        }

    }

    private void getHomePageCases(int districtNumber, String string) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.covid19india.org/v4/min/data.min.json", new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response).getJSONObject(string).getJSONObject("districts");
                    Iterator<String> iter = jsonObject.keys();
                    String s1 = null;
                    int s2=0,s3=0,s4=0,s5 = 0,s6=0,s7=0,s8=0,a=0;
                    while (iter.hasNext()) {
                        String key = iter.next();
                        if(a==districtNumber)
                        {
                            s1 = key;
                            JSONObject jsonObject1 = jsonObject.getJSONObject(key).getJSONObject("total");
                            JSONObject jsonObject2 = jsonObject.getJSONObject(key);
                            Log.e("abc",String.valueOf(jsonObject1));
                            if(jsonObject2.has("delta")){
                                if(jsonObject2.getJSONObject("delta").has("confirmed"))
                                    s6 = jsonObject2.getJSONObject("delta").getInt("confirmed");
                                else
                                    s6 = 0;
                                if(jsonObject2.getJSONObject("delta").has("recovered"))
                                    s7 = jsonObject2.getJSONObject("delta").getInt("recovered");
                                else
                                    s7 = 0;
                                if(jsonObject2.getJSONObject("delta").has("deceased"))
                                    s8 = jsonObject2.getJSONObject("delta").getInt("deceased");
                                else
                                    s8 = 0;
                            }else{
                                s6 = 0;
                                s7 = 0;
                                s8 = 0;
                            }
                            if(jsonObject1.has("confirmed")){
                                if(jsonObject1.has("recovered")){
                                    if(jsonObject1.has("deceased")){
                                        s2 = jsonObject1.getInt("confirmed");
                                        s3 = jsonObject1.getInt("recovered");
                                        s4 = jsonObject1.getInt("deceased");
                                        s5 = (jsonObject1.getInt("confirmed")-jsonObject1.getInt("recovered")-jsonObject1.getInt("deceased"));

                                    }
                                    else{
                                        s2 = jsonObject1.getInt("confirmed");
                                        s3 = jsonObject1.getInt("recovered");
                                        Log.e("deceased",key);
                                        s4 = 0;
                                        s5 = (jsonObject1.getInt("confirmed")-jsonObject1.getInt("recovered"));
                                    }
                                }
                                else {
                                    s2 = jsonObject1.getInt("confirmed");
                                    s3 = 0;
                                    s4 = 0;
                                    s5 = (jsonObject1.getInt("confirmed"));
                                }
                            }
                            else{
                                s2= 0;
                                s3 = 0;
                                s4 = 0;
                                s5 = 0;
                            }
                            break;
                        }
                        a++;
                        Log.e("valueofA",String.valueOf(a));
                    }
                    t31.setText(s5+"");
                    t32.setText(s2+"(+" +s6+ ")");
                    t33.setText(s4+"(+" +s8+ ")");
                    t34.setText(s3+"(+" +s7+ ")");
                    t35.setText(s1);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error",e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
}