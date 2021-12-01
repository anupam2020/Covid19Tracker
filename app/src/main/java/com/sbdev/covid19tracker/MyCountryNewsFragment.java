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

    private String title,des,url,webURL;

    private ProgressDialog dialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.myCountryNewsRecycler);

        arrayList=new ArrayList<>();

        adapter=new GlobalNewsAdapter(getActivity(),arrayList);
        recyclerView.setAdapter(adapter);

        dialog=new ProgressDialog(getActivity());

        dialog.show();
        dialog.setContentView(R.layout.loading_bg);
        //dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://coronavirus-smartable.p.rapidapi.com/news/v1/IN/")
                .get()
                .addHeader("x-rapidapi-host", "coronavirus-smartable.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e8f6c57650msh666fef2e3a110b5p13b950jsn4359d608e124")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.e("OnFailure",e.getMessage());
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
                                JSONArray newsArray=jsonObject.getJSONArray("news");

                                for(int i=0;i<newsArray.length();i++)
                                {

                                    if(i==1 || i==9 || i==22)
                                    {
                                        continue;
                                    }
                                    else
                                    {
                                        JSONObject index=newsArray.getJSONObject(i);
                                        title=index.getString("title");
                                        des=index.getString("excerpt");
                                        webURL=index.getString("webUrl");

                                        JSONArray imagesArray=index.getJSONArray("images");
                                        JSONObject zero=imagesArray.getJSONObject(0);

                                        url=zero.getString("url");

                                        arrayList.add(new GlobalNewsModel(url,title,des,webURL));
                                    }

                                }

                                adapter.notifyDataSetChanged();

                                dialog.dismiss();


                            } catch (JSONException e) {
                                Log.e("CATCH",e.getMessage());
                            }


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