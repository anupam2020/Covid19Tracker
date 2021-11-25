package com.sbdev.covid19tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StatesAdapter extends RecyclerView.Adapter<StatesAdapter.StatesViewHolder> {

    private ArrayList<StatesModel> arrayList;
    private Context context;

    public StatesAdapter(ArrayList<StatesModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public StatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StatesViewHolder(LayoutInflater.from(context).inflate(R.layout.states_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatesViewHolder holder, int position) {

        holder.location.setText(arrayList.get(holder.getAdapterPosition()).location);
        holder.affected.setText(arrayList.get(holder.getAdapterPosition()).affected);
        holder.deaths.setText(arrayList.get(holder.getAdapterPosition()).deaths);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class StatesViewHolder extends RecyclerView.ViewHolder {

        TextView location,affected,deaths;

        public StatesViewHolder(@NonNull View itemView) {
            super(itemView);

            location=itemView.findViewById(R.id.textStateLocation);
            affected=itemView.findViewById(R.id.textStateAffected);
            deaths=itemView.findViewById(R.id.textStateDeath);

        }
    }

}
