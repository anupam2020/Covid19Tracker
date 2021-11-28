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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherFragment extends Fragment {

    private TextView locText;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private OkHttpClient client;

    private Request request;

    String url;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locText = view.findViewById(R.id.locationText);

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

                Location location=task.getResult();

                if(location!=null)
                {

                    Geocoder geocoder=new Geocoder(getActivity(), Locale.getDefault());

                    try {

                        List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                        String details="Country Name: "+addresses.get(0).getCountryName()+"\n"+
                                "Locality: "+addresses.get(0).getLocality()+"\n"+
                                "Lat: "+(float)addresses.get(0).getLatitude()+"\n"+
                                "Lon: "+(float)addresses.get(0).getLongitude()+"\n"+
                                "Admin Area: "+addresses.get(0).getAdminArea()+"\n"+
                                "Sub Admin Area: "+addresses.get(0).getSubAdminArea();

                        int lat= (int) addresses.get(0).getLatitude();
                        int lon= (int) addresses.get(0).getLongitude();

                        url="https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=minutely&appid=66b871b322375297071a645567414648";

                        request=new Request.Builder()
                                .url(url)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                                Log.e("On Failure",e.getMessage());

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
                                                String timezone=jsonObject.getString("timezone");

                                                locText.setText(timezone.substring(timezone.indexOf('/')+1)+"\n"
                                                +addresses.get(0).getAdminArea()+", "+addresses.get(0).getCountryName());

                                            } catch (JSONException e) {
                                                Log.e("Exception",e.getMessage());
                                            }

                                        }
                                    });

                                }

                            }
                        });


                        //locText.setText(details);

                        Log.d("Details",details);

                    } catch (IOException e) {
                        Log.e("Exception",e.getMessage());
                    }

                }

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }
}