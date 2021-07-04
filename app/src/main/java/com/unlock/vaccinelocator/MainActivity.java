package com.unlock.vaccinelocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unlock.vaccinelocator.Adapters.SlotAdapter;
import com.unlock.vaccinelocator.Models.Doses;
import com.unlock.vaccinelocator.Service.BackgroundService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.unlock.vaccinelocator.App.CHANNEL_ID;

public class MainActivity extends AppCompatActivity {

    private Button b,stop,b1;
    private RadioGroup radioGroup1,radioGroup2;
    private EditText date,pincode;
    private ImageView cal;
    private RecyclerView rv;
    private NotificationManagerCompat notificationManagerCompat;
    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        notificationManagerCompat = NotificationManagerCompat.from(this);
        final DatePickerDialog.OnDateSetListener Datedialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                calendar.set(Calendar.HOUR_OF_DAY,23);
                calendar.set(Calendar.MINUTE,59);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);
                updateLabel();
            }
        };
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this,Datedialog,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                RadioButton vaccine = (RadioButton) findViewById(radioGroup1.getCheckedRadioButtonId());
                RadioButton age = (RadioButton) findViewById(radioGroup2.getCheckedRadioButtonId());
                String mpincode = pincode.getText().toString();
                String mdate = date.getText().toString();
                String mvaccine = vaccine.getText().toString();
                String mage = age.getText().toString();
                if(!mpincode.equals("") && !mdate.equals("") && !mvaccine.equals("") && !mage.equals(""))
                {
                    long Currtime = Calendar.getInstance().getTimeInMillis();
                    long submittedtime = calendar.getTimeInMillis();
                    Date submittedDate = calendar.getTime();
                    long diff = (submittedtime-Currtime)/(60*60*1000);
                    Date d = Calendar.getInstance().getTime();
                    Log.e("time","in hour "+Currtime/(60*60*1000));
                    Log.e("date","date is "+d);
                    Log.e("subDate","submiited date is "+submittedDate);
                    Log.e("Diff","submiited time is "+submittedtime/(60*60*1000)+" Difference is "+diff);
                    Intent intent = new Intent(MainActivity.this, BackgroundService.class);
                    intent.putExtra("difference",diff);
                    intent.putExtra("submittedTime",submittedtime);
                    intent.putExtra("pincode",mpincode);
                    intent.putExtra("vaccine",mvaccine);
                    intent.putExtra("age",mage);
                    intent.putExtra("date",mdate);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startService(intent);
                    }
                    ArrayList<Doses> arrayList = new ArrayList<>();
                    Log.e("details",pincode.getText().toString()+" " +date.getText().toString()+" "+vaccine.getText().toString()+" "+age.getText().toString());
//                    sendApiRequest(mpincode,mdate,mvaccine,mage,arrayList);
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BackgroundService.class);
                stopService(intent);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchbyDistrict.class);
                startActivity(intent);
            }
        });


    }

    private void initViews() {
        b = findViewById(R.id.find);
        radioGroup1 = findViewById(R.id.radio_group);
        radioGroup2 = findViewById(R.id.radio_group2);
        pincode = findViewById(R.id.Pincode);
        rv = findViewById(R.id.vaccine_list);
        cal = findViewById(R.id.calender);
        date = findViewById(R.id.date);
        stop = findViewById(R.id.stop);
        b1= findViewById(R.id.searchbyD);
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date.setText(sdf.format(calendar.getTime()));
    }


    private void sendApiRequest(String pincode, String date, String vaccineBrand, String ageText, ArrayList<Doses> arrayList) {

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
                        String dose2 = jsonArray.getJSONObject(i).get("available_capacity_dose2").toString();
                        String min_age = jsonArray.getJSONObject(i).get("min_age_limit").toString();
                        String CentreName = jsonArray.getJSONObject(i).get("name").toString();
                        String CentreAddr = jsonArray.getJSONObject(i).get("address").toString();
                        String cost_vac = jsonArray.getJSONObject(i).get("fee").toString();
                        int slotAvail = Integer.parseInt(jsonArray.getJSONObject(i).get("available_capacity").toString());
                        if(min_age.equals(ageText) && vaccine.equals(vaccineBrand) && slotAvail >0 )
                        {
                            Doses listItem = new Doses(CentreName,CentreAddr,vaccine,dose1,dose2,cost_vac,min_age);
                            arrayList.add(listItem);
//                            SendNotif(vaccine,slotAvail,min_age);
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

    private void preparelist(ArrayList<Doses> arrayList) {
        SlotAdapter slotAdapter = new SlotAdapter(arrayList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(slotAdapter);
    }

}