package com.sbdev.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private DatabaseReference databaseReference;

    private RecyclerView recyclerView;

    private NotificationAdapter adapter;

    private ArrayList<NotificationModel> arrayList;

    private ImageView back;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        recyclerView=findViewById(R.id.notiRecycler);
        back=findViewById(R.id.notiBack);

        progressDialog=new ProgressDialog(NotificationsActivity.this);

        arrayList=new ArrayList<>();

        firebaseAuth=FirebaseAuth.getInstance();

        databaseReference= FirebaseDatabase.getInstance().getReference("Notifications");

        adapter=new NotificationAdapter(NotificationsActivity.this,arrayList);
        recyclerView.setAdapter(adapter);

        progressDialog.show();
        progressDialog.setContentView(R.layout.loading_bg);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {

                    NotificationModel model=dataSnapshot.getValue(NotificationModel.class);
                    arrayList.add(model);

                    progressDialog.dismiss();

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                finish();
            }
        });

    }
}