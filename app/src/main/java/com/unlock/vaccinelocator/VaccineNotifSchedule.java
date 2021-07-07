package com.unlock.vaccinelocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unlock.vaccinelocator.Adapters.SlotAdapter;
import com.unlock.vaccinelocator.Models.District;
import com.unlock.vaccinelocator.Models.Doses;
import com.unlock.vaccinelocator.Service.BackgroundService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class VaccineNotifSchedule extends AppCompatActivity {

    private Button setSchedule,stop;
    private LinearLayout layout;
    private RadioGroup radioGroup1,radioGroup2;
    private RadioButton distr,pin,locat;
    private EditText date,pincode;
    private Spinner s2,s1;
    private ProgressDialog progress;
    private ArrayList<District> districts;
    private ImageView cal;
    private NotificationManagerCompat notificationManagerCompat;
    Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_notif_schedule);
        initViews();
        String[] states = {"Select a State","Andhra Pradesh",
                "Arunachal Pradesh",
                "Assam",
                "Bihar",
                "Chhattisgarh",
                "Goa",
                "Gujarat",
                "Haryana",
                "Himachal Pradesh",
                "Jammu and Kashmir",
                "Jharkhand",
                "Karnataka",
                "Kerala",
                "Madhya Pradesh",
                "Maharashtra",
                "Manipur",
                "Meghalaya",
                "Mizoram",
                "Nagaland",
                "Odisha",
                "Punjab",
                "Rajasthan",
                "Sikkim",
                "Tamil Nadu",
                "Telangana",
                "Tripura",
                "Uttarakhand",
                "Uttar Pradesh",
                "West Bengal",
                "Andaman and Nicobar Islands",
                "Chandigarh",
                "Dadra and Nagar Haveli",
                "Daman and Diu",
                "Delhi (NCT)",
                "Lakshadweep",
                "Puducherry",
                "Ladakh"
        };
        String selectState = "Select District";

        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.hide();

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
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, states);
        s1.setAdapter(aa);
        s1.setSelection(0);
        s2.setEnabled(false);

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                districts = new ArrayList<>();
                selectStateFun(selectState);

                if(position>0){
                    progress.show();
                    getdata(states[position], districts);
                    s2.setEnabled(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(VaccineNotifSchedule.this,Datedialog,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        distr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischecked = distr.isChecked();
                if(ischecked)
                {
                    layout.setVisibility(View.VISIBLE);
                    pincode.setVisibility(View.GONE);
                }
            }
        });
        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischecked = pin.isChecked();
                if(ischecked)
                {
                    layout.setVisibility(View.GONE);
                    pincode.setVisibility(View.VISIBLE);
                }
            }
        });
        locat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischecked = locat.isChecked();
                if(ischecked)
                {
                    layout.setVisibility(View.GONE);
                    pincode.setVisibility(View.GONE);
                }
            }
        });

        setSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                RadioButton vaccine = (RadioButton) findViewById(radioGroup1.getCheckedRadioButtonId());
                RadioButton age = (RadioButton) findViewById(radioGroup2.getCheckedRadioButtonId());
                String mpincode = pincode.getText().toString();
                String mdate = date.getText().toString();
                String mvaccine = vaccine.getText().toString();
                String mage = age.getText().toString();
                if( !mdate.equals("") && !mvaccine.equals("") && !mage.equals("") && ((s1.getSelectedItemPosition()!=0) || !mpincode.equals("") ) )
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
                    Intent intent = new Intent(VaccineNotifSchedule.this, BackgroundService.class);
                    intent.putExtra("difference",diff);
                    intent.putExtra("submittedTime",submittedtime);
                    if(pin.isChecked())
                    {
                        intent.putExtra("pincode",mpincode);
                    }
                    else if(distr.isChecked())
                    {
                        int distr_id = districts.get(s2.getSelectedItemPosition()).getDis_id();
                        intent.putExtra("distr_id",distr_id);
                    }
                    intent.putExtra("vaccine",mvaccine);
                    intent.putExtra("age",mage);
                    intent.putExtra("date",mdate);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startService(intent);
                    }
                }
                else
                    {
                        Toast.makeText(getApplicationContext(),"Fill the details", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VaccineNotifSchedule.this, BackgroundService.class);
                stopService(intent);
            }
        });
    }

    private void getdata(String state, ArrayList<District> districts) {
        RequestQueue queue = Volley.newRequestQueue(VaccineNotifSchedule.this);
        StringRequest request = new StringRequest(Request.Method.GET, "https://indian-states.herokuapp.com/State/"+state, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("districts");
//                    Log.e("each", jsonArray.getJSONObject(0).get("district_name").toString());
                    for(int i=0;i<jsonArray.length();i++){
                        int id = jsonArray.getJSONObject(i).getInt("district_id");
                        String name = jsonArray.getJSONObject(i).get("district_name").toString();
                        District d1 = new District(id,name);
                        districts.add(d1);
                        Log.e("id", String.valueOf(id));
                        Log.e("name",name);
                    }
                    setAdapters2(districts);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {

        });
        queue.add(request);
    }

    private void setAdapters2(ArrayList<District> districts) {
        ArrayAdapter<District> aaa = new ArrayAdapter<District>(this,R.layout.support_simple_spinner_dropdown_item,districts);
        s2.setAdapter(aaa);
        progress.hide();
    }

    private void selectStateFun(String selectState) {
        ArrayAdapter<String> onfirst = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Collections.singletonList(selectState));
        s2.setAdapter(onfirst);
    }

    private void initViews() {
        setSchedule = findViewById(R.id.set_notifSchedule);
        radioGroup1 = findViewById(R.id.vac_radiogrup);
        radioGroup2 = findViewById(R.id.radio_group2);
        pincode = findViewById(R.id.PincodeEdit);
        cal = findViewById(R.id.calender);
        date = findViewById(R.id.date);
        stop = findViewById(R.id.stop);
        distr = findViewById(R.id.radiobtn_district);
        pin = findViewById(R.id.radiobtn_pin);
        locat = findViewById(R.id.radiobtn_location);
        layout = findViewById(R.id.spinnerLayout);
        s1 = findViewById(R.id.spinner1);
        s2 = findViewById(R.id.spinner2);
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date.setText(sdf.format(calendar.getTime()));
    }

}