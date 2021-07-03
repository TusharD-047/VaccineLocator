package com.unlock.vaccinelocator.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.unlock.vaccinelocator.Models.Doses;
import com.unlock.vaccinelocator.R;

import java.text.Format;
import java.util.List;
import java.util.Locale;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.MyViewHolder> {

    List<Doses> list;
    Context mContext;

    public SlotAdapter(List<Doses> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SlotAdapter.MyViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.dose_item,parent,false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull SlotAdapter.MyViewHolder holder, int position) {
        Format format = NumberFormat.getNumberInstance(new Locale("en","in"));
        holder.t1.setText(list.get(position).getCentre_name());
        holder.t2.setText(list.get(position).getAddress());
        holder.t3.setText("Rs : "+list.get(position).getCost());
        holder.t4.setText(list.get(position).getVac_name());
        holder.t5.setText("Dose1 : "+list.get(position).getSlot1());
        holder.t6.setText("Dose2 : "+list.get(position).getSlot2());
        holder.t7.setText(list.get(position).getAge()+ "+");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView t1,t2,t3,t4,t5,t6,t7;
        public MyViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.centre_name);
            t2 = itemView.findViewById(R.id.address);
            t3 = itemView.findViewById(R.id.price);
            t4 = itemView.findViewById(R.id.vaccine);
            t5 = itemView.findViewById(R.id.slots1);
            t6 = itemView.findViewById(R.id.slots2);
            t7 = itemView.findViewById(R.id.age);
        }
    }
}
