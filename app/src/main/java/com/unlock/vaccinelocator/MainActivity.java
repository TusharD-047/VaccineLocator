package com.unlock.vaccinelocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.unlock.vaccinelocator.App.CHANNEL_ID;

public class MainActivity extends AppCompatActivity {

    private Button b;
    private RadioGroup radioGroup1,radioGroup2;
    private EditText date,pincode;
    private ListView lv;
    private NotificationManagerCompat notificationManagerCompat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b = findViewById(R.id.find);
        radioGroup1 = findViewById(R.id.radio_group);
        radioGroup2 = findViewById(R.id.radio_group2);
        pincode = findViewById(R.id.Pincode);
        lv = findViewById(R.id.vaccine_list);
        date = findViewById(R.id.date);
        notificationManagerCompat = NotificationManagerCompat.from(this);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> arrayList = new ArrayList<String>();
                RadioButton vaccine = (RadioButton) findViewById(radioGroup1.getCheckedRadioButtonId());
                RadioButton age = (RadioButton) findViewById(radioGroup2.getCheckedRadioButtonId());
                vaccine.getText();
                sendApiRequest(pincode.getText().toString(),date.getText().toString(),vaccine.getText().toString(),age.getText().toString(),arrayList);
            }
        });
    }

    private void sendApiRequest(String pincode, String date, String vaccineBrand, String ageText, ArrayList<String> arrayList) {

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET, "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode="+pincode+"&date="+date, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                        Log.e("response",response);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("sessions");
                    for (int i =0 ; i<jsonArray.length();i++)
                    {

                        String vaccine = jsonArray.getJSONObject(i).get("vaccine").toString();
                        String dose1 = jsonArray.getJSONObject(i).get("available_capacity_dose1").toString();
                        String min_age = jsonArray.getJSONObject(i).get("min_age_limit").toString();
                        String CentreName = jsonArray.getJSONObject(i).get("name").toString();
                        String CentreAddr = jsonArray.getJSONObject(i).get("address").toString();
                        int slotAvail = Integer.parseInt(jsonArray.getJSONObject(i).get("available_capacity").toString());
                        if(min_age.equals(ageText) && vaccine.equals(vaccineBrand) && slotAvail >0 )
                        {
                            String listItem = "Vaccine Name :- "+vaccineBrand+", Centre Name :- "+CentreName+", Centre Address :- "+CentreAddr+ " No. of slots :- "+slotAvail;
                            arrayList.add(listItem);
                            SendNotif(vaccine,slotAvail,min_age);
                            Log.e("vaccine",vaccine);
                            Log.e("dose1",dose1);
                        }

                    }
                    preparelist(arrayList);
//                            Log.e("array",jsonArray.get(0).toString());
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

    private void preparelist(ArrayList<String> arrayList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrayList);
        lv.setAdapter(adapter);

    }

    private void SendNotif(String vaccine, int slotAvail, String min_age) {
        String msg = "Slots are Availaible of "+vaccine+" for age group "+min_age+" with total no, of free slots equals to "+slotAvail;
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24)
                .setContentTitle("Vaccine Slot Availaible")
                .setContentText(msg)
                .setVibrate(new long[] {1000,1000,1000,1000})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .build();
        notificationManagerCompat.notify(1,notification);
    }
}