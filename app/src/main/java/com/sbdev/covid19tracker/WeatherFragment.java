package com.sbdev.covid19tracker;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherFragment extends Fragment {

//    private TextView locText,city,country,dateTime,temp,skyType,pressure,humidity,windSpeed;
//
//    private ImageView weather;
//
//    private FusedLocationProviderClient fusedLocationProviderClient;
//
//    private OkHttpClient client;
//
//    private Request request;
//
//    private String url,dateString;
//
//    private SimpleDateFormat simpleDateFormat;
//
//    private Date date;
//
//    private ProgressDialog progressDialog;
//
//    private NestedScrollView nestedScrollView;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        nestedScrollView=view.findViewById(R.id.nestedScrollView);
//        nestedScrollView.setBackground(getResources().getDrawable(R.drawable.night_mode));

//        locText = view.findViewById(R.id.locationText);
//
//        city=view.findViewById(R.id.cityText);
//        country=view.findViewById(R.id.countryText);
//        dateTime=view.findViewById(R.id.dateTimeText);
//        temp=view.findViewById(R.id.tempText);
//        skyType=view.findViewById(R.id.skyTypeText);
//        pressure=view.findViewById(R.id.pressureValue);
//        humidity=view.findViewById(R.id.humidityValue);
//        windSpeed=view.findViewById(R.id.windValue);
//
//        weather=view.findViewById(R.id.weatherImg);
//
//        simpleDateFormat=new SimpleDateFormat("EEE, MMMM dd, hh:mm a");
//        date=new Date();
//        dateString=simpleDateFormat.format(date);
//
//
//        progressDialog=new ProgressDialog(getActivity());
//
//        progressDialog.show();
//        progressDialog.setContentView(R.layout.loading_bg);
//        //progressDialog.setCancelable(false);
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//
//        client=new OkHttpClient();
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
//
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            getLocation();
//
//        } else {
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
//        }




    }

//    private void getLocation() {
//
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//
//                Location location=task.getResult();
//
//                if(location!=null)
//                {
//
//                    Geocoder geocoder=new Geocoder(getActivity(), Locale.getDefault());
//
//                    try {
//
//                        List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
//
//                        String details="Country Name: "+addresses.get(0).getCountryName()+"\n"+
//                                "Locality: "+addresses.get(0).getLocality()+"\n"+
//                                "Lat: "+(float)addresses.get(0).getLatitude()+"\n"+
//                                "Lon: "+(float)addresses.get(0).getLongitude()+"\n"+
//                                "Admin Area: "+addresses.get(0).getAdminArea()+"\n"+
//                                "Sub Admin Area: "+addresses.get(0).getSubAdminArea();
//
//                        city.setText(addresses.get(0).getLocality());
//                        country.setText(addresses.get(0).getAdminArea()+", "+addresses.get(0).getCountryName());
//                        dateTime.setText(dateString);
//
//                        int lat= (int) addresses.get(0).getLatitude();
//                        int lon= (int) addresses.get(0).getLongitude();
//
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
//                                    getActivity().runOnUiThread(new Runnable() {
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
//                                                String countryURL="https://api.weatherapi.com/v1/current.json?key=5660084f7fdd4f4cb80140903212811&q="+timezone+"&aqi=no";
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
//                                                            progressDialog.dismiss();
//
//                                                            String res1=response.body().string();
//
//                                                            getActivity().runOnUiThread(new Runnable() {
//                                                                @Override
//                                                                public void run() {
//
//                                                                    try {
//
//                                                                        JSONObject jsonObject1=new JSONObject(res1);
//                                                                        JSONObject current=jsonObject1.getJSONObject("current");
//                                                                        int temp_c= (int) current.getDouble("temp_c");
//
//                                                                        temp.setText(temp_c+"\u2103");
//
//                                                                        JSONObject condition=current.getJSONObject("condition");
//                                                                        String text=condition.getString("text");
//                                                                        skyType.setText(text);
//
//                                                                        if(text.equalsIgnoreCase("Mist"))
//                                                                        {
//                                                                            weather.setImageResource(R.drawable.mist);
//                                                                        }
//                                                                        else if(text.equalsIgnoreCase("Sunny"))
//                                                                        {
//                                                                            weather.setImageResource(R.drawable.sun);
//                                                                        }
//                                                                        else if(text.equalsIgnoreCase("Clear"))
//                                                                        {
//                                                                            weather.setImageResource(R.drawable.clear_sky);
//                                                                        }
//                                                                        else
//                                                                        {
//                                                                            weather.setImageResource(R.drawable.cloudy);
//                                                                        }
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
//
//
//                        //locText.setText(details);
//
//                        Log.d("Details",details);
//
//                    } catch (IOException e) {
//                        Log.e("Exception",e.getMessage());
//                    }
//
//                }
//
//            }
//        });
//
//    }
//
//    public static String epochToDate(long epoch)
//    {
//
//        SimpleDateFormat sdf=new SimpleDateFormat("EEE, MMMM dd hh:mm a");
//        Date date=new Date(epoch);
//        String newDate=sdf.format(date);
//
//        return newDate;
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }
}