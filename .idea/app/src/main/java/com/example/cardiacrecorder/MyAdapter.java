package com.example.cardiacrecorder;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Details> list;
    SelectListener listener;


    public MyAdapter(Context context, ArrayList<Details> list, SelectListener listener) {
        this.context = context;
        this.list = list;
        this.listener=listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Details details = list.get(holder.getAdapterPosition());
        holder.Name.setText(details.getName());
        holder.HearRate.setText(String.valueOf(details.getHeartrate()));
        holder.Systolic.setText(String.valueOf(details.getSystolic()));
        holder.Diastolic.setText(String.valueOf(details.getDiastolic()));
//        holder.Comment.setText(details.getComment());
        holder.Date.setText(details.getDate().toString());
        holder.Time.setText(details.getTime().toString());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(list.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView HearRate, Systolic, Diastolic, Date, Time, Comment,Name;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.name);
            HearRate = itemView.findViewById(R.id.heartrate);
            Systolic = itemView.findViewById(R.id.systolic);
            Diastolic = itemView.findViewById(R.id.diastolic);
//            Comment = itemView.findViewById(R.id.comment);
            Date = itemView.findViewById(R.id.date);
            Time = itemView.findViewById(R.id.time);


            cardView = itemView.findViewById(R.id.main_container);
        }
    }

}