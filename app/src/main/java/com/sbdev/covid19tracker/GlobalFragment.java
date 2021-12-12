package com.sbdev.covid19tracker;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GlobalFragment extends Fragment {

    private TextView affected,death,recovered,active,newAffected,newDeath,newRecovered,critical;

    private int affCount,deathCount,recCount,actCount,newAffCount,newDeathCount,newRecCount,critCount;

    private ProgressDialog progressDialog;

    private String url;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        affected=view.findViewById(R.id.textAffectedCountGlobal);
        death=view.findViewById(R.id.textDeathCountGlobal);
        recovered=view.findViewById(R.id.textRecoveredCountGlobal);
        active=view.findViewById(R.id.textActiveCountGlobal);
        newAffected=view.findViewById(R.id.textNewCasesCountGlobal);
        newDeath=view.findViewById(R.id.textNewDeathsCountGlobal);
        newRecovered=view.findViewById(R.id.textNewRecoveredCountGlobal);
        critical=view.findViewById(R.id.textCriticalCountGlobal);

        url="https://disease.sh/v3/covid-19/all";

        progressDialog=new ProgressDialog(getActivity());

        progressDialog.show();
        progressDialog.setContentView(R.layout.loading_bg);
        //progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        OkHttpClient client = new OkHttpClient();

//        Request request = new Request.Builder()
//                .url("https://vaccovid-coronavirus-vaccine-and-treatment-tracker.p.rapidapi.com/api/npm-covid-data/world")
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

                        progressDialog.dismiss();
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

                                JSONObject jsonObject=new JSONObject(res);

                                affCount=jsonObject.getInt("cases");
                                deathCount=jsonObject.getInt("deaths");
                                recCount=jsonObject.getInt("recovered");
                                actCount=jsonObject.getInt("active");
                                newAffCount=jsonObject.getInt("todayCases");
                                newDeathCount=jsonObject.getInt("todayDeaths");
                                newRecCount=jsonObject.getInt("todayRecovered");
                                critCount=jsonObject.getInt("critical");



                                affected.setText(getFormattedAmount(affCount));
                                death.setText(getFormattedAmount(deathCount));
                                recovered.setText(getFormattedAmount(recCount));
                                active.setText(getFormattedAmount(actCount));
                                newAffected.setText(getFormattedAmount(newAffCount));
                                newDeath.setText(getFormattedAmount(newDeathCount));
                                newRecovered.setText(getFormattedAmount(newRecCount));
                                critical.setText(getFormattedAmount(critCount));

                                progressDialog.dismiss();

                            } catch (JSONException e) {

                                progressDialog.dismiss();
                                Log.e("OnFailure",e.getMessage());
                                DynamicToast.makeError(getActivity(),e.getMessage(),2000).show();

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

                            progressDialog.dismiss();
                            Log.e("OnFailure",response.message());
                            DynamicToast.makeError(getActivity(),response.message(),2000).show();

                        }
                    });
                }

            }
        });

    }

    private String getFormattedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_global, container, false);
    }
}