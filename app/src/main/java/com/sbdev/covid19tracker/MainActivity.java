package com.sbdev.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout rootLayout;

    private GridLayout gridLayout;

    private CardView card1,card2,card3,card4,card5,card6;

    private AppCompatButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginRegActivity.class));
                finishAffinity();
            }
        });

    }
}