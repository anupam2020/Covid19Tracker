package com.sbdev.covid19tracker;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

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

public class AllCountryFragment extends Fragment {

    private TextInputEditText search;
    private RecyclerView recyclerView;
    private AllCountryAdapter adapter;
    private ArrayList<CountryModel> arrayList;

    private ArrayList<CountryModel> filterList;

    private ProgressDialog dialog;

    private String url;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        search=view.findViewById(R.id.countrySearch);
        recyclerView=view.findViewById(R.id.countryRecyclerView);

        arrayList=new ArrayList<>();
        filterList=new ArrayList<>();

        url="https://corona.lmao.ninja/v2/countries?sort";

        dialog=new ProgressDialog(getActivity());

        adapter=new AllCountryAdapter(getActivity(),arrayList);
        recyclerView.setAdapter(adapter);

        dialog.show();
        dialog.setContentView(R.layout.loading_bg);
        //dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        OkHttpClient client = new OkHttpClient();

//        Request request = new Request.Builder()
//                .url("https://vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com/api/npm-covid-data/")
//                .get()
//                .addHeader("x-rapidapi-host", "vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com")
//                .addHeader("x-rapidapi-key", "e8f6c57650msh666fef2e3a110b5p13b950jsn4359d608e124")
//                .build();


        Request request=new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                if(getActivity()==null)
                {
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        dialog.dismiss();
                        Log.e("OnFailure",e.getMessage());
                        DynamicToast.makeError(getActivity(),e.getMessage(),2000).show();

                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if(response.isSuccessful())
                {

                    String res=response.body().string();

                    if(getActivity()==null)
                    {
                        return;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {

                                JSONArray jsonArray=new JSONArray(res);
                                Log.d("Array Size", String.valueOf(jsonArray.length()));
                                for(int i=1;i<jsonArray.length();i++)
                                {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                                        String country = jsonObject.getString("country");

                                        JSONObject countryInfo=jsonObject.getJSONObject("countryInfo");
                                        String flag=countryInfo.getString("flag");

                                        if(country.equals("Total:"))
                                        {
                                            country="World";
                                        }
                                        String active = jsonObject.getString("cases");
                                        String deaths = jsonObject.getString("deaths");
                                        String recovered = jsonObject.getString("recovered");

                                        arrayList.add(new CountryModel(country, getFormattedAmount(Integer.parseInt(active)), getFormattedAmount(Integer.parseInt(deaths)), getFormattedAmount(Integer.parseInt(recovered)),flag));

                                }


                                adapter.notifyDataSetChanged();

                                dialog.dismiss();


                            } catch (JSONException e) {
                                Log.e("Catch",e.getMessage());
                            }

                        }
                    });

                }
                else
                {

                    if(getActivity()==null)
                    {
                        return;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            dialog.dismiss();
                            Log.e("OnFailure",response.message());
                            DynamicToast.makeError(getActivity(),response.message(),2000).show();

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
                    recyclerView.setAdapter(new AllCountryAdapter(getActivity(),arrayList));
                    adapter.notifyDataSetChanged();
                }
                else
                {
                    filter(s.toString());
                }
            }
        });

    }

    private void filter(String country) {

        for(CountryModel model : arrayList)
        {
            if(model.getLocation().toLowerCase().contains(country.toLowerCase()))
            {
                filterList.add(model);
            }
        }

        recyclerView.setAdapter(new AllCountryAdapter(getActivity(),filterList));
        adapter.notifyDataSetChanged();

    }

    private String getFormattedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_country, container, false);
    }
}