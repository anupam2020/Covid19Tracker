package com.sbdev.covid19tracker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AllCountryFragment extends Fragment {

    private TextInputEditText search;
    private RecyclerView recyclerView;
    private AllCountryAdapter adapter;
    private ArrayList<CountryModel> arrayList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        search=view.findViewById(R.id.countrySearch);
        recyclerView=view.findViewById(R.id.countryRecyclerView);

        adapter=new AllCountryAdapter(getActivity(),arrayList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_country, container, false);
    }
}