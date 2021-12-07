package com.sbdev.covid19tracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DailyFragment extends Fragment {

    private RecyclerView recyclerView;

    private DailyAdapter adapter;

    private ArrayList<DailyModel> arrayList;

    private String url="",start="",end="";

    private OkHttpClient client;

    private Request request;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private TextView dateRange;

    private ProgressBar progressBar;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.dailyRecycler);
        dateRange=view.findViewById(R.id.dailyText);
        progressBar=view.findViewById(R.id.dailyProgress);

        arrayList=new ArrayList<>();

        adapter=new DailyAdapter(getActivity(),arrayList);
        recyclerView.setAdapter(adapter);

        client=new OkHttpClient();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLocation();

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
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

                        Log.d("Latitude", String.valueOf(lat));
                        Log.d("Longitude", String.valueOf(lon));


                        url="https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=minutely,hourly&appid=66b871b322375297071a645567414648";

                        Log.d("URL",url);

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

                                    Log.d("Response","Successful");

                                    String res=response.body().string();

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            try {

                                                JSONObject jsonObject=new JSONObject(res);
                                                JSONArray daily=jsonObject.getJSONArray("daily");
                                                Log.d("Daily Size", String.valueOf(daily.length()));

                                                for(int i=0;i<daily.length();i++)
                                                {

                                                    JSONObject index=daily.getJSONObject(i);

                                                    long dt=index.getLong("dt");

                                                    if(i==0)
                                                    {
                                                        start=epochToDateMonthDate(dt*1000);
                                                    }
                                                    if(i==daily.length()-1)
                                                    {
                                                        end=epochToDateMonthDate(dt*1000);
                                                    }

                                                    JSONObject temp=index.getJSONObject("temp");
                                                    int maxTemp= (int) (temp.getDouble("max") - 273.15);
                                                    int minTemp= (int) (temp.getDouble("min") - 273.15);

                                                    JSONArray weather=index.getJSONArray("weather");
                                                    JSONObject weatherType=weather.getJSONObject(0);

                                                    String type=weatherType.getString("description");

                                                    arrayList.add(new DailyModel(epochToDate(dt*1000),maxTemp+"\u2103"+" / "+minTemp+"\u2103",type));

                                                }

                                                adapter.notifyDataSetChanged();

                                                dateRange.setText(start+" - "+end);

                                                progressBar.setVisibility(View.GONE);
                                                
                                            } catch (JSONException e) {
                                                Log.e("CATCH",e.getMessage());
                                            }

                                        }
                                    });

                                }
                                else
                                {
                                    Log.d("Response","Failed!");
                                }

                            }
                        });


                    }catch (IOException e) {
                        Log.e("Exception",e.getMessage());
                    }
                }
            }
        });
    }

    public String epochToDate(long epoch)
    {

        SimpleDateFormat sdf=new SimpleDateFormat("EEE, MMM dd");
        Date date=new Date(epoch);
        String newDate=sdf.format(date);

        return newDate;

    }

    public String epochToDateMonthDate(long epoch)
    {

        SimpleDateFormat sdf=new SimpleDateFormat("MMMM dd");
        Date date=new Date(epoch);
        String newDate=sdf.format(date);

        return newDate;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily, container, false);
    }

}