package com.sbdev.covid19tracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
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

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    private View view;

    private TextView headerName,headerEmail;

    private DatabaseReference reference;

    private LocationRequest locationRequest;

    public static final int REQUEST_CHECK_SETTINGS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_tracker);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        //getLocation();

        drawer=findViewById(R.id.covidTrackerDrawer);

        bottomNavigationView=findViewById(R.id.bottomNav);

        navigationView=findViewById(R.id.navView);

        menu=findViewById(R.id.mainMenu);
        notiImg=findViewById(R.id.covidNotifications);

        view=navigationView.getHeaderView(0);
        headerName=view.findViewById(R.id.headerNameText);
        headerEmail=view.findViewById(R.id.headerEmailText);

        firebaseAuth=FirebaseAuth.getInstance();

        reference= FirebaseDatabase.getInstance().getReference("Users");

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


        reference.child(firebaseAuth.getCurrentUser().getUid()).child("Profile")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if(dataSnapshot.getKey().equals("Name"))
                    {
                        headerName.setText(dataSnapshot.getValue().toString());
                    }
                    if(dataSnapshot.getKey().equals("Email"))
                    {
                        headerEmail.setText(dataSnapshot.getValue().toString());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error",error.getMessage());
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

                    case R.id.weatherReport:
                        startActivity(new Intent(CovidTrackerActivity.this,AllCountryWeather.class));
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.shareApp:
                        shareMyApp();
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.feedback:
                        getSupportFragmentManager().beginTransaction().replace(R.id.covidFrameLayout,new FeedbackFragment()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.updateApp:
                        openAppInGooglePlay();
                        drawer.closeDrawer(GravityCompat.START);
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

//    private void getLocation() {
//
//        locationRequest=LocationRequest.create();
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        locationRequest.setInterval(5000);
//        locationRequest.setFastestInterval(2000);
//
//        LocationSettingsRequest.Builder builder=new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//        builder.setAlwaysShow(true);
//
//        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
//                .checkLocationSettings(builder.build());
//
//        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
//            @Override
//            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
//
//                try {
//                    LocationSettingsResponse response = task.getResult(ApiException.class);
//                    Toast.makeText(CovidTrackerActivity.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();
//
//                } catch (ApiException e) {
//
//                    switch (e.getStatusCode()) {
//                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//
//                            try {
//                                ResolvableApiException resolvableApiException = (ResolvableApiException)e;
//                                resolvableApiException.startResolutionForResult(CovidTrackerActivity.this,REQUEST_CHECK_SETTINGS);
//                            } catch (IntentSender.SendIntentException ex) {
//                                ex.printStackTrace();
//                            }
//                            break;
//
//                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                            //Device does not have location
//                            break;
//                    }
//                }
//            }
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_CHECK_SETTINGS) {
//
//            switch (resultCode) {
//                case Activity.RESULT_OK:
//                    Toast.makeText(this, "GPS is turned on", Toast.LENGTH_SHORT).show();
//
//                case Activity.RESULT_CANCELED:
//                    Toast.makeText(this, "GPS required to be turned on", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

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