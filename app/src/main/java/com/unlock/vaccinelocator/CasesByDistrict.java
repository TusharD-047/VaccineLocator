package com.unlock.vaccinelocator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unlock.vaccinelocator.Adapters.CasesDistrictAdapter;
import com.unlock.vaccinelocator.Models.CasesDistrict;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class CasesByDistrict extends AppCompatActivity {

    RecyclerView rv_dis;
    SearchView searchView;
    ArrayList<CasesDistrict> arrayList = new ArrayList<>();
    CasesDistrictAdapter casesDistrictAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases_by_district);

        TextView t15 = findViewById(R.id.actualName);
        rv_dis = findViewById(R.id.rv10);
        searchView = findViewById(R.id.search_view);

        t15.setText(getIntent().getStringExtra("actualName"));

        sendApiRequest(arrayList);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterText(newText);
                return false;
            }
        });

    }

    private void filterText(String newText) {
        ArrayList<CasesDistrict> filtered = new ArrayList<>();
        for (CasesDistrict item : arrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getDis_name().toLowerCase().contains(newText.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filtered.add(item);
            }
        }
        if (filtered.isEmpty()) {
            // if no item is added in filtered list we are 
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            casesDistrictAdapter.filterList(filtered);
        }
    }

    private void sendApiRequest(ArrayList<CasesDistrict> arrayList) {
        RequestQueue requestQueue = Volley.newRequestQueue(CasesByDistrict.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.covid19india.org/v4/min/data.min.json", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response).getJSONObject(getIntent().getStringExtra("codeNames")).getJSONObject("districts");
                    Iterator<String> iter = jsonObject.keys();
                    String s1;
                    int s2,s3,s4,s5,s6,s7,s8,a=0;
                    while (iter.hasNext()) {
                        a++;
                        String key = iter.next();

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
                        CasesDistrict cD = new CasesDistrict(s1,s5,s2,s4,s3,s6,s7,s8);
                        arrayList.add(cD);

                        Log.e("size",String.valueOf(cD));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("error",e.getMessage());
                }
                initRecyclerView(arrayList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void initRecyclerView(ArrayList<CasesDistrict> arrayList) {
        casesDistrictAdapter = new CasesDistrictAdapter(arrayList,CasesByDistrict.this,getIntent().getStringExtra("Date"));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_dis.setLayoutManager(linearLayoutManager);
        rv_dis.setAdapter(casesDistrictAdapter);

    }
}