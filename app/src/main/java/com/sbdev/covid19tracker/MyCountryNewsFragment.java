package com.sbdev.covid19tracker;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

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

public class MyCountryNewsFragment extends Fragment {

    private RecyclerView recyclerView;

    private ArrayList<GlobalNewsModel> arrayList;

    private GlobalNewsAdapter adapter;

    private String title,des,webURL,imgURL,url;

    private ProgressDialog dialog;

    private OkHttpClient client;

    private Request request;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.myCountryNewsRecycler);

        arrayList=new ArrayList<>();

        client=new OkHttpClient();

        adapter=new GlobalNewsAdapter(getActivity(),arrayList);
        recyclerView.setAdapter(adapter);

        url="https://newsapi.in/newsapi/news.php?key=kyDsI3QproB74VUSmqPIMaqNay0Q1x&category=bengali_state";
        request=new Request.Builder()
                .url(url)
                .build();

        dialog=new ProgressDialog(getActivity());

        dialog.show();
        dialog.setContentView(R.layout.loading_bg);
        //dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


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

                                JSONObject jsonObject=new JSONObject(res);
                                JSONArray News=jsonObject.getJSONArray("News");
                                for(int i=0;i<News.length();i++)
                                {

                                    JSONObject index=News.getJSONObject(i);

                                    title= index.getString("title");
                                    des= index.getString("description");
                                    imgURL= index.getString("image");;
                                    webURL=index.getString("url");

                                    arrayList.add(new GlobalNewsModel(imgURL,title,des,webURL));

                                }

                                adapter.notifyDataSetChanged();

                                dialog.dismiss();


                            } catch (JSONException e) {

                                dialog.dismiss();
                                Log.e("CATCH",e.getMessage());
                                DynamicToast.makeWarning(getActivity(),e.getMessage(),2000).show();

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_country_news, container, false);
    }
}