package com.unlock.vaccinelocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ScrollingTabContainerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.Arrays;

public class SearchbyDistrict extends AppCompatActivity {


    String strings;
    ArrayAdapter<String> a;
    Spinner s2;
    ArrayList<District> districts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchby_district);

        String[] states = {"Andhra Pradesh",
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


        Spinner s1 = findViewById(R.id.spinner1);
        s2 = findViewById(R.id.spinner2);
        Button b1 = findViewById(R.id.get_details);


        ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, states);
        s1.setAdapter(aa);
        s2.setEnabled(false);
//        a  = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,strings);
//        s2.setAdapter(a);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                districts = new ArrayList<>();
                getdata(states[position], districts);
                s2.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("value", ""+districts.get(position).getDis_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                    printstates(districts);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {

        });
        queue.add(request);
    }

    private void printstates(ArrayList<District> arrayList) {
        ArrayAdapter<District> aaa = new ArrayAdapter<District>(this,R.layout.support_simple_spinner_dropdown_item,arrayList);
        s2.setAdapter(aaa);
    }


}