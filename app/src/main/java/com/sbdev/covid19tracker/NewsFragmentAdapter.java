package com.sbdev.covid19tracker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class NewsFragmentAdapter extends FragmentStateAdapter {
    public NewsFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position)
        {
            case 0:
                return new MyCountryNewsFragment();
            case 1:
                return new GlobalNewsFragment();
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
