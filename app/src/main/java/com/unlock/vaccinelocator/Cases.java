package com.unlock.vaccinelocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unlock.vaccinelocator.Adapters.CasesStateAdapter;
import com.unlock.vaccinelocator.Models.CasesState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Cases extends AppCompatActivity {

    String [] stateCodeNames = {"AN","AP","AR"};
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases);

        recyclerView = findViewById(R.id.recyclerview4);
        ArrayList<CasesState> casesStates = new ArrayList<>();
        sendApiRequest(casesStates);

    }

    private void sendApiRequest(ArrayList<CasesState> casesStates) {
        RequestQueue queue = Volley.newRequestQueue(Cases.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.covid19india.org/v4/min/timeseries.min.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("check","done");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    for(int i=0;i<stateCodeNames.length;i++){
                        JSONObject jsonObject1 = jsonObject.getJSONObject(stateCodeNames[i]).getJSONObject("dates");
                        Calendar c1 = Calendar.getInstance();
                        Date c = c1.getTime();
                        System.out.println("Current time => " + c);
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);
                        Log.e("cases", formattedDate);
                        if(jsonObject1.has(formattedDate)){
                            int confirmed = jsonObject1.getJSONObject(formattedDate).getJSONObject("total").getInt("confirmed");
                            int deceased = jsonObject1.getJSONObject(formattedDate).getJSONObject("total").getInt("deceased");
                            int recovered = jsonObject1.getJSONObject(formattedDate).getJSONObject("total").getInt("recovered");
                            int active = confirmed-deceased-recovered;
                            c1.add(Calendar.DATE,-1);
                            String yesterday = df.format(c1.getTime());
                            int confirmed_yest = jsonObject1.getJSONObject(yesterday).getJSONObject("total").getInt("confirmed");
                            int deceased_yest = jsonObject1.getJSONObject(yesterday).getJSONObject("total").getInt("deceased");
                            int recovered_yest = jsonObject1.getJSONObject(yesterday).getJSONObject("total").getInt("recovered");
                            CasesState casesState1 = new CasesState(confirmed,recovered,deceased,confirmed-confirmed_yest,recovered-recovered_yest,deceased-deceased_yest,active,formattedDate);
                            casesStates.add(casesState1);
                        }
                        else{
                            c1.add(Calendar.DATE,-1);
                            formattedDate = df.format(c1.getTime());
                            int confirmed = jsonObject1.getJSONObject(formattedDate).getJSONObject("total").getInt("confirmed");
                            int deceased = jsonObject1.getJSONObject(formattedDate).getJSONObject("total").getInt("deceased");
                            int recovered = jsonObject1.getJSONObject(formattedDate).getJSONObject("total").getInt("recovered");
                            int active = confirmed-deceased-recovered;
                            c1.add(Calendar.DATE,-1);
                            String yesterday = df.format(c1.getTime());
                            int confirmed_yest = jsonObject1.getJSONObject(yesterday).getJSONObject("total").getInt("confirmed");
                            int deceased_yest = jsonObject1.getJSONObject(yesterday).getJSONObject("total").getInt("deceased");
                            int recovered_yest = jsonObject1.getJSONObject(yesterday).getJSONObject("total").getInt("recovered");
                            CasesState casesState1 = new CasesState(confirmed,recovered,deceased,confirmed-confirmed_yest,recovered-recovered_yest,deceased-deceased_yest,active,formattedDate);
                            casesStates.add(casesState1);
                        }

                    }
                    recyclerinit(casesStates);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }
    private void recyclerinit(ArrayList<CasesState> casesStates) {
        CasesStateAdapter casesStateAdapter = new CasesStateAdapter(casesStates,Cases.this,stateCodeNames);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(casesStateAdapter);
    }
}