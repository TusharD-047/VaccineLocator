package com.unlock.vaccinelocator.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unlock.vaccinelocator.MainActivity;
import com.unlock.vaccinelocator.Models.AlertList;
import com.unlock.vaccinelocator.R;
import com.unlock.vaccinelocator.Service.BackgroundService;
import com.unlock.vaccinelocator.VaccineScheduleList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleListViewHolder> {

    public Context mcontext;
    public ArrayList<AlertList> list;

    public ScheduleListAdapter(Context mcontext, ArrayList<AlertList> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public ScheduleListViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.schedulelist_item,parent,false);
        return new ScheduleListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ScheduleListAdapter.ScheduleListViewHolder holder, int position) {

        String age = list.get(position).getMinAge();
        String vac_name = list.get(position).getVacBrand();
        String date = list.get(position).getDate();
        String distr = list.get(position).getDistr();
        String state = list.get(position).getState();
        String pincode = list.get(position).getPincode();
        holder.vac_age_detail.setText(vac_name+" , "+age+" min. age");
        if(pincode.isEmpty())
        {
            holder.location.setText(distr+", "+state);
        }
        else
            {
                holder.location.setText("Pincode : "+pincode);
            }
        holder.date.setText(date);
        holder.Alertcount.setText("Alert "+(position+1)+" !");
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child("AlertsList");
                reference.child("Alert"+(position+1)).removeValue();
                Intent intent = new Intent(mcontext, BackgroundService.class);
                ((Activity)mcontext).stopService(intent);
                mcontext.startActivity(new Intent(mcontext,VaccineScheduleList.class));
                ((Activity)mcontext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ScheduleListViewHolder extends RecyclerView.ViewHolder {

        public TextView Alertcount,vac_age_detail,date,location,status;
        public ImageView delete;
        public ScheduleListViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            Alertcount = itemView.findViewById(R.id.tv1);
            vac_age_detail = itemView.findViewById(R.id.vac_age_detail);
            date = itemView.findViewById(R.id.date);
            location = itemView.findViewById(R.id.address);
            status = itemView.findViewById(R.id.status);
            delete = itemView.findViewById(R.id.delete_alert);
        }
    }
}
