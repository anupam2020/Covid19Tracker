package com.sbdev.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CovidTrackerActivity extends AppCompatActivity {

    private DrawerLayout drawer;

    private FirebaseAuth firebaseAuth;

    private BottomNavigationView bottomNavigationView;

    private NavigationView navigationView;

    private ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_tracker);

        getWindow().setStatusBarColor(getResources().getColor(R.color.blue_bg));

        drawer=findViewById(R.id.covidTrackerDrawer);

        bottomNavigationView=findViewById(R.id.bottomNav);

        navigationView=findViewById(R.id.navView);

        menu=findViewById(R.id.mainMenu);

        firebaseAuth=FirebaseAuth.getInstance();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(GravityCompat.START);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.covidFrameLayout,new HomeFragment()).commit();
                        break;

                    case R.id.news:
                        getSupportFragmentManager().beginTransaction().replace(R.id.covidFrameLayout,new NewsFragment()).commit();
                        break;

                    case R.id.weather:
                        getSupportFragmentManager().beginTransaction().replace(R.id.covidFrameLayout,new WeatherFragment()).commit();
                        break;
                }

                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(CovidTrackerActivity.this);
            builder.setTitle("Warning");
            builder.setMessage("Please select any one!");

            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finishAffinity();
                }
            });

            builder.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    firebaseAuth.signOut();
                    startActivity(new Intent(CovidTrackerActivity.this,MainActivity.class));
                    finishAffinity();
                }
            });

            builder.show();
        }
    }

}