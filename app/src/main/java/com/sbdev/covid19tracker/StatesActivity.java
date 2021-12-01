package com.sbdev.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StatesActivity extends AppCompatActivity {

    private TextInputEditText search;
    private RecyclerView recyclerView;
    private StatesAdapter adapter;
    private ArrayList<StatesModel> arrayList;

    private ArrayList<StatesModel> filterList;

    private ProgressDialog dialog;

    private String country,state,confirmed,deaths,lastChecked;

    private TextView provinceText;

    private ImageView backImg;

    private ArrayList<String> countryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_states);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        search=findViewById(R.id.statesSearch);
        recyclerView=findViewById(R.id.statesRecyclerView);
        provinceText=findViewById(R.id.statesProvinceText);
        backImg=findViewById(R.id.statesBack);

        arrayList=new ArrayList<>();
        filterList=new ArrayList<>();
        countryList=new ArrayList<>();

        dialog=new ProgressDialog(StatesActivity.this);

        adapter=new StatesAdapter(arrayList,StatesActivity.this);
        recyclerView.setAdapter(adapter);

        dialog.show();
        dialog.setContentView(R.layout.loading_bg);
        //dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        country=getIntent().getStringExtra("countryName");
        Log.d("Country Name",country);

        provinceText.setText("Provinces of "+country);


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://covid-19-coronavirus-statistics.p.rapidapi.com/v1/stats?country="+country)
                .get()
                .addHeader("x-rapidapi-host", "covid-19-coronavirus-statistics.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e8f6c57650msh666fef2e3a110b5p13b950jsn4359d608e124")
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

                    StatesActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                JSONObject jsonObject=new JSONObject(res);
                                JSONObject data=jsonObject.getJSONObject("data");
                                JSONArray covid19Stats=data.getJSONArray("covid19Stats");

                                countryList.clear();

                                for(int i=0;i<covid19Stats.length();i++)
                                {
                                    JSONObject index=covid19Stats.getJSONObject(i);

                                    state=index.getString("province");
                                    confirmed=index.getString("confirmed");
                                    deaths=index.getString("deaths");
                                    lastChecked=index.getString("lastUpdate");

                                    if(state.equals("null") || state.equals("Unknown"))
                                    {
                                        continue;
                                    }
                                    else
                                    {
                                        if(countryList.contains(state))
                                        {
                                           continue;
                                        }
                                        else
                                        {
                                            arrayList.add(new StatesModel(state,getFormattedAmount(Integer.parseInt(confirmed)),getFormattedAmount(Integer.parseInt(deaths))));
                                            countryList.add(state);
                                        }

                                    }
                                }

                                adapter.notifyDataSetChanged();

                                dialog.dismiss();


                            } catch (JSONException e) {
                                Log.e("Catch",e.getMessage());
                            }

                        }
                    });

                }

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filterList.clear();

                if(s.toString().isEmpty())
                {
                    recyclerView.setAdapter(new StatesAdapter(arrayList,StatesActivity.this));
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    filter(s.toString());
                }
            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


    }

    private void filter(String state) {

        for(StatesModel model : arrayList)
        {
            if(model.getLocation().toLowerCase().contains(state.toLowerCase()))
            {
                filterList.add(model);
            }
        }

        recyclerView.setAdapter(new StatesAdapter(filterList,StatesActivity.this));
        adapter.notifyDataSetChanged();

    }

    private String getFormattedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

}