package com.sbdev.covid19tracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;

public class WeatherStateAdapter extends androidx.viewpager2.adapter.FragmentStateAdapter {
    public WeatherStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 0:
                return new TodayFragment();
            case 1:
                return new HourlyFragment();
            case 2:
                return new DailyFragment();
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
