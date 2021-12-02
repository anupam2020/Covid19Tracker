package com.sbdev.covid19tracker;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder> {

    private ArrayList<HourlyModel> arrayList;
    private Context context;

    public HourlyAdapter(ArrayList<HourlyModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HourlyViewHolder(LayoutInflater.from(context).inflate(R.layout.hourly_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {

        //Glide.with(context).load(arrayList.get(holder.getAdapterPosition()).imageURL).into(holder.weather);

        String subTime=arrayList.get(holder.getAdapterPosition()).time.substring(0,2);
        if(Integer.parseInt(subTime)<=11)
        {
            if(subTime.equals("00"))
            {
                holder.time.setText("12:00 AM");
            }
            else
            {
                holder.time.setText(arrayList.get(holder.getAdapterPosition()).time+" AM");
            }
        }
        else
        {
            if(Integer.parseInt(subTime)==12)
            {
                holder.time.setText("12:00 PM");
            }
            else
            {
                int diff=Integer.parseInt(subTime)-12;
                holder.time.setText(diff+arrayList.get(holder.getAdapterPosition()).time.substring(2)+" PM");
            }
        }

        holder.temp.setText(arrayList.get(holder.getAdapterPosition()).temp+"\u2103");

        if(arrayList.get(holder.getAdapterPosition()).isDay==0)
        {
            holder.cardView.setBackgroundResource(R.drawable.night_mode);
            holder.time.setTextColor(context.getResources().getColor(R.color.white));
            holder.temp.setTextColor(context.getResources().getColor(R.color.white));
            if(arrayList.get(holder.getAdapterPosition()).temp<=18)
            {
                holder.weather.setImageResource(R.drawable.mist);
            }
            else
            {
                holder.weather.setImageResource(R.drawable.moon_clear);
            }
        }
        else
        {
            holder.cardView.setBackgroundResource(R.drawable.day_mode);
            holder.time.setTextColor(context.getResources().getColor(R.color.black));
            holder.temp.setTextColor(context.getResources().getColor(R.color.black));
            if(arrayList.get(holder.getAdapterPosition()).temp<=18)
            {
                holder.weather.setImageResource(R.drawable.mist);
            }
            else if(arrayList.get(holder.getAdapterPosition()).temp>18 && arrayList.get(holder.getAdapterPosition()).temp<=24)
            {
                holder.weather.setImageResource(R.drawable.cloudy);
            }
            else
            {
                holder.weather.setImageResource(R.drawable.sun);
            }
        }



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HourlyViewHolder extends RecyclerView.ViewHolder {

        TextView time,temp;
        ImageView weather;
        LinearLayout cardView;

        public HourlyViewHolder(@NonNull View itemView) {
            super(itemView);

            time=itemView.findViewById(R.id.hourlyTime);
            temp=itemView.findViewById(R.id.hourlyTemp);
            weather=itemView.findViewById(R.id.hourlyImage);
            cardView=itemView.findViewById(R.id.hourlyLinear);

        }
    }

}
