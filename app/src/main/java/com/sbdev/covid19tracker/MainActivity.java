package com.sbdev.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import com.facebook.share.Share;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout rootLayout;

    private GridLayout gridLayout;

    private CardView card1,card2,card3,card4,card5,card6;

    private AppCompatButton button;

    private FirebaseAuth firebaseAuth;

    private SharedPreferences sp;

    private String PREF="PREF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        sp=getSharedPreferences(PREF,MODE_PRIVATE);

        String count=sp.getString("dialogCount","0");
        if(count.equals("0"))
        {
            showDialog();
        }

        rootLayout=findViewById(R.id.rootLayout);
        gridLayout=findViewById(R.id.gridLayout);
        button=findViewById(R.id.mainLetsGoButton);

        card1=findViewById(R.id.cardView1);
        card2=findViewById(R.id.cardView2);
        card3=findViewById(R.id.cardView3);
        card4=findViewById(R.id.cardView4);
        card5=findViewById(R.id.cardView5);
        card6=findViewById(R.id.cardView6);

        card1.setBackgroundColor(Color.WHITE);
        card2.setBackgroundColor(Color.WHITE);
        card3.setBackgroundColor(Color.WHITE);
        card4.setBackgroundColor(Color.WHITE);
        card5.setBackgroundColor(Color.WHITE);
        card6.setBackgroundColor(Color.WHITE);

        rootLayout.setBackgroundColor(Color.WHITE);
        gridLayout.setBackgroundColor(Color.WHITE);

        firebaseAuth= FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(MainActivity.this,CovidTrackerActivity.class));
            finishAffinity();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginRegActivity.class));
                finishAffinity();
            }
        });

    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Exit");
        builder.setMessage("Do you really want to exit?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finishAffinity();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void showDialog()
    {

        SharedPreferences.Editor editor=sp.edit();
        editor.putString("dialogCount","1");
        editor.apply();

        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Location");
        builder.setMessage("Covid19Tracker collects location data to fetch the Weather details of the current location only when the app is opened or in use.");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.show();

    }

}