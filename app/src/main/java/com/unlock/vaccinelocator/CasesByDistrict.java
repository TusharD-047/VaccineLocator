package com.unlock.vaccinelocator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class CasesByDistrict extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases_by_district);

        TextView t15 = findViewById(R.id.actualName);
        TextView t16 = findViewById(R.id.codename);

        t15.setText(getIntent().getStringExtra("actualName"));
        t16.setText(getIntent().getStringExtra("codeNames"));

        sendApiRequest();

    }

    private void sendApiRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(CasesByDistrict.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.covid19india.org/v4/min/data.min.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response).getJSONObject(getIntent().getStringExtra("codeNames")).getJSONObject("districts");
                    Iterator<String> iter = jsonObject.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        JSONObject jsonObject1 = jsonObject.getJSONObject(key).getJSONObject("total");
                        Log.e("districtWise", key+jsonObject1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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