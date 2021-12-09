package com.sbdev.covid19tracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.textfield.TextInputLayout;
import com.jaeger.library.StatusBarUtil;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AllCountryWeather extends AppCompatActivity {

    private static final int REQUEST_CHECK_SETTINGS = 1001;

    private LocationRequest locationRequest;

    private SharedPreferences sp;

    private String mode;

    private String SHARED_PREFS="SHARED_PREFS";

    private ImageView day_night_mode,weather;

    private TextInputLayout layout;

    private EditText editText;

    private OkHttpClient client;

    private Request request;

    private Button search;

    private TextView city,country,pressure,wind,humidity,temp,temp_type;
    private TextView presText,humText,windText;

    private ProgressDialog dialog;

    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_country_weather);


        StatusBarUtil.setTransparentForImageView(AllCountryWeather.this,null);

        day_night_mode=findViewById(R.id.allWeatherMode);
        layout=findViewById(R.id.allWeatherLayout);
        editText=findViewById(R.id.allWeatherEditText);
        search=findViewById(R.id.allWeatherButton);

        city=findViewById(R.id.allWeatherCityText);
        country=findViewById(R.id.allWeatherCountryText);
        pressure=findViewById(R.id.allWeatherPressureValue);
        wind=findViewById(R.id.allWeatherWindValue);
        humidity=findViewById(R.id.allWeatherHumidityValue);
        temp=findViewById(R.id.allWeatherTempText);
        temp_type=findViewById(R.id.allWeatherSkyTypeText);

        presText=findViewById(R.id.allWeatherPressureText);
        humText=findViewById(R.id.allWeatherHumidityText);
        windText=findViewById(R.id.allWeatherWindText);

        weather=findViewById(R.id.allWeatherWeatherImg);

        relativeLayout=findViewById(R.id.allWeatherSwipeRelative);

        dialog=new ProgressDialog(AllCountryWeather.this);

        layout.setBoxBackgroundColorResource(R.color.white);

        client=new OkHttpClient();

        sp=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);

        mode=sp.getString("bgMode","1");
        if(mode.equals("0"))
        {
            day_night_mode.setImageResource(R.drawable.night_mode);
            nightMode();
        }
        else
        {
            day_night_mode.setImageResource(R.drawable.day_mode);
            dayMode();
        }


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) AllCountryWeather.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                dialog.show();
                dialog.setContentView(R.layout.loading_bg);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                String strCity=editText.getText().toString();

                if(strCity.isEmpty())
                {
                    dialog.dismiss();
                    DynamicToast.makeWarning(AllCountryWeather.this,"Field cannot be empty!",2000).show();
                }
                else
                {
                    getWeather(strCity);
                }

            }
        });

    }

    private void getWeather(String strCity)
    {

        String url="http://api.weatherapi.com/v1/current.json?key=5660084f7fdd4f4cb80140903212811&q="+strCity+"&aqi=no";

        request=new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                dialog.dismiss();
                Log.e("onFailure",e.getMessage());
                AllCountryWeather.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DynamicToast.makeError(AllCountryWeather.this,e.getMessage(),2000).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful())
                {

                    String res=response.body().string();

                    AllCountryWeather.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                JSONObject jsonObject=new JSONObject(res);
                                JSONObject location=jsonObject.getJSONObject("location");

                                String strName=location.getString("name");
                                city.setText(strName);
                                String strCountry=location.getString("country");
                                String strContinent=location.getString("tz_id");
                                strContinent=strContinent.substring(0,strContinent.indexOf('/'));
                                country.setText(strCountry+", "+strContinent);

                                JSONObject current=jsonObject.getJSONObject("current");
                                int temp_c= (int) current.getDouble("temp_c");
                                Typeface face = Typeface.createFromAsset(getAssets(),
                                        "font/aladin.ttf");
                                temp.setTypeface(face);
                                temp.setText(temp_c+"\u2103");

                                int is_day=current.getInt("is_day");

                                SharedPreferences.Editor editor=sp.edit();
                                if(is_day==0)
                                {
                                    day_night_mode.setImageResource(R.drawable.night_mode);
                                    nightMode();

                                    editor.putString("bgMode","0");
                                }
                                else
                                {
                                    day_night_mode.setImageResource(R.drawable.day_mode);
                                    dayMode();

                                    editor.putString("bgMode","1");
                                }
                                editor.apply();

                                JSONObject condition=current.getJSONObject("condition");
                                String text=condition.getString("text");
                                temp_type.setText(text);
                                text=text.toLowerCase();

                                if(text.contains("sunny"))
                                {
                                    if(temp_c<=22)
                                    {
                                        weather.setImageResource(R.drawable.mist);
                                    }
                                    else
                                    {
                                        weather.setImageResource(R.drawable.clear_sky);
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

                                String strWind=current.getString("wind_mph");
                                wind.setText(strWind+" mph");

                                int strHumidity=current.getInt("humidity");
                                humidity.setText(strHumidity+"%");

                                int strPressure=current.getInt("pressure_mb");
                                String pressure_two_decimal=String.format("%.2f",strPressure*0.750062);
                                pressure.setText(pressure_two_decimal+" mmhg");

                                relativeLayout.setVisibility(View.VISIBLE);

                                dialog.dismiss();

                            } catch (JSONException e) {
                                dialog.dismiss();
                                Log.e("CATCH",e.getMessage());
                                DynamicToast.makeWarning(AllCountryWeather.this,e.getMessage(),2000).show();
                            }

                        }
                    });

                }
                else
                {

                    AllCountryWeather.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            dialog.dismiss();
                            DynamicToast.makeError(AllCountryWeather.this,response.message(),2000).show();

                        }
                    });
                }

            }
        });

    }


    public void dayMode()
    {

        city.setTextColor(Color.BLACK);
        country.setTextColor(Color.DKGRAY);
        temp.setTextColor(Color.BLACK);
        temp_type.setTextColor(Color.BLACK);
        pressure.setTextColor(Color.BLACK);
        humidity.setTextColor(Color.BLACK);
        wind.setTextColor(Color.BLACK);

        presText.setTextColor(Color.DKGRAY);
        humText.setTextColor(Color.DKGRAY);
        windText.setTextColor(Color.DKGRAY);

    }

    public void nightMode()
    {

        city.setTextColor(Color.WHITE);
        country.setTextColor(Color.LTGRAY);
        temp.setTextColor(Color.WHITE);
        temp_type.setTextColor(Color.WHITE);
        pressure.setTextColor(Color.WHITE);
        humidity.setTextColor(Color.WHITE);
        wind.setTextColor(Color.WHITE);

        presText.setTextColor(Color.LTGRAY);
        humText.setTextColor(Color.LTGRAY);
        windText.setTextColor(Color.LTGRAY);

    }

}