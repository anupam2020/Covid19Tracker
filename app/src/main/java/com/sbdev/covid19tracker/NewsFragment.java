package com.sbdev.covid19tracker;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.jaeger.library.StatusBarUtil;

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

public class NewsFragment extends Fragment {

//    private TabLayout newsTabLayout;
//    private ViewPager2 newsViewPager;
//    private NewsFragmentAdapter newsAdapter;

    private RecyclerView recyclerView;

    private ArrayList<GlobalNewsModel> arrayList;

    private GlobalNewsAdapter adapter;

    private ProgressDialog dialog;

    private String title,des,imgURL,webURL;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        newsTabLayout=view.findViewById(R.id.covidTabLayout);
//        newsViewPager=view.findViewById(R.id.covidViewPager2);
//
//
//        FragmentManager fm=getChildFragmentManager();
//        newsAdapter=new NewsFragmentAdapter(fm,getLifecycle());
//        newsViewPager.setAdapter(newsAdapter);
//
//        newsTabLayout.addTab(newsTabLayout.newTab().setText("My Country"));
//        newsTabLayout.addTab(newsTabLayout.newTab().setText("Global"));
//
//        newsTabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_location_on_24);
//        newsTabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_world_24);
//
//        newsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//                newsViewPager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
//
//        newsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//
//                newsTabLayout.selectTab(newsTabLayout.getTabAt(position));
//            }
//        });


        recyclerView=view.findViewById(R.id.newsRecycler);

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
        return inflater.inflate(R.layout.fragment_news, container, false);
    }
}