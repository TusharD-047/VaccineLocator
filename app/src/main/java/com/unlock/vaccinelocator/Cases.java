package com.unlock.vaccinelocator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Cases extends AppCompatActivity {

    String [] stateCodeNames = {"AN",
            "AP",
            "AR",
            "AS",
            "BR",
            "CH",
            "CT",
            "DN",
            "DL",
            "GA",
            "GJ",
            "HR",
            "HP",
            "JK",
            "JH",
            "KA",
            "KL",
            "LA",
            "LD",
            "MP",
            "MH",
            "MN",
            "ML",
            "MZ",
            "NL",
            "OR",
            "PB",
            "PY",
            "RJ",
            "SK",
            "TN",
            "TG",
            "TR",
            "UP",
            "UT",
            "WB"};
    String [] fullNames = {"Andaman and Nicobar Islands",
            "Andhra Pradesh",
            "Arunachal Pradesh",
            "Assam",
            "Bihar",
            "Chandigarh",
            "Chhattisgarh",
            "Dadra and Nagar Haveli and Daman and Diu",
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
    RecyclerView recyclerView;
    TextView t5,t6,t7,t8,t9,t10,t11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases);

        recyclerView = findViewById(R.id.recyclerview4);
        ArrayList<CasesState> casesStates = new ArrayList<>();
        sendApiRequest(casesStates);
        t5 = findViewById(R.id.confirmed_value);
        t6= findViewById(R.id.recovered_value);
        t7 = findViewById(R.id.active_value);
        t8 = findViewById(R.id.deceased_value);
        t9 = findViewById(R.id.confirmed_inc_value);
        t10= findViewById(R.id.recovered_inc_value);
        t11 = findViewById(R.id.deceased_inc_value);

    }

    private void sendApiRequest(ArrayList<CasesState> casesStates) {
        RequestQueue queue = Volley.newRequestQueue(Cases.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.covid19india.org/v4/min/timeseries.min.json", new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                long res_change_conf=0, res_change_rec=0,res_change_dec=0;
                long india_conf=0,india_recov=0,india_dec=0,india_active=0,ind_change_conf=0,india_change_rec=0,india_change_dec=0;
                Format format = NumberFormat.getNumberInstance(new Locale("en","in"));
                Log.e("check","done");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    for(int i=0;i<stateCodeNames.length;i++){
                        JSONObject jsonObject1 = jsonObject.getJSONObject(stateCodeNames[i]).getJSONObject("dates");
                        Calendar c1 = Calendar.getInstance();
                        Calendar c2 = Calendar.getInstance();
                        c2.add(Calendar.DATE,-1);
                        Date c = c1.getTime();
                        System.out.println("Current time => " + c);
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        String formattedDate = df.format(c);
                        String res_date = df.format(c2.getTime());
                        if(jsonObject1.has(formattedDate)){
                            int active;
                            int confirmed = jsonObject1.getJSONObject(formattedDate).getJSONObject("total").getInt("confirmed");
                            int deceased = jsonObject1.getJSONObject(formattedDate).getJSONObject("total").getInt("deceased");
                            int recovered = jsonObject1.getJSONObject(formattedDate).getJSONObject("total").getInt("recovered");
                            if(jsonObject1.getJSONObject(formattedDate).getJSONObject("total").has("other"))
                                active = confirmed-deceased-recovered-jsonObject1.getJSONObject(formattedDate).getJSONObject("total").getInt("other");
                            else
                                active = confirmed-deceased-recovered;
                            c1.add(Calendar.DATE,-1);
                            String yesterday = df.format(c1.getTime());
                            int confirmed_yest = jsonObject1.getJSONObject(yesterday).getJSONObject("total").getInt("confirmed");
                            int deceased_yest = jsonObject1.getJSONObject(yesterday).getJSONObject("total").getInt("deceased");
                            int recovered_yest = jsonObject1.getJSONObject(yesterday).getJSONObject("total").getInt("recovered");
                            CasesState casesState1 = new CasesState(confirmed,recovered,deceased,confirmed-confirmed_yest,recovered-recovered_yest,deceased-deceased_yest,active,formattedDate);
                            casesStates.add(casesState1);
                            india_conf+=confirmed;
                            india_recov+=recovered;
                            india_dec+=deceased;
                            ind_change_conf += confirmed-confirmed_yest;
                            india_change_dec+= deceased-deceased_yest;
                            india_active+=active;
                            india_change_rec+=recovered-recovered_yest;

                        }
                        else{
                            do {
                                c1.add(Calendar.DATE, -1);
                                formattedDate = df.format(c1.getTime());
                            } while (!jsonObject1.has(formattedDate));
                            int active;
                            int confirmed = jsonObject1.getJSONObject(formattedDate).getJSONObject("total").getInt("confirmed");
                            int deceased = jsonObject1.getJSONObject(formattedDate).getJSONObject("total").getInt("deceased");
                            int recovered = jsonObject1.getJSONObject(formattedDate).getJSONObject("total").getInt("recovered");
                            if(jsonObject1.getJSONObject(formattedDate).getJSONObject("total").has("other"))
                                active = confirmed-deceased-recovered-jsonObject1.getJSONObject(formattedDate).getJSONObject("total").getInt("other");
                            else
                                active = confirmed-deceased-recovered;
                            c1.add(Calendar.DATE,-1);
                            String yesterday = df.format(c1.getTime());
                            int confirmed_yest = jsonObject1.getJSONObject(yesterday).getJSONObject("total").getInt("confirmed");
                            int deceased_yest = jsonObject1.getJSONObject(yesterday).getJSONObject("total").getInt("deceased");
                            int recovered_yest = jsonObject1.getJSONObject(yesterday).getJSONObject("total").getInt("recovered");
                            CasesState casesState1 = new CasesState(confirmed,recovered,deceased,confirmed-confirmed_yest,recovered-recovered_yest,deceased-deceased_yest,active,formattedDate);
                            casesStates.add(casesState1);
                            india_conf+=confirmed;
                            india_recov+=recovered;
                            india_dec+=deceased;
                            india_active+=active;
                            if(res_date.equals(formattedDate)){
                                res_change_conf += confirmed - confirmed_yest;
                                res_change_dec += deceased - deceased_yest;
                                res_change_rec += recovered - recovered_yest;
                            }
                        }

                    }
                    if(ind_change_conf==0 && india_change_dec==0 && india_change_rec==0){
                        ind_change_conf = res_change_conf;
                        india_change_dec= res_change_dec;
                        india_change_rec=res_change_rec;
                    }
                    t5.setText(String.valueOf(format.format(india_conf)));
                    t6.setText(String.valueOf(format.format(india_recov)));
                    t7.setText(String.valueOf(format.format(india_active)));
                    t8.setText(String.valueOf(format.format(india_dec)));
                    t9.setText("+"+format.format(ind_change_conf));
                    t10.setText("+"+format.format(india_change_rec));
                    t11.setText("+"+format.format(india_change_dec));
                    recyclerinit(casesStates);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("err","This is error"+" "+error);
            }
        });
        queue.add(stringRequest);
    }
    private void recyclerinit(ArrayList<CasesState> casesStates) {
        CasesStateAdapter casesStateAdapter = new CasesStateAdapter(casesStates,Cases.this,fullNames,stateCodeNames);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(casesStateAdapter);
    }
}