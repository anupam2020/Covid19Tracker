package com.sbdev.covid19tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AllCountryFragment extends Fragment {

    private TextInputEditText search;
    private RecyclerView recyclerView;
    private AllCountryAdapter adapter;
    private ArrayList<CountryModel> arrayList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        search=view.findViewById(R.id.countrySearch);
        recyclerView=view.findViewById(R.id.countryRecyclerView);

        arrayList=new ArrayList<>();

        adapter=new AllCountryAdapter(getActivity(),arrayList);
        recyclerView.setAdapter(adapter);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com/api/npm-covid-data/")
                .get()
                .addHeader("x-rapidapi-host", "vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com")
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

                    try {

                        JSONArray jsonArray=new JSONArray(res);
                        for(int i=1;i<=220;i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            String country=jsonObject.getString("Country");
                            String active=jsonObject.getString("ActiveCases");
                            String deaths=jsonObject.getString("TotalDeaths");
                            String recovered=jsonObject.getString("TotalRecovered");

                            arrayList.add(new CountryModel(country,active,deaths,recovered));
                            //adapter.notifyDataSetChanged();

                        }


                    } catch (JSONException e) {
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
        return inflater.inflate(R.layout.fragment_all_country, container, false);
    }
}