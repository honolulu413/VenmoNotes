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
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.loopj.android.image.SmartImageView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;

/**
 * Created by lulu on 7/22/2015.
 */
public class StatisticsFragment extends Fragment{
    public static String TAG = "STATISTIC";
    public static final String ARRAYLIST =
            "com.example.lulu.StatisticsFragment.arraylist";
    public static final String HASHMAP =
            "com.example.lulu.StatisticsFragment.hashmap";
    private ArrayList<Transaction> mTransactions;
    private GraphView mGraph;
    private GraphView mBarGraph;
    private GraphView mBarGraph2;
    private ViewGroup mImages;
    private ViewGroup mImages2;

    private TextView mSummary;
    private int barSize = 3;
    private double mAmoutOffset = 1.2;
    private double mDateOffSet = 200000000;
    int barWidth = 26;
    OnDataPointTapListener tapListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        mTransactions = (ArrayList<Transaction>)getArguments().getSerializable(ARRAYLIST);
        ArrayList<Transaction> pTransactions = new ArrayList<Transaction>();
        ArrayList<Transaction> nTransactions = new ArrayList<Transaction>();

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

        View v = inflater.inflate(R.layout.fragment_statistics, parent, false);

        String dateString = mTransactions.get(mTransactions.size() - 1).getDateString();
        String summary = "Since " + dateString + "    " + "Gained: " + sGained + "$  Paid: " + sPaid + "$";
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

        drawSeries(pTransactions, Color.RED);
        drawSeries(nTransactions, Color.BLUE);

        mGraph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
        mGraph.getGridLabelRenderer().setNumHorizontalLabels(3);

        mGraph.getViewport().setYAxisBoundsManual(true);
        mGraph.getViewport().setMinY((int) (mAmoutOffset * min));
        mGraph.getViewport().setMaxY((int) (mAmoutOffset * max));

        mGraph.getViewport().setXAxisBoundsManual(true);
        mGraph.getViewport().setMinX(mTransactions.get(mTransactions.size() - 1).getDate().getTime() - mDateOffSet);
        mGraph.getViewport().setMaxX(mTransactions.get(0).getDate().getTime() + mDateOffSet);
        mGraph.getViewport().setScalable(true);

        HashMap<User, ArrayList<Transaction>> map = (HashMap<User, ArrayList<Transaction>>) getArguments().getSerializable(HASHMAP);
        PriorityQueue<FriendTotal> pq = new PriorityQueue<FriendTotal>();
        PriorityQueue<FriendTotal> pqTimes = new PriorityQueue<FriendTotal>(100, new Comparator<FriendTotal>() {
            @Override
            public int compare(FriendTotal lhs, FriendTotal rhs) {
                return rhs.getTimes() - lhs.getTimes();
            }
        });

        for (Map.Entry<User, ArrayList<Transaction>> entry: map.entrySet()) {
            FriendTotal tmp = new FriendTotal();
            ArrayList<Transaction> list = entry.getValue();
            tmp.setTimes(list.size());

            User friend = entry.getKey();
            tmp.setDisplayName(friend.getDisplayName());
            tmp.setUrl(friend.getProfileUrl());
            for (Transaction t: list) {
                double amount = t.getRealAmount();
                if (amount <= 0) tmp.addPay(t.getAmount());
                else tmp.addGain(t.getAmount());
            }
            pq.add(tmp);
            pqTimes.add(tmp);
        }

        FriendTotal[] topFriends = new FriendTotal[barSize];
        for (int i = 0; i < barSize; i++) {
            topFriends[i] = pq.poll();
        }

        mBarGraph = (GraphView) v.findViewById(R.id.top_friend);
        ViewGroup group = (ViewGroup) v.findViewById(R.id.top_friend_group);
        loadImage(group, topFriends);
        drawBars(topFriends);

        for (int i = 0; i < barSize; i++) {
            topFriends[i] = pqTimes.poll();
        }

        mBarGraph2 = (GraphView) v.findViewById(R.id.top_friend_times);
        ViewGroup group2 = (ViewGroup) v.findViewById(R.id.top_friend_group2);
        loadImage(group2, topFriends);
        drawBars2(topFriends);

        return v;
    }

    public static StatisticsFragment newInstance(ArrayList<Transaction> transactions, HashMap<User, ArrayList<Transaction>> map) {
        Bundle args = new Bundle();
        args.putSerializable(ARRAYLIST, transactions);
        args.putSerializable(HASHMAP, map);
        StatisticsFragment fragment = new StatisticsFragment();
        fragment.setArguments(args);
        return fragment;
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

    private void drawBars(FriendTotal[] friends) {
        int barWidth = 26;
        int size = friends.length;
        DataPoint[] gainPoints = new DataPoint[size];
        DataPoint[] payPoints = new DataPoint[size];

        int max = 0;
        for (int i = 0; i < size; i++) {
            FriendTotal tmp = friends[i];
            max = Math.max(max, (int)Math.max(tmp.getGain(), tmp.getPay()));
            gainPoints[i] = new DataPoint(i + 1, tmp.getGain());
            payPoints[i] = new DataPoint(i + 1, tmp.getPay());
        }

        BarGraphSeries<DataPoint> gainSeries = new BarGraphSeries<DataPoint>(gainPoints);
        gainSeries.setColor(Color.RED);
        gainSeries.setSpacing(barWidth);
        mBarGraph.addSeries(gainSeries);

        BarGraphSeries<DataPoint> paySeries = new BarGraphSeries<DataPoint>(payPoints);
        paySeries.setColor(Color.BLUE);
        paySeries.setSpacing(barWidth);
        mBarGraph.addSeries(paySeries);

        gainSeries.setTitle("gain");
        paySeries.setTitle("pay");

        mBarGraph.getViewport().setXAxisBoundsManual(true);
        mBarGraph.getViewport().setMinX(0);
        mBarGraph.getViewport().setMaxX(barSize + 1);

        mBarGraph.getViewport().setYAxisBoundsManual(true);
        mBarGraph.getViewport().setMinY(0);
        mBarGraph.getViewport().setMaxY((int) (max * 1.2));

        mBarGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double v, boolean b) {
                if (b) {
                    return null;
                } else return super.formatLabel(v, b);
            }
        });

        mBarGraph.getGridLabelRenderer().setNumHorizontalLabels(barSize + 2);
        //mBarGraph.getGridLabelRenderer().setTextSize(4.5f);

        mBarGraph.getLegendRenderer().setVisible(true);
        mBarGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    private void drawBars2(FriendTotal[] friends) {
        int size = friends.length;
        DataPoint[] points = new DataPoint[size];
        final String[] labels = new String[size];

        int max = 0;
        for (int i = 0; i < size; i++) {
            FriendTotal tmp = friends[i];
            max = Math.max(max, tmp.getTimes());
            points[i] = new DataPoint(i + 1, tmp.getTimes());
            labels[i] = tmp.getDisplayName();
        }

        mBarGraph2.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double v, boolean b) {
                if (b) {
                    return null;
                } else return super.formatLabel(v, b);
            }
        });

        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(points);
        series.setColor(Color.rgb(102, 204, 0));
        series.setSpacing((int) (barWidth * 1.8));
        mBarGraph2.addSeries(series);

        series.setTitle("times");

        mBarGraph2.getViewport().setXAxisBoundsManual(true);
        mBarGraph2.getViewport().setMinX(0);
        mBarGraph2.getViewport().setMaxX(barSize + 1);

        mBarGraph2.getViewport().setYAxisBoundsManual(true);
        mBarGraph2.getViewport().setMinY(0);
        max = (int)(max * 1.3);
        while (max % 4 != 0) max++;
        mGraph.getGridLabelRenderer().setNumHorizontalLabels(4);
        mBarGraph2.getViewport().setMaxY(max);

        mBarGraph2.getGridLabelRenderer().setNumHorizontalLabels(barSize + 2);

        mBarGraph2.getLegendRenderer().setVisible(true);
        mBarGraph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }

    public void loadImage(ViewGroup v, FriendTotal[] friends) {
        for (int i = 0; i < friends.length; i++) {
            SmartImageView image = (SmartImageView) v.getChildAt(i);
            final FriendTotal friend = friends[i];
            Log.d(TAG, "image" + image);
            Log.d(TAG, "friend" + friend);
            Log.d(TAG, "friendurl" + friend.getUrl());

            image.setImageUrl(friend.getUrl());
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), friend.getDisplayName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
