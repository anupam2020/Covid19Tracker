package com.sbdev.covid19tracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GlobalNewsAdapter extends RecyclerView.Adapter<GlobalNewsAdapter.GlobalNewsViewHolder> {

    private Context context;
    private ArrayList<GlobalNewsModel> arrayList;

    public GlobalNewsAdapter(Context context, ArrayList<GlobalNewsModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public GlobalNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GlobalNewsViewHolder(LayoutInflater.from(context).inflate(R.layout.news_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GlobalNewsViewHolder holder, int position) {

        Glide.with(context).load(arrayList.get(holder.getAdapterPosition()).url).into(holder.img);

        holder.title.setText(arrayList.get(holder.getAdapterPosition()).title);
        holder.des.setText(arrayList.get(holder.getAdapterPosition()).des);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,NewsWebviewActivity.class);
                intent.putExtra("WebUrl",arrayList.get(holder.getAdapterPosition()).webURL);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class GlobalNewsViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView img;
        private TextView title,des;

        public GlobalNewsViewHolder(@NonNull View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.newsItemImg);
            title=itemView.findViewById(R.id.newsItemTitle);
            des=itemView.findViewById(R.id.newsItemDes);

        }
    }

}
