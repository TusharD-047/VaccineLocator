package com.unlock.vaccinelocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ScrollingTabContainerView;

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
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unlock.vaccinelocator.Models.District;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class SearchbyDistrict extends AppCompatActivity {


    String strings;
    ArrayAdapter<String> a;
    Spinner s2;
    ArrayList<District> districts;
    ProgressDialog progress;
    private ImageView cal1;
    Calendar calendar = Calendar.getInstance();
    private EditText date_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchby_district);

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
                "Delhi",
                "Lakshadweep",
                "Puducherry",
                "Ladakh"
        };
        String selectState = "Select District";


        Spinner s1 = findViewById(R.id.spinner1);
        s2 = findViewById(R.id.spinner2);
        Button b1 = findViewById(R.id.get_details);
        cal1 = findViewById(R.id.calender1);
        date_edit = findViewById(R.id.date1);

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

        cal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SearchbyDistrict.this,Datedialog,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, states);
        s1.setAdapter(aa);
        s1.setSelection(0);
        s2.setEnabled(false);
//        a  = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,strings);
//        s2.setAdapter(a);
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
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    Log.e("value", ""+districts.get(position).getDis_id());
                    getVaccineData(districts.get(position).getDis_id(),date_edit.getText().toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date_edit.setText(sdf.format(calendar.getTime()));
    }

    private void getVaccineData(int dis_id, String s) {
        //"https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?district_id=512&date=31-03-2021"
    }

    private void selectStateFun(String selectState) {
        ArrayAdapter<String> onfirst = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, Collections.singletonList(selectState));
        s2.setAdapter(onfirst);
    }

    private void getdata(String state, ArrayList<District> districts) {

        RequestQueue queue = Volley.newRequestQueue(SearchbyDistrict.this);
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

    private void setAdapters2(ArrayList<District> arrayList) {
        ArrayAdapter<District> aaa = new ArrayAdapter<District>(this,R.layout.support_simple_spinner_dropdown_item,arrayList);
        s2.setAdapter(aaa);
        progress.hide();
    }


}