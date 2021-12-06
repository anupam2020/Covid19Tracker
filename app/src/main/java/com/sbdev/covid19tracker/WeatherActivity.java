package com.sbdev.covid19tracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class WeatherActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private TextView city,country,dateTime,temp,skyType,pressure,humidity,windSpeed,presText,humText,windText;

    private ImageView weather,dayNightMode;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private OkHttpClient client;

    private Request request;

    private String url,dateString;

    private SimpleDateFormat simpleDateFormat;

    private Date date;

    private ProgressDialog progressDialog;

    private SharedPreferences sp,sp2;

    private String SHARED_PREFS="SHARED_PREFS";

    private TabLayout WeathertabLayout;
    private ViewPager2 WeatherviewPager2;
    private WeatherStateAdapter Weatheradapter;

    private SwipeRefreshLayout swipe;

    private int count=0;

    private LocationRequest locationRequest;

    public static final int REQUEST_CHECK_SETTINGS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        StatusBarUtil.setTransparentForImageView(WeatherActivity.this,null);

        grantPermission();

        city=findViewById(R.id.cityText);
        country=findViewById(R.id.countryText);
        dateTime=findViewById(R.id.dateTimeText);
        temp=findViewById(R.id.tempText);
        skyType=findViewById(R.id.skyTypeText);
        pressure=findViewById(R.id.pressureValue);
        humidity=findViewById(R.id.humidityValue);
        windSpeed=findViewById(R.id.windValue);

        presText=findViewById(R.id.pressureText);
        humText=findViewById(R.id.humidityText);
        windText=findViewById(R.id.windText);

        weather=findViewById(R.id.weatherImg);
        dayNightMode=findViewById(R.id.day_night_mode);

        swipe=findViewById(R.id.swipeToRefresh);

        WeathertabLayout=findViewById(R.id.weatherTabLayout);
        WeatherviewPager2=findViewById(R.id.weatherViewPager2);

        FragmentManager fm=getSupportFragmentManager();
        Weatheradapter=new WeatherStateAdapter(fm,getLifecycle());
        WeatherviewPager2.setAdapter(Weatheradapter);


        WeathertabLayout.addTab(WeathertabLayout.newTab().setText("Today"));
        WeathertabLayout.addTab(WeathertabLayout.newTab().setText("Hourly"));
        WeathertabLayout.addTab(WeathertabLayout.newTab().setText("Daily"));

//        WeathertabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_location_on_24);
//        WeathertabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_world_24);

        WeathertabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                WeatherviewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        WeatherviewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                WeathertabLayout.selectTab(WeathertabLayout.getTabAt(position));
            }
        });


        sp=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        sp2=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);

        progressDialog=new ProgressDialog(WeatherActivity.this);

        progressDialog.show();
        progressDialog.setContentView(R.layout.loading_bg);
        //progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        String mode=sp.getString("bgMode","1");
        if(mode.equals("0"))
        {
            dayNightMode.setImageResource(R.drawable.night_mode);
            nightMode();
        }
        else
        {
            dayNightMode.setImageResource(R.drawable.day_mode);
            dayMode();
        }


        client=new OkHttpClient();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(WeatherActivity.this);

        if (ActivityCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLocation();

        } else {
            ActivityCompat.requestPermissions(WeatherActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                finish();
                startActivity(getIntent());

                swipe.setRefreshing(false);

            }
        });

    }

    private void grantPermission() {

        locationRequest=LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder=new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(WeatherActivity.this, "GPS is already turned on", Toast.LENGTH_SHORT).show();
                    getLocation();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                                resolvableApiException.startResolutionForResult(WeatherActivity.this,REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {

            switch (resultCode) {
                case Activity.RESULT_OK:
                    Toast.makeText(this, "GPS is turned on", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            finish();
                            startActivity(getIntent());

                        }
                    },2000);

                case Activity.RESULT_CANCELED:
                    Toast.makeText(this, "GPS required to be turned on", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location=task.getResult();

                if(location!=null)
                {

                    Geocoder geocoder=new Geocoder(WeatherActivity.this, Locale.getDefault());

                    try {

                        List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                        String details="Country Name: "+addresses.get(0).getCountryName()+"\n"+
                                "Locality: "+addresses.get(0).getLocality()+"\n"+
                                "Lat: "+(float)addresses.get(0).getLatitude()+"\n"+
                                "Lon: "+(float)addresses.get(0).getLongitude()+"\n"+
                                "Admin Area: "+addresses.get(0).getAdminArea()+"\n"+
                                "Sub Admin Area: "+addresses.get(0).getSubAdminArea();

                        simpleDateFormat=new SimpleDateFormat("MMMM dd, EEEE hh:mm a");
                        date=new Date();
                        dateString=simpleDateFormat.format(date);

                        city.setText(addresses.get(0).getLocality());
                        country.setText(addresses.get(0).getAdminArea()+", "+addresses.get(0).getCountryName());
                        dateTime.setText(dateString);

                        double lat= addresses.get(0).getLatitude();
                        double lon= addresses.get(0).getLongitude();

//                        url="https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=minutely&appid=66b871b322375297071a645567414648";
//
//                        request=new Request.Builder()
//                                .url(url)
//                                .build();
//
//                        client.newCall(request).enqueue(new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//
//                                Log.e("On Failure",e.getMessage());
//
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//
//                                if(response.isSuccessful())
//                                {
//
//                                    String res=response.body().string();
//
//                                    WeatherActivity.this.runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//
//                                            try {
//
//                                                JSONObject jsonObject=new JSONObject(res);
//
//                                                String timezone=jsonObject.getString("timezone");
//                                                Log.d("Timezone",timezone.substring(timezone.indexOf('/')+1));
//                                                timezone=timezone.substring(timezone.indexOf('/')+1);
//
//                                                String countryURL="https://api.weatherapi.com/v1/current.json?key=5660084f7fdd4f4cb80140903212811&q="+addresses.get(0).getSubAdminArea()+"&aqi=no";
//
//                                                Request request1=new Request.Builder()
//                                                        .url(countryURL)
//                                                        .build();
//
//                                                client.newCall(request1).enqueue(new Callback() {
//                                                    @Override
//                                                    public void onFailure(Call call, IOException e) {
//
//                                                        Log.e("Inside onFailure",e.getMessage());
//
//                                                    }
//
//                                                    @Override
//                                                    public void onResponse(Call call, Response response) throws IOException {
//
//                                                        if(response.isSuccessful())
//                                                        {
//
//
//                                                            String res1=response.body().string();
//
//                                                            WeatherActivity.this.runOnUiThread(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//
//                                                                    try {
//
//                                                                        JSONObject jsonObject1=new JSONObject(res1);
//                                                                        JSONObject current=jsonObject1.getJSONObject("current");
//
//                                                                        int is_day=current.getInt("is_day");
//
//                                                                        SharedPreferences.Editor editor=sp.edit();
//                                                                        if(is_day==0)
//                                                                        {
//                                                                            dayNightMode.setImageResource(R.drawable.night_mode);
//                                                                            nightMode();
//
//                                                                            editor.putString("bgMode","0");
//                                                                        }
//                                                                        else
//                                                                        {
//                                                                            dayNightMode.setImageResource(R.drawable.day_mode);
//                                                                            dayMode();
//
//                                                                            editor.putString("bgMode","1");
//                                                                        }
//                                                                        editor.apply();
//
//                                                                        int temp_c= (int) current.getDouble("temp_c");
//
//                                                                        temp.setText(temp_c+"\u2103");
//
//                                                                        JSONObject condition=current.getJSONObject("condition");
//                                                                        String text=condition.getString("text");
//                                                                        skyType.setText(text);
//
//                                                                        if(text.equalsIgnoreCase("Sunny"))
//                                                                        {
//                                                                            weather.setImageResource(R.drawable.sun);
//                                                                        }
//                                                                        else if(text.equalsIgnoreCase("Clear"))
//                                                                        {
//                                                                            weather.setImageResource(R.drawable.moon_clear);
//                                                                        }
//                                                                        else
//                                                                        {
//                                                                            if(is_day==0)
//                                                                            {
//                                                                                weather.setImageResource(R.drawable.mist);
//                                                                            }
//                                                                            else
//                                                                            {
//                                                                                weather.setImageResource(R.drawable.cloudy);
//                                                                            }
//                                                                        }
//
//                                                                        String wind_mphSTR=current.getString("wind_mph");
//                                                                        int pressure_mbSTR= (int) current.getDouble("pressure_mb");
//                                                                        String humiditySTR=current.getString("humidity");
//
//                                                                        String pressure_two_decimal=String.format("%.2f",pressure_mbSTR*0.750062);
//
//                                                                        pressure.setText(pressure_two_decimal+" mmhg");
//                                                                        humidity.setText(humiditySTR+"%");
//                                                                        windSpeed.setText(wind_mphSTR+" mph");
//
//                                                                        progressDialog.dismiss();
//
//
//                                                                    } catch (JSONException e) {
//                                                                        e.printStackTrace();
//                                                                    }
//
//                                                                }
//                                                            });
//
//
//                                                        }
//
//                                                    }
//                                                });
//
//
//                                            } catch (JSONException e) {
//                                                Log.e("Exception",e.getMessage());
//                                            }
//
//                                        }
//                                    });
//
//                                }
//
//                            }
//                        });


                        //locText.setText(details);


                        String countryURL="https://api.weatherapi.com/v1/current.json?key=5660084f7fdd4f4cb80140903212811&q="+lat+","+lon+"&aqi=no";
                        Log.d("countryURL",countryURL);

                        setValues(countryURL);

                        Log.d("Details",details);

                    } catch (IOException e) {
                        Log.e("Exception",e.getMessage());
                    }

                }

            }
        });

    }

    public void setValues(String url)
    {

        Request request=new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.e("Inside onFailure",e.getMessage());

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful())
                {


                    String res=response.body().string();

                    WeatherActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                JSONObject jsonObject=new JSONObject(res);

                                JSONObject location=jsonObject.getJSONObject("location");
                                String name=location.getString("name");
                                Log.d("NAME",name);

                                JSONObject current=jsonObject.getJSONObject("current");

                                int is_day=current.getInt("is_day");

                                SharedPreferences.Editor editor=sp.edit();
                                if(is_day==0)
                                {
                                    dayNightMode.setImageResource(R.drawable.night_mode);
                                    nightMode();

                                    editor.putString("bgMode","0");
                                }
                                else
                                {
                                    dayNightMode.setImageResource(R.drawable.day_mode);
                                    dayMode();

                                    editor.putString("bgMode","1");
                                }
                                editor.apply();

                                int temp_c= (int) current.getDouble("temp_c");
                                Typeface face = Typeface.createFromAsset(getAssets(),
                                        "font/aladin.ttf");

                                temp.setTypeface(face);
                                temp.setText(temp_c+"\u2103");

                                JSONObject condition=current.getJSONObject("condition");
                                String text=condition.getString("text");
                                skyType.setText(text);
                                text=text.toLowerCase();

                                if(text.contains("sunny"))
                                {
                                    if(temp_c<=22)
                                    {
                                        weather.setImageResource(R.drawable.mist);
                                    }
                                    else
                                    {
                                        weather.setImageResource(R.drawable.sun);
                                    }
                                }
                                else if(text.contains("mist") || text.contains("fog"))
                                {
                                    weather.setImageResource(R.drawable.mist);
                                }
                                else if(text.contains("clear"))
                                {
                                    if(temp_c<=20)
                                    {
                                        weather.setImageResource(R.drawable.mist);
                                    }
                                    else
                                    {
                                        weather.setImageResource(R.drawable.moon_clear);
                                    }
                                }
                                else if(text.contains("drizzle"))
                                {
                                    if(is_day==0)
                                    {
                                        weather.setImageResource(R.drawable.drizzle_night);
                                    }
                                    else
                                    {
                                        weather.setImageResource(R.drawable.morning_drizzle);
                                    }
                                }
                                else if(text.contains("rain"))
                                {
                                    if(is_day==0)
                                    {
                                        if(text.contains("light") || text.contains("patchy"))
                                        {
                                            weather.setImageResource(R.drawable.light_rain_night);
                                        }
                                        else if(text.contains("moderate"))
                                        {
                                            weather.setImageResource(R.drawable.moderate_rain_night);
                                        }
                                        else
                                        {
                                            if(text.contains("heavy"))
                                            {
                                                weather.setImageResource(R.drawable.heavy_rain_night);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        if(text.contains("light") || text.contains("patchy"))
                                        {
                                            weather.setImageResource(R.drawable.light_rain);
                                        }
                                        else if(text.contains("moderate"))
                                        {
                                            weather.setImageResource(R.drawable.moderate_rain);
                                        }
                                        else
                                        {
                                            if(text.contains("heavy"))
                                            {
                                                weather.setImageResource(R.drawable.heavy_rain);
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    if(is_day==0)
                                    {
                                        weather.setImageResource(R.drawable.mist);
                                    }
                                    else
                                    {
                                        weather.setImageResource(R.drawable.cloudy);
                                    }
                                }

                                String wind_mphSTR=current.getString("wind_mph");
                                int pressure_mbSTR= (int) current.getDouble("pressure_mb");
                                String humiditySTR=current.getString("humidity");

                                String pressure_two_decimal=String.format("%.2f",pressure_mbSTR*0.750062);

                                pressure.setText(pressure_two_decimal+" mmhg");
                                humidity.setText(humiditySTR+"%");
                                windSpeed.setText(wind_mphSTR+" mph");

                                progressDialog.dismiss();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });


                }

            }
        });

    }

    public static String epochToDate(long epoch)
    {

        SimpleDateFormat sdf=new SimpleDateFormat("EEE, MMMM dd hh:mm a");
        Date date=new Date(epoch);
        String newDate=sdf.format(date);

        return newDate;

    }


    public void dayMode()
    {

        city.setTextColor(Color.BLACK);
        country.setTextColor(Color.DKGRAY);
        dateTime.setTextColor(Color.BLACK);
        temp.setTextColor(Color.BLACK);
        skyType.setTextColor(Color.BLACK);
        pressure.setTextColor(Color.BLACK);
        humidity.setTextColor(Color.BLACK);
        windSpeed.setTextColor(Color.BLACK);

        presText.setTextColor(Color.DKGRAY);
        humText.setTextColor(Color.DKGRAY);
        windText.setTextColor(Color.DKGRAY);

    }

    public void nightMode()
    {

        city.setTextColor(Color.WHITE);
        country.setTextColor(Color.LTGRAY);
        dateTime.setTextColor(Color.WHITE);
        temp.setTextColor(Color.WHITE);
        skyType.setTextColor(Color.WHITE);
        pressure.setTextColor(Color.WHITE);
        humidity.setTextColor(Color.WHITE);
        windSpeed.setTextColor(Color.WHITE);

        presText.setTextColor(Color.LTGRAY);
        humText.setTextColor(Color.LTGRAY);
        windText.setTextColor(Color.LTGRAY);

    }

}