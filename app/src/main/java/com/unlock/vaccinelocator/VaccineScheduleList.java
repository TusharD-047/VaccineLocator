package com.unlock.vaccinelocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.unlock.vaccinelocator.Adapters.ScheduleListAdapter;
import com.unlock.vaccinelocator.Models.AlertList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class VaccineScheduleList extends AppCompatActivity {


    private RecyclerView rv;
    long count = 0;
    private ArrayList<AlertList> list;
    private FloatingActionButton addAlert;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_schedule_list);
        rv =  findViewById(R.id.schedulelist_rv);
        addAlert = findViewById(R.id.add_alert);
        tv = findViewById(R.id.text_schedule);


        list = new ArrayList<>();
        ScheduleListAdapter adapter = new ScheduleListAdapter(VaccineScheduleList.this,list);
        rv.setLayoutManager(new LinearLayoutManager(VaccineScheduleList.this));
        rv.setAdapter(adapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child("yL5ZsftdURgfvEZbPK35d7B5aUb2")
                .child("AlertsList");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    rv.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.GONE);
                    count = snapshot.getChildrenCount();
                    for (DataSnapshot snapshot1: snapshot.getChildren())
                    {
                        String vacBrand = snapshot1.child("VaccineName").getValue().toString();
                        String date = snapshot1.child("Date").getValue().toString();
                        String minAge = snapshot1.child("MinAge").getValue().toString();
                        String pincode = null;
                        String distr = null;
                        String state = null;
                        if(snapshot1.hasChild("Pincode"))
                        {
                            pincode = snapshot1.child("Pincode").getValue().toString();
                            Log.e("Pincode",pincode);

                        }
                        else if(snapshot1.hasChild("District"))
                        {
                            distr = snapshot1.child("District").getValue().toString();
                            state = snapshot1.child("State").getValue().toString();
                        }
                        list.add(new AlertList(vacBrand,date,minAge,pincode,distr,state));
                    }
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        addAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VaccineScheduleList.this,VaccineNotifSchedule.class);
                intent.putExtra("count",count);
                startActivity(intent);
                finish();
            }
        });
    }
}