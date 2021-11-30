package com.sbdev.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
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
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

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

    private ImageView menu,notiImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_tracker);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        drawer=findViewById(R.id.covidTrackerDrawer);

        bottomNavigationView=findViewById(R.id.bottomNav);

        navigationView=findViewById(R.id.navView);

        menu=findViewById(R.id.mainMenu);
        notiImg=findViewById(R.id.covidNotifications);

        firebaseAuth=FirebaseAuth.getInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.covidFrameLayout,new HomeFragment()).commit();
        navigationView.setCheckedItem(R.id.home);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.openDrawer(GravityCompat.START);
            }
        });

        notiImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(CovidTrackerActivity.this,NotificationsActivity.class));
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.covidFrameLayout,new HomeFragment()).commit();
                        item.setChecked(true);
                        break;

                    case R.id.news:
                        getSupportFragmentManager().beginTransaction().replace(R.id.covidFrameLayout,new NewsFragment()).commit();
                        item.setChecked(true);
                        break;

                    case R.id.weather:
                        startActivity(new Intent(CovidTrackerActivity.this,WeatherActivity.class));
                        break;
                }

                return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {

                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.covidFrameLayout,new ProfileFragment()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.showAll:
                        getSupportFragmentManager().beginTransaction().replace(R.id.covidFrameLayout,new AllCountryFragment()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.shareApp:
                        shareMyApp();
                        break;

                    case R.id.feedback:
                        getSupportFragmentManager().beginTransaction().replace(R.id.covidFrameLayout,new FeedbackFragment()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.updateApp:
                        openAppInGooglePlay();
                        break;

                    case R.id.logout:
                        firebaseAuth.signOut();
                        startActivity(new Intent(CovidTrackerActivity.this,MainActivity.class));
                        finish();
                        break;

                    case R.id.more:
                        getSupportFragmentManager().beginTransaction().replace(R.id.covidFrameLayout,new MoreFragment()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                }

                return false;
            }
        });

    }

    public void shareMyApp()
    {
        try {
            final String appPackageName = CovidTrackerActivity.this.getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out the App at: https://play.google.com/store/apps/details?id=" + appPackageName);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } catch(Exception e) {
            DynamicToast.makeError(CovidTrackerActivity.this,e.getMessage(),2000).show();
        }
    }

    public void openAppInGooglePlay() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) { // if there is no Google Play on device
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
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