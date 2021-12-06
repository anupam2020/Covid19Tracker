package com.sbdev.covid19tracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class TodayFragment extends Fragment {

    private String url="";

    private TextView morningText,afternoonText,eveningText,nightText;

    private TextView morningTemp,afternoonTemp,eveningTemp,nightTemp;

    private ImageView morningImg,afternoonImg,eveningImg,nightImg;

    private OkHttpClient client;

    private Request request;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private RelativeLayout mCard,aCard,eCard,nCard;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        morningText=view.findViewById(R.id.morningText);
        morningTemp=view.findViewById(R.id.morningTemp);
        morningImg=view.findViewById(R.id.morningImg);

        afternoonText=view.findViewById(R.id.afternoonText);
        afternoonTemp=view.findViewById(R.id.afternoonTemp);
        afternoonImg=view.findViewById(R.id.afternoonImg);

        eveningText=view.findViewById(R.id.eveningText);
        eveningTemp=view.findViewById(R.id.eveningTemp);
        eveningImg=view.findViewById(R.id.eveningImg);

        nightText=view.findViewById(R.id.nightText);
        nightTemp=view.findViewById(R.id.nightTemp);
        nightImg=view.findViewById(R.id.nightImg);

        mCard=view.findViewById(R.id.morningCard);
        aCard=view.findViewById(R.id.afternoonCard);
        eCard=view.findViewById(R.id.eveningCard);
        nCard=view.findViewById(R.id.nightCard);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        client=new OkHttpClient();


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLocation();

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }


    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

                Location location = task.getResult();

                if (location != null) {

                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

                    try {

                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        String details = "Country Name: " + addresses.get(0).getCountryName() + "\n" +
                                "Locality: " + addresses.get(0).getLocality() + "\n" +
                                "Lat: " + (float) addresses.get(0).getLatitude() + "\n" +
                                "Lon: " + (float) addresses.get(0).getLongitude() + "\n" +
                                "Admin Area: " + addresses.get(0).getAdminArea() + "\n" +
                                "Sub Admin Area: " + addresses.get(0).getSubAdminArea();

                        double lat = addresses.get(0).getLatitude();
                        double lon = addresses.get(0).getLongitude();

                        url="https://api.weatherapi.com/v1/forecast.json?key=5660084f7fdd4f4cb80140903212811&q="+lat+","+lon+"&days=1&aqi=no&alerts=no";

                        request=new Request.Builder()
                                .url(url)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("onFailure",e.getMessage());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                if(response.isSuccessful())
                                {

                                    String res=response.body().string();

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            try {

                                                JSONObject jsonObject=new JSONObject(res);

                                                JSONObject forecast=jsonObject.getJSONObject("forecast");

                                                JSONArray forecastday=forecast.getJSONArray("forecastday");

                                                JSONObject forecastdayObject=forecastday.getJSONObject(0);

                                                JSONArray hour=forecastdayObject.getJSONArray("hour");

                                                for(int i=0;i<hour.length();i++)
                                                {
                                                    JSONObject hourObject=hour.getJSONObject(i);
                                                    int temp_c= (int) hourObject.getDouble("temp_c");
                                                    int isDay=hourObject.getInt("is_day");

                                                    JSONObject condition=hourObject.getJSONObject("condition");
                                                    String text=condition.getString("text");
                                                    text=text.toLowerCase();

                                                    if(i==7)
                                                    {
                                                        mCard.setBackgroundResource(R.drawable.day_mode);
                                                        if(text.contains("rain"))
                                                        {
                                                            if(text.contains("light") || text.contains("patchy"))
                                                            {
                                                                morningImg.setImageResource(R.drawable.light_rain);
                                                            }
                                                            else if(text.contains("moderate"))
                                                            {
                                                                morningImg.setImageResource(R.drawable.moderate_rain);
                                                            }
                                                            else
                                                            {
                                                                if(text.contains("heavy"))
                                                                {
                                                                    morningImg.setImageResource(R.drawable.heavy_rain);
                                                                }
                                                            }
                                                        }
                                                        else if(text.contains("drizzle"))
                                                        {
                                                            morningImg.setImageResource(R.drawable.morning_drizzle);
                                                        }
                                                        else if(text.contains("cloudy"))
                                                        {
                                                            if(temp_c<=22)
                                                            {
                                                                morningImg.setImageResource(R.drawable.mist);
                                                            }
                                                            else
                                                            {
                                                                morningImg.setImageResource(R.drawable.cloudy);
                                                            }
                                                        }
                                                        else if(text.contains("mist"))
                                                        {
                                                            morningImg.setImageResource(R.drawable.mist);
                                                        }
                                                        else
                                                        {
                                                            morningImg.setImageResource(R.drawable.sun);
                                                        }
                                                        morningTemp.setText(temp_c+"\u2103");
                                                    }
                                                    if(i==12)
                                                    {
                                                        aCard.setBackgroundResource(R.drawable.day_mode);
                                                        if(text.contains("rain"))
                                                        {
                                                            if(text.contains("light") || text.contains("patchy"))
                                                            {
                                                                afternoonImg.setImageResource(R.drawable.light_rain);
                                                            }
                                                            else if(text.contains("moderate"))
                                                            {
                                                                afternoonImg.setImageResource(R.drawable.moderate_rain);
                                                            }
                                                            else
                                                            {
                                                                if(text.contains("heavy"))
                                                                {
                                                                    afternoonImg.setImageResource(R.drawable.heavy_rain);
                                                                }
                                                            }
                                                        }
                                                        else if(text.contains("drizzle"))
                                                        {
                                                            afternoonImg.setImageResource(R.drawable.morning_drizzle);
                                                        }
                                                        else if(text.contains("cloudy"))
                                                        {
                                                            if(temp_c<=25)
                                                            {
                                                                afternoonImg.setImageResource(R.drawable.mist);
                                                            }
                                                            else
                                                            {
                                                                afternoonImg.setImageResource(R.drawable.cloudy);
                                                            }
                                                        }
                                                        else if(text.contains("mist"))
                                                        {
                                                            afternoonImg.setImageResource(R.drawable.mist);
                                                        }
                                                        else
                                                        {
                                                            afternoonImg.setImageResource(R.drawable.sun);
                                                        }
                                                        afternoonTemp.setText(temp_c+"\u2103");
                                                    }
                                                    if(i==17)
                                                    {
                                                        eCard.setBackgroundResource(R.drawable.night_mode);
                                                        if(text.contains("rain"))
                                                        {
                                                            if(text.contains("light") || text.contains("patchy"))
                                                            {
                                                                eveningImg.setImageResource(R.drawable.light_rain_night);
                                                            }
                                                            else if(text.contains("moderate"))
                                                            {
                                                                eveningImg.setImageResource(R.drawable.moderate_rain_night);
                                                            }
                                                            else
                                                            {
                                                                if(text.contains("heavy"))
                                                                {
                                                                    eveningImg.setImageResource(R.drawable.heavy_rain_night);
                                                                }
                                                            }
                                                        }
                                                        else if(text.contains("drizzle"))
                                                        {
                                                            eveningImg.setImageResource(R.drawable.drizzle_night);
                                                        }
                                                        else if(text.contains("cloudy"))
                                                        {
                                                            if(temp_c<=20)
                                                            {
                                                                eveningImg.setImageResource(R.drawable.mist);
                                                            }
                                                            else
                                                            {
                                                                eveningImg.setImageResource(R.drawable.moon_clear);
                                                            }
                                                        }
                                                        else if(text.contains("mist"))
                                                        {
                                                            eveningImg.setImageResource(R.drawable.mist);
                                                        }
                                                        else
                                                        {
                                                            eveningImg.setImageResource(R.drawable.moon_clear);
                                                        }
                                                        eveningTemp.setText(temp_c+"\u2103");
                                                        eveningTemp.setTextColor(Color.WHITE);
                                                        eveningText.setTextColor(Color.WHITE);

                                                    }
                                                    if(i==21)
                                                    {
                                                        nCard.setBackgroundResource(R.drawable.night_mode);
                                                        if(text.contains("rain"))
                                                        {
                                                            if(text.contains("light") || text.contains("patchy"))
                                                            {
                                                                nightImg.setImageResource(R.drawable.light_rain_night);
                                                            }
                                                            else if(text.contains("moderate"))
                                                            {
                                                                nightImg.setImageResource(R.drawable.moderate_rain_night);
                                                            }
                                                            else
                                                            {
                                                                if(text.contains("heavy"))
                                                                {
                                                                    nightImg.setImageResource(R.drawable.heavy_rain_night);
                                                                }
                                                            }
                                                        }
                                                        else if(text.contains("drizzle"))
                                                        {
                                                            nightImg.setImageResource(R.drawable.drizzle_night);
                                                        }
                                                        else if(text.contains("cloudy"))
                                                        {
                                                            if(temp_c<=20)
                                                            {
                                                                nightImg.setImageResource(R.drawable.mist);
                                                            }
                                                            else
                                                            {
                                                                nightImg.setImageResource(R.drawable.moon_clear);
                                                            }
                                                        }
                                                        else if(text.contains("mist") || text.contains("fog"))
                                                        {
                                                            nightImg.setImageResource(R.drawable.mist);
                                                        }
                                                        else
                                                        {
                                                            nightImg.setImageResource(R.drawable.moon_clear);
                                                        }
                                                        nightTemp.setText(temp_c+"\u2103");
                                                        nightTemp.setTextColor(Color.WHITE);
                                                        nightText.setTextColor(Color.WHITE);

                                                    }

                                                }

                                            } catch (JSONException e) {
                                                Log.e("CATCH",e.getMessage());
                                            }

                                        }
                                    });

                                }

                            }
                        });


                    } catch (IOException e) {
                        Log.e("CATCH",e.getMessage());
                    }


                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }
}