package com.sbdev.covid19tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllCountryAdapter extends RecyclerView.Adapter<AllCountryAdapter.CountryViewHolder> {

    private Context context;
    private ArrayList<CountryModel> arrayList;

    public AllCountryAdapter(Context context, ArrayList<CountryModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CountryViewHolder(LayoutInflater.from(context).inflate(R.layout.country_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {

        holder.location.setText(arrayList.get(position).getLocation());
        holder.active.setText(arrayList.get(position).getActive());
        holder.deaths.setText(arrayList.get(position).getDeaths());
        holder.recovered.setText(arrayList.get(position).getRecovered());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder
    {

        private TextView location,active,deaths,recovered;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);

            location=itemView.findViewById(R.id.textLocation);
            active=itemView.findViewById(R.id.textActive);
            deaths=itemView.findViewById(R.id.textDeath);
            recovered=itemView.findViewById(R.id.textRecovered);

        }
    }
}
