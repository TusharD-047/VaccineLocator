package com.unlock.vaccinelocator.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.unlock.vaccinelocator.CasesByDistrict;
import com.unlock.vaccinelocator.Models.CasesState;
import com.unlock.vaccinelocator.R;

import org.jetbrains.annotations.NotNull;

import java.text.Format;
import java.util.ArrayList;
import java.util.Locale;

public class CasesStateAdapter extends RecyclerView.Adapter<CasesStateAdapter.MyViewHolder> {

    ArrayList<CasesState> arrayList;
    Context mContext;
    String [] abc;
    String [] def;

    public CasesStateAdapter(ArrayList<CasesState> arrayList, Context mContext, String[] abc, String[] def) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        this.abc = abc;
        this.def = def;
    }

    @NonNull
    @NotNull
    @Override



    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_cases,parent,false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull CasesStateAdapter.MyViewHolder holder, int position) {
        Format format = NumberFormat.getNumberInstance(new Locale("en","in"));
        holder.t1.setText(abc[position]);
        holder.t2.setText(format.format(arrayList.get(position).getActive()));
        holder.t3.setText(format.format(arrayList.get(position).getConf())+" ( +"+format.format(arrayList.get(position).getIncrease_conf())+")");
        holder.t4.setText(format.format(arrayList.get(position).getRecovered())+" ( +"+format.format(arrayList.get(position).getChange_recover())+")");
        holder.t5.setText(format.format(arrayList.get(position).getDeceased())+" ( +"+format.format(arrayList.get(position).getChange_deceased())+")");
        holder.t6.setText(arrayList.get(position).getDate());
        holder.c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CasesByDistrict.class);
                intent.putExtra("codeNames",def[position]);
                intent.putExtra("actualName",abc[position]);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView t1,t2,t3,t4,t5,t6;
        CardView c1;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.stateName);
            t2 = itemView.findViewById(R.id.active_val);
            t3 = itemView.findViewById(R.id.conf_value);
            t4 = itemView.findViewById(R.id.recov_val);
            t5 = itemView.findViewById(R.id.dec_val);
            t6 = itemView.findViewById(R.id.date_cases);
            c1 = itemView.findViewById(R.id.card_cases);
        }
    }
}
