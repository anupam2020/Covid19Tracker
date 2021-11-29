package com.sbdev.covid19tracker;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.jaeger.library.StatusBarUtil;

public class NewsFragment extends Fragment {

    private TabLayout newsTabLayout;
    private ViewPager2 newsViewPager;
    private NewsFragmentAdapter newsAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newsTabLayout=view.findViewById(R.id.covidTabLayout);
        newsViewPager=view.findViewById(R.id.covidViewPager2);


        FragmentManager fm=getChildFragmentManager();
        newsAdapter=new NewsFragmentAdapter(fm,getLifecycle());
        newsViewPager.setAdapter(newsAdapter);

        newsTabLayout.addTab(newsTabLayout.newTab().setText("My Country"));
        newsTabLayout.addTab(newsTabLayout.newTab().setText("Global"));

        newsTabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_location_on_24);
        newsTabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_world_24);

        newsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                newsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        newsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                newsTabLayout.selectTab(newsTabLayout.getTabAt(position));
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