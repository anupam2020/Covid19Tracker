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
import android.widget.ImageView;
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

                        url="https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=minutely,hourly&appid=66b871b322375297071a645567414648";

                        request=new Request.Builder()
                                .url(url)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("on Failure",e.getMessage());
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
                                                JSONArray daily=jsonObject.getJSONArray("daily");
                                                Log.d("Daily Size", String.valueOf(daily.length()));

                                                JSONObject zero=daily.getJSONObject(0);

                                                JSONObject temp=zero.getJSONObject("temp");

                                                double morning=temp.getDouble("morn");
                                                double afternoon=temp.getDouble("max");
                                                double evening=temp.getDouble("eve");
                                                double night=temp.getDouble("night");

                                                int mTemp=(int)(morning-273.15);
                                                int aTemp=(int)(afternoon-273.15);
                                                int eTemp=(int)(evening-273.15);
                                                int nTemp=(int)(night-273.15);

                                                morningTemp.setText(mTemp+"\u2103");
                                                afternoonTemp.setText(aTemp+"\u2103");
                                                eveningTemp.setText(eTemp+"\u2103");
                                                nightTemp.setText(nTemp+"\u2103");

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    });

                                }

                            }
                        });


                    } catch (IOException e) {
                        e.printStackTrace();
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