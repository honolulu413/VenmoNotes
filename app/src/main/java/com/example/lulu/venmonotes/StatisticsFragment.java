package com.example.lulu.venmonotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by lulu on 7/22/2015.
 */
public class StatisticsFragment extends Fragment{
    public static final String ARRAYLIST =
            "com.example.lulu.StatisticsFragment.arraylist";
    private ArrayList<Transaction> mTransactions = new ArrayList<Transaction>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statistics, parent, false);
        return v;
    }

    public static StatisticsFragment newInstance(ArrayList<Transaction> transactions) {
        Bundle args = new Bundle();
        args.putSerializable(ARRAYLIST, transactions);
        StatisticsFragment fragment = new StatisticsFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
