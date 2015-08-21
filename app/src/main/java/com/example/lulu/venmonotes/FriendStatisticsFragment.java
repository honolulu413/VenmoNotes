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
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.loopj.android.image.SmartImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by lulu on 8/17/2015.
 */
public class FriendStatisticsFragment extends Fragment {
    public static String TAG = "STATISTIC";
    public static final String ARRAYLIST =
            "com.example.lulu.FriendStatisticsFragment.arraylist";
    private ArrayList<Transaction> mTransactions;
    private GraphView mGraph;

    private TextView mSummary;
    private int barSize = 3;
    private double mAmoutOffset = 1.2;
    private double mDateOffSet = 200000000;
    OnDataPointTapListener tapListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {

        mTransactions = (ArrayList<Transaction>)getArguments().getSerializable(ARRAYLIST);

        if (mTransactions == null || mTransactions.isEmpty()) {
            View v = inflater.inflate(R.layout.fragment_no_transactions, parent, false);
            return v;
        }

        ArrayList<Transaction> pTransactions = new ArrayList<Transaction>();
        ArrayList<Transaction> nTransactions = new ArrayList<Transaction>();

        View v = inflater.inflate(R.layout.fragment_friend_sta, parent, false);
        //Log.d(TAG, "" + mTransactions);
        double max = 0;
        double min = 0;
        double paid = 0;
        double gained = 0;
        for (Transaction t: mTransactions) {
            if (t.isPositive()) {
                pTransactions.add(t);
                gained += t.getRealAmount();
                max = Math.max(max, t.getAmount());
            }
            else {
                nTransactions.add(t);
                paid += t.getRealAmount();
                min = Math.min(min, t.getRealAmount());
            }
        }

        String sPaid = "" + (int)paid;
        String sGained = "" + (int)gained;



        String dateString = mTransactions.get(mTransactions.size() - 1).getDateString();
        String summary = "Since " + dateString + "    " + "Gained: $" + sGained + "  Paid: $" + sPaid;
        SpannableStringBuilder sb = new SpannableStringBuilder(summary);
        int p = summary.indexOf(dateString);
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), p, p + dateString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        p = summary.indexOf("Gained:") + 8;
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), p, p + sGained.length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(Color.rgb(34, 139, 34)), p, p + sGained.length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        p = summary.indexOf("Paid:") + 6;
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), p, p + sPaid.length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(Color.RED), p, p + sPaid.length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mSummary = (TextView) v.findViewById(R.id.statistics_summary1);
        mSummary.setText(sb);

        mGraph = (GraphView) v.findViewById(R.id.recent_transaction1);

        tapListener = new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Date date = new Date();
                date.setTime((long) dataPoint.getX());
                String dateString = "On " + new SimpleDateFormat("MM/dd/yyyy").format(date);
                String action = dataPoint.getY() >= 0? "gained": "paid";
                Toast.makeText(getActivity(), dateString + ", you " + action + " " + dataPoint.getY() + "$", Toast.LENGTH_SHORT).show();
            }
        };

        if (!pTransactions.isEmpty())
            drawSeries(pTransactions, Color.rgb(34, 139, 34));
        if (!nTransactions.isEmpty())
            drawSeries(nTransactions, Color.RED);

        mGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        mGraph.getGridLabelRenderer().setNumHorizontalLabels(3);

        mGraph.getViewport().setYAxisBoundsManual(true);
        mGraph.getViewport().setMinY((int) (mAmoutOffset * min));
        mGraph.getViewport().setMaxY((int) (mAmoutOffset * max));

        mGraph.getViewport().setXAxisBoundsManual(true);
        mGraph.getViewport().setMinX(mTransactions.get(mTransactions.size() - 1).getDate().getTime() - mDateOffSet);
        mGraph.getViewport().setMaxX(mTransactions.get(0).getDate().getTime() + mDateOffSet);
        mGraph.getViewport().setScalable(true);
        Log.d(TAG, "123");
        return v;
    }

    private void drawSeries(ArrayList<Transaction> transactions, int color) {
        int size = transactions.size();
        DataPoint[] points = new DataPoint[size];
        for (int i = 0; i < size; i++) {
            Transaction transaction = transactions.get(size - 1 - i);
            points[i] = new DataPoint(transaction.getDate(), transaction.getRealAmount());
        }
        LineGraphSeries<DataPoint> lineSeries = new LineGraphSeries<DataPoint>(points);
        lineSeries.setColor(color);

        PointsGraphSeries<DataPoint> pointSeries = new PointsGraphSeries<DataPoint>(points);
        pointSeries.setColor(color);
        pointSeries.setSize(16);
        pointSeries.setOnDataPointTapListener(tapListener);

        mGraph.addSeries(lineSeries);
        mGraph.addSeries(pointSeries);
    }
}
