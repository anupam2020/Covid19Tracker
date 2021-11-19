package com.sbdev.covid19tracker;

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

public class HomeFragment extends Fragment {

    private TabLayout CovidtabLayout;
    private ViewPager2 CovidviewPager2;
    private FragmentAdapter Covidadapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CovidtabLayout=view.findViewById(R.id.covidTabLayout);
        CovidviewPager2=view.findViewById(R.id.covidViewPager2);


        FragmentManager fm=getChildFragmentManager();
        Covidadapter=new FragmentAdapter(fm,getLifecycle());
        CovidviewPager2.setAdapter(Covidadapter);

        CovidtabLayout.addTab(CovidtabLayout.newTab().setText("My Country"));
        CovidtabLayout.addTab(CovidtabLayout.newTab().setText("Global"));

        CovidtabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_location_on_24);
        CovidtabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_world_24);

        CovidtabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                CovidviewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        CovidviewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                CovidtabLayout.selectTab(CovidtabLayout.getTabAt(position));
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}