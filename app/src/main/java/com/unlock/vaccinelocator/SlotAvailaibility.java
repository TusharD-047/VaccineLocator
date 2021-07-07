package com.unlock.vaccinelocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class SlotAvailaibility extends AppCompatActivity {

    private static String TAG = "SLOT_AVAILAIBILITY";
    private RadioButton distr,pin,locat;
    private LinearLayout layout;
    private EditText Edit_pin;
    private String strings;
    private ArrayAdapter<String> a;
    private Spinner s2,s1;
    private ArrayList<District> districts;
    private RadioGroup r1,r2;
    private ProgressDialog progress;
    private ImageView cal1;
    private Calendar calendar = Calendar.getInstance();
    private EditText date_edit;
    private RecyclerView rv2;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_availaibility);
        initviews();

        String[] states = {"Select a State","Andaman and Nicobar Islands",
                "Andhra Pradesh",
                "Arunachal Pradesh",
                "Assam",
                "Bihar",
                "Chandigarh",
                "Chhattisgarh",
                "Dadra and Nagar Haveli and ",
                "Daman and Diu",
                "Delhi (NCT)",
                "Goa",
                "Gujarat",
                "Haryana",
                "Himachal Pradesh",
                "Jammu and Kashmir",
                "Jharkhand",
                "Karnataka",
                "Kerala",
                "Ladakh",
                "Lakshadweep",
                "Madhya Pradesh",
                "Maharashtra",
                "Manipur",
                "Meghalaya",
                "Mizoram",
                "Nagaland",
                "Odisha",
                "Puducherry",
                "Punjab",
                "Rajasthan",
                "Sikkim",
                "Tamil Nadu",
                "Telangana",
                "Tripura",
                "Uttar Pradesh",
                "Uttarakhand",
                "West Bengal"
        };
        String[] state_id = {"0","1", "2", "3", "4", "5", "6", "7", "8","37","9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36"};
        String selectState = "Select District";

        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait");
        progress.setCancelable(false);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.hide();

        final DatePickerDialog.OnDateSetListener Datedialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
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
                    getdata(state_id[position], districts);
                    s2.setEnabled(true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                submit.setEnabled(true);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SlotAvailaibility.this,Datedialog,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        distr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ischecked = distr.isChecked();
                if(ischecked)
                {
                    layout.setVisibility(View.VISIBLE);
                    Edit_pin.setVisibility(View.GONE);
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
                    Edit_pin.setVisibility(View.VISIBLE);
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
                    Edit_pin.setVisibility(View.GONE);
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Doses> arrayList = new ArrayList<>();
                RadioButton vaccine = (RadioButton) findViewById(r1.getCheckedRadioButtonId());
                RadioButton age = (RadioButton) findViewById(r2.getCheckedRadioButtonId());
                String vacBrand = vaccine.getText().toString().toUpperCase();
                String date = date_edit.getText().toString();
                String agel = age.getText().toString();
                if(distr.isChecked() && !vacBrand.isEmpty() && !date.isEmpty() && !agel.isEmpty() && s1.getSelectedItemPosition()!=0)
                {
                    int dis_id = districts.get(s2.getSelectedItemPosition()).getDis_id();
                    Log.e("disid", String.valueOf(dis_id));
                    getVaccineDatabyDistr(dis_id,date,vacBrand,agel,arrayList);
                }
                else if(pin.isChecked() && !vacBrand.isEmpty() && !date.isEmpty() && !agel.isEmpty())
                {
                    String pincode = Edit_pin.getText().toString();
                    if(!pincode.isEmpty())
                    {
                        getVaccineDatabyPin(pincode,date,vacBrand,agel,arrayList);
                    }
                }
                else
                    {
                        Toast.makeText(getApplicationContext(),"Fill in the details",Toast.LENGTH_SHORT).show();
                    }

            }
        });



    }

    private void getVaccineDatabyPin(String pincode, String date, String vacBrand, String agel, ArrayList<Doses> arrayList) {
        RequestQueue queue = Volley.newRequestQueue(SlotAvailaibility.this);
        StringRequest request = new StringRequest(Request.Method.GET, "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode="+pincode+"&date="+date, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                        if(min_age.equals(agel) && vaccine.equals(vacBrand) )
                        {
                            Doses listItem = new Doses(CentreName,CentreAddr,vaccine,dose1,dose2,cost_vac,min_age);
                            arrayList.add(listItem);
                            Log.e("vaccine",vaccine);
                            Log.e("dose1",dose1);
                        }

                    }
                    preparelist(arrayList);
                } catch (JSONException e) {
                    e.printStackTrace();

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

    private void getdata(String state, ArrayList<District> districts) {
        RequestQueue queue = Volley.newRequestQueue(SlotAvailaibility.this);
        StringRequest request = new StringRequest(Request.Method.GET, "https://cdn-api.co-vin.in/api/v2/admin/location/districts/"+state, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("each", String.valueOf(jsonObject));
                    JSONArray jsonArray = jsonObject.getJSONArray("districts");

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
                    Log.e("error",e.getMessage());
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

    private void getVaccineDatabyDistr(int dis_id, String date, String vacBrand, String ageValue, ArrayList<Doses> arrayList) {
        //"https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id=512&date=31-03-2021"
        RequestQueue q1 = Volley.newRequestQueue(SlotAvailaibility.this);
        StringRequest s1 = new StringRequest(Request.Method.GET, "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id="+dis_id+"&date="+date,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray jsonArray = object.getJSONArray("sessions");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                String vaccine = jsonArray.getJSONObject(i).get("vaccine").toString();
                                String dose1 = jsonArray.getJSONObject(i).get("available_capacity_dose1").toString();
                                String dose2 = jsonArray.getJSONObject(i).get("available_capacity_dose2").toString();
                                String min_age = jsonArray.getJSONObject(i).get("min_age_limit").toString();
                                String CentreName = jsonArray.getJSONObject(i).get("name").toString();
                                String CentreAddr = jsonArray.getJSONObject(i).get("address").toString();
                                String cost_vac = jsonArray.getJSONObject(i).get("fee").toString();
                                int slotAvail = Integer.parseInt(jsonArray.getJSONObject(i).get("available_capacity").toString());
                                if (min_age.equals(ageValue) && vaccine.equals(vacBrand)) {
                                    Doses listItem = new Doses(CentreName, CentreAddr, vaccine, dose1, dose2, cost_vac, min_age);
                                    arrayList.add(listItem);
//                                    SendNotif(vaccine, slotAvail, min_age);
                                    Log.e("vaccine", vaccine);
                                    Log.e("dose1", dose1);
                                }

                            }
                            preparelist(arrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        q1.add(s1);
    }

    private void preparelist(ArrayList<Doses> arrayList) {
        SlotAdapter slotAdapter1 = new SlotAdapter(arrayList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv2.setLayoutManager(linearLayoutManager);
        rv2.setAdapter(slotAdapter1);
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date_edit.setText(sdf.format(calendar.getTime()));

    }

    private void initviews()
    {
        distr = findViewById(R.id.radiobtn_district);
        pin = findViewById(R.id.radiobtn_pin);
        Edit_pin = findViewById(R.id.PincodeEdit);
        layout = findViewById(R.id.spinnerLayout);
        locat = findViewById(R.id.radiobtn_location);

        s1 = findViewById(R.id.spinner1);
        s2 = findViewById(R.id.spinner2);
        submit = findViewById(R.id.getslot_data);
        cal1 = findViewById(R.id.calender);
        date_edit = findViewById(R.id.date);
        rv2 = findViewById(R.id.recyclerView2);
        r1 = findViewById(R.id.vac_radiogrup);
        r2 = findViewById(R.id.radio_group2);

    }
}