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

public class GlobalNewsFragment extends Fragment {

    private RecyclerView recyclerView;

    private ArrayList<GlobalNewsModel> arrayList;

    private GlobalNewsAdapter adapter;

    private String title,des,imgURL,webURL;

    private ProgressDialog dialog;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.globalNewsRecycler);

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
                .url("https://covid-19-news.p.rapidapi.com/v1/covid?q=covid&lang=en&sort_by=date&media=True")
                .get()
                .addHeader("x-rapidapi-host", "covid-19-news.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "e8f6c57650msh666fef2e3a110b5p13b950jsn4359d608e124")
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

                                JSONObject jsonObject=new JSONObject(res);
                                JSONArray articles=jsonObject.getJSONArray("articles");

                                Log.d("Articles Size", String.valueOf(articles.length()));

                                for(int i=0;i<articles.length();i++)
                                {

                                    JSONObject index=articles.getJSONObject(i);

                                    title=index.getString("title");
                                    des=index.getString("summary");
                                    imgURL=index.getString("media");
                                    webURL=index.getString("link");

                                    arrayList.add(new GlobalNewsModel(imgURL,title,des,webURL));

                                }

                                adapter.notifyDataSetChanged();

                                dialog.dismiss();


                            } catch (JSONException e) {
                                Log.e("CATCH",e.getMessage());
                                dialog.dismiss();
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
                            DynamicToast.makeError(getActivity(),response.message(),2000).show();
                        }
                    });

                }

            }
        });


//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("https://coronavirus-smartable.p.rapidapi.com/news/v1/global/")
//                .get()
//                .addHeader("x-rapidapi-host", "coronavirus-smartable.p.rapidapi.com")
//                .addHeader("x-rapidapi-key", "e8f6c57650msh666fef2e3a110b5p13b950jsn4359d608e124")
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//                Log.e("OnFailure",e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                if(response.isSuccessful())
//                {
//
//                    String res=response.body().string();
//
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            try {
//
//                                JSONObject jsonObject=new JSONObject(res);
//                                JSONArray newsArray=jsonObject.getJSONArray("news");
//
//                                for(int i=0;i<newsArray.length();i++)
//                                {
//
//                                    if(i==25 || i==7)
//                                    {
//                                        continue;
//                                    }
//                                    else
//                                    {
//                                        JSONObject index=newsArray.getJSONObject(i);
//                                        title=index.getString("title");
//                                        des=index.getString("excerpt");
//                                        webURL=index.getString("webUrl");
//
//                                        JSONArray imagesArray=index.getJSONArray("images");
//                                        JSONObject zero=imagesArray.getJSONObject(0);
//
//                                        url=zero.getString("url");
//
//                                        arrayList.add(new GlobalNewsModel(url,title,des,webURL));
//                                    }
//
//                                }
//
//                                adapter.notifyDataSetChanged();
//
//                                dialog.dismiss();
//
//
//                            } catch (JSONException e) {
//                                Log.e("CATCH",e.getMessage());
//                            }
//
//
//                        }
//                    });
//
//                }
//
//            }
//        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_global_news, container, false);
    }
}