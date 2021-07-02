package com.unlock.vaccinelocator.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unlock.vaccinelocator.Models.CasesState;
import com.unlock.vaccinelocator.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CasesStateAdapter extends RecyclerView.Adapter<CasesStateAdapter.MyViewHolder> {

    ArrayList<CasesState> arrayList;
    Context mContext;
    String [] abc;

    public CasesStateAdapter(ArrayList<CasesState> arrayList, Context mContext, String[] abc) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        this.abc = abc;
    }

    @NonNull
    @NotNull
    @Override



    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_cases,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull CasesStateAdapter.MyViewHolder holder, int position) {
        holder.t1.setText(abc[position]);
        holder.t2.setText(arrayList.get(position).getActive()+"");
        holder.t3.setText(arrayList.get(position).getConf()+" ("+arrayList.get(position).getIncrease_conf()+")");
        holder.t4.setText(arrayList.get(position).getRecovered()+" ("+arrayList.get(position).getChange_recover()+")");
        holder.t5.setText(arrayList.get(position).getDeceased()+" ("+arrayList.get(position).getChange_deceased()+")");
        holder.t6.setText(arrayList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView t1,t2,t3,t4,t5,t6;
        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.stateName);
            t2 = itemView.findViewById(R.id.active_val);
            t3 = itemView.findViewById(R.id.conf_value);
            t4 = itemView.findViewById(R.id.recov_val);
            t5 = itemView.findViewById(R.id.dec_val);
            t6 = itemView.findViewById(R.id.date_cases);
        }
    }
}
