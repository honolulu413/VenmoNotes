package com.example.lulu.venmonotes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by lulu on 7/22/2015.
 */
public class StatisticsFragment extends Fragment{
    public static String TAG = "STATISTIC";
    public static final String ARRAYLIST =
            "com.example.lulu.StatisticsFragment.arraylist";
    private ArrayList<Transaction> mTransactions;
    private GraphView mGraph;
    private TextView mSummary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        mTransactions = (ArrayList<Transaction>)getArguments().getSerializable(ARRAYLIST);
        ArrayList<Transaction> pTransactions = new ArrayList<Transaction>();
        ArrayList<Transaction> nTransactions = new ArrayList<Transaction>();

        int paid = 0;
        int gained = 0;
        for (Transaction t: mTransactions) {
            if (t.isPositive()) {
                pTransactions.add(t);
                gained += t.getRealAmount();
            }
            else {
                nTransactions.add(t);
                paid += t.getRealAmount();
            }
        }
        String sPaid = "" + paid;
        String sGained = "" + gained;

        View v = inflater.inflate(R.layout.fragment_statistics, parent, false);

        String dateString = mTransactions.get(mTransactions.size() - 1).getDateString();
        String summary = "Since " + dateString + "    " + "Gained: " + gained + "$  Paid: " + paid + "$";
        SpannableStringBuilder sb = new SpannableStringBuilder(summary);
        int p = summary.indexOf(dateString);
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), p, p + dateString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        p = summary.indexOf("Gained:") + 8;
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), p, p + sGained.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(Color.RED), p, p + sGained.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        p = summary.indexOf("Paid:") + 6;
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), p, p + sPaid.length() , Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(Color.BLUE), p, p + sPaid.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mSummary = (TextView) v.findViewById(R.id.statistics_summary);
        mSummary.setText(sb);

        mGraph = (GraphView) v.findViewById(R.id.recent_transaction);

        drawSeries(pTransactions, Color.RED);
        drawSeries(nTransactions, Color.BLUE);

        mGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        mGraph.getGridLabelRenderer().setNumHorizontalLabels(3);
//
        mGraph.getViewport().setYAxisBoundsManual(true);
        mGraph.getViewport().setMinY(-30);
        mGraph.getViewport().setMaxY(80);

        mGraph.getViewport().setXAxisBoundsManual(true);
        mGraph.getViewport().setMinX(-30);
        mGraph.getViewport().setMaxX(80);
        mGraph.getViewport().setScalable(true);
        return v;
    }

    public static StatisticsFragment newInstance(ArrayList<Transaction> transactions) {
        Bundle args = new Bundle();
        args.putSerializable(ARRAYLIST, transactions);
        StatisticsFragment fragment = new StatisticsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void drawSeries(ArrayList<Transaction> transactions, int color) {
        DataPoint[] points = new DataPoint[transactions.size()];
        for (int i = 0; i < points.length; i++) {
            Transaction transaction = transactions.get(i);
            points[i] = new DataPoint(transaction.getDate(), transaction.getRealAmount());
            //Log.d(TAG, "" + transaction.getRealAmount());
        }
        LineGraphSeries<DataPoint> lineSeries = new LineGraphSeries<DataPoint>(points);
        lineSeries.setColor(color);

        PointsGraphSeries<DataPoint> pointSeries = new PointsGraphSeries<DataPoint>(points);
        pointSeries.setColor(color);
        pointSeries.setSize(16);

        mGraph.addSeries(lineSeries);
        mGraph.addSeries(pointSeries);
    }
}
