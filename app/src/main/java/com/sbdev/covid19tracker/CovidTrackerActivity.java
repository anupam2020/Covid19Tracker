package com.sbdev.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    private ArrayList<String> countryList;
    private ArrayList<String> isoList;

    private EditText text;
    private Button submit,logout;

    private TextView textView;

    private RelativeLayout rootLayout;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_tracker);


        text=findViewById(R.id.editText);
        submit=findViewById(R.id.btnSubmit);
        logout=findViewById(R.id.covidLogout);
        textView=findViewById(R.id.textView);

        rootLayout=findViewById(R.id.covidTrackerRelative);

        firebaseAuth=FirebaseAuth.getInstance();

        countryList=new ArrayList<>();
        isoList=new ArrayList<>();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com/api/npm-covid-data/countries-name-ordered")
                .get()
                .addHeader("x-rapidapi-host", "vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e8f6c57650msh666fef2e3a110b5p13b950jsn4359d608e124")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.e("Failure Exception",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful())
                {

                    countryList.clear();
                    isoList.clear();

                    String res=response.body().string();

                    CovidTrackerActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                JSONArray jsonArray=new JSONArray(res);
                                for(int i=0;i<219;i++)
                                {

                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String country=jsonObject.getString("Country");
                                    countryList.add(country.toLowerCase());
                                    String iso=jsonObject.getString("ThreeLetterSymbol");
                                    isoList.add(iso.toLowerCase());

                                }

                                for(int i=0;i<219;i++)
                                {
                                    Log.d("Country values",countryList.get(i));
                                    Log.d("ISO values",isoList.get(i));
                                }

                            } catch (JSONException e) {
                                Log.e("Catch Exception 1",e.getMessage());
                            }

                        }
                    });

                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textView.setText("");

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rootLayout.getWindowToken(), 0);

                Log.d("CountryList size", String.valueOf(countryList.size()));
                Log.d("IsoList size", String.valueOf(isoList.size()));

                String country=text.getText().toString();// Bosnia and Harzegovina

                if(!country.isEmpty())
                {
                    int count=0;
                    if(countryList.contains(country.toLowerCase()) || isoList.contains(country.toLowerCase()))
                    {
                        int posCountry;
                        String isoCountry="";
                        String newCountry="";
                        String userCountry="";
                        if(country.equalsIgnoreCase("USA"))
                        {
                            isoCountry="usa";
                            userCountry="USA";
                        }
                        else
                        {
                            if(isoList.contains(country.toLowerCase()))
                            {
                                newCountry=countryList.get(isoList.indexOf(country.toLowerCase()));// western sahara
                                isoCountry=country.toLowerCase();
                                newCountry=" "+newCountry;

                                for(int i=0;i<newCountry.length();i++)
                                {
                                    if(newCountry.charAt(i)==' ')
                                    {
                                        if(count==0)
                                        {
                                            userCountry=userCountry+newCountry.toUpperCase().charAt(i+1);
                                        }
                                        else
                                        {
                                            userCountry=userCountry+" "+newCountry.toUpperCase().charAt(i+1);
                                        }
                                        count++;
                                        i++;
                                    }
                                    else
                                    {
                                        userCountry=userCountry+newCountry.toLowerCase().charAt(i);
                                    }

                                }



                                Log.d("User Country",userCountry);

                                Log.d("NEW COUNTRY",newCountry);
                            }

                            if(countryList.contains(country.toLowerCase()))
                            {
                                posCountry=countryList.indexOf(country.toLowerCase());
                                isoCountry=isoList.get(posCountry);
                                Log.d("INDEX", String.valueOf(posCountry));

                                newCountry=" "+country;// $papua$new$guinea
                                // Western sahara
                                for(int i=0;i<newCountry.length();i++)
                                {
                                    if(newCountry.charAt(i)==' ')
                                    {
                                        if(count==0)
                                        {
                                            userCountry=userCountry+newCountry.toUpperCase().charAt(i+1);
                                        }
                                        else
                                        {
                                            userCountry=userCountry+" "+newCountry.toUpperCase().charAt(i+1);
                                        }
                                        count++;
                                        i++;
                                    }
                                    else
                                    {
                                        userCountry=userCountry+newCountry.toLowerCase().charAt(i);
                                    }

                                }

                            }
                        }


                        if(count>1)
                        {
                            StringTokenizer stringTokenizer=new StringTokenizer(userCountry);
                            String firstString=stringTokenizer.nextToken();
                            String secondString=stringTokenizer.nextToken();

                            if(secondString.equalsIgnoreCase("And"))
                            {
                                StringBuilder stringBuilder=new StringBuilder(userCountry);
                                stringBuilder.replace(userCountry.indexOf(' '),userCountry.lastIndexOf(' ')," and");
                                Log.d("StringBuilder", String.valueOf(stringBuilder));

                                userCountry=stringBuilder.toString();
                            }
                        }

                        Log.d("User Country",userCountry);

                        Log.d("isoCountry",isoCountry);
                        Log.d("User Input",newCountry);

                        OkHttpClient dataClient = new OkHttpClient();

                        Request dataRequest = new Request.Builder()
                                .url("https://vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com/api/npm-covid-data/country-report-iso-based/"+userCountry+"/"+isoCountry)
                                .get()
                                .addHeader("x-rapidapi-host", "vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com")
                                .addHeader("x-rapidapi-key", "e8f6c57650msh666fef2e3a110b5p13b950jsn4359d608e124")
                                .build();

                        dataClient.newCall(dataRequest).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                                Log.e("Failure Exception",e.getMessage());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                if(response.isSuccessful())
                                {

                                    String dataRes=response.body().string();

                                    CovidTrackerActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            try {

                                                JSONArray jsonArray=new JSONArray(dataRes);

                                                JSONObject jsonObject=jsonArray.getJSONObject(0);
                                                String totalCases=jsonObject.getString("TotalCases");
                                                String newCases=jsonObject.getString("NewCases");
                                                String totalDeaths=jsonObject.getString("TotalDeaths");
                                                String newDeaths=jsonObject.getString("NewDeaths");
                                                String totalRecovered=jsonObject.getString("TotalRecovered");
                                                String newRecovered=jsonObject.getString("NewRecovered");
                                                textView.setText("Total Cases: "+totalCases+"\n"+
                                                        "New Cases: "+newCases+"\n"+
                                                        "Total Deaths: "+totalDeaths+"\n"+
                                                        "New Deaths: "+newDeaths+"\n"+
                                                        "Total Recovered: "+totalRecovered+"\n"+
                                                        "New Recovered: "+newRecovered);

                                            } catch (JSONException e) {
                                                Log.e("Catch Exception 2",e.getMessage());
                                            }


                                        }
                                    });

                                }

                            }
                        });


                    }
                    else
                    {
                        Toast.makeText(CovidTrackerActivity.this, "No country found!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                finishAffinity();
                startActivity(new Intent(CovidTrackerActivity.this,MainActivity.class));
            }
        });


    }

    @Override
    public void onBackPressed() {

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