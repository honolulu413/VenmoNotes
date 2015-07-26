package com.example.lulu.venmonotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by lulu on 7/22/2015.
 */
public class StatisticsFragment extends Fragment{
    public static String TAG = "STATISTIC";
    public static final String ARRAYLIST =
            "com.example.lulu.StatisticsFragment.arraylist";
    private ArrayList<Transaction> mTransactions = new ArrayList<Transaction>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        mTransactions = (ArrayList<Transaction>)getArguments().getSerializable(ARRAYLIST);
        Log.d(TAG, "" + mTransactions);

        View v = inflater.inflate(R.layout.fragment_statistics, parent, false);
        GraphView graph = (GraphView) v.findViewById(R.id.recent_transaction);
        graph.addSeries(getDataPoints(mTransactions));
        Log.d(TAG, "" + "123");
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space
        graph.getViewport().setXAxisBoundsManual(true);
        Log.d(TAG, "" + "456");
        return v;
    }

    public static StatisticsFragment newInstance(ArrayList<Transaction> transactions) {
        Bundle args = new Bundle();
        args.putSerializable(ARRAYLIST, transactions);
        StatisticsFragment fragment = new StatisticsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    LineGraphSeries<DataPoint> getDataPoints(ArrayList<Transaction> transactions) {
        DataPoint[] points = new DataPoint[transactions.size()];
        for (int i = 0; i < points.length; i++) {
            Transaction transaction = transactions.get(i);
            points[i] = new DataPoint(transaction.getDate(), transaction.getRealAmount());
            //Log.d(TAG, "" + transaction.getRealAmount());
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(points);
        return series;
    }
}
