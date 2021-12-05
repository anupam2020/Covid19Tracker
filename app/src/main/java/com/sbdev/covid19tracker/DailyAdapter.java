package com.sbdev.covid19tracker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.DailyViewHolder> {

    private Context context;
    private ArrayList<DailyModel> arrayList;

    public DailyAdapter(Context context, ArrayList<DailyModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DailyViewHolder(LayoutInflater.from(context).inflate(R.layout.daily_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {

        holder.temp.setText(arrayList.get(holder.getAdapterPosition()).temp);
        holder.date.setText(arrayList.get(holder.getAdapterPosition()).date);

        String weatherType=arrayList.get(holder.getAdapterPosition()).type;

        weatherType=weatherType.toLowerCase();
        Log.d("Weather + Temp",arrayList.get(holder.getAdapterPosition()).temp + " , " + weatherType);
        if(weatherType.contains("rain"))
        {
            if(weatherType.contains("light"))
            {
                holder.weather.setImageResource(R.drawable.light_rain);
                Log.d("Type","LIGHT RAIN!!!!!");
            }
            else if(weatherType.contains("moderate"))
            {
                holder.weather.setImageResource(R.drawable.moderate_rain);
                Log.d("Type","MODERATE RAIN!!!!!");
            }
            else
            {
                if(weatherType.contains("heavy"))
                {
                    holder.weather.setImageResource(R.drawable.heavy_rain);
                    Log.d("Type","HEAVY RAIN!!!!!");
                }
            }
        }
        else if(weatherType.contains("clear"))
        {
            holder.weather.setImageResource(R.drawable.clear_sky);
        }
        else if(weatherType.contains("mist"))
        {
            holder.weather.setImageResource(R.drawable.mist);
        }
        else
        {
            holder.weather.setImageResource(R.drawable.cloudy);
            Log.d("Type","CLOUDY!!!!!");
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class DailyViewHolder extends RecyclerView.ViewHolder {

        TextView date,temp;
        ImageView weather;

        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.dailyItemDate);
            temp=itemView.findViewById(R.id.dailyItemTemperature);
            weather=itemView.findViewById(R.id.dailyItemWeather);

        }
    }

}
