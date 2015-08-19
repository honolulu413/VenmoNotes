package com.example.lulu.venmonotes;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

/**
 * Created by lulu on 8/18/2015.
 */
public class SubEventListFragment extends ListFragment{
    private ArrayList<SubEvent> mSubEvents;
    public static final String ARRAY = "com.example.lulu.SubEventListFragment.ARRAY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        return v;
    }

    public static SubEventListFragment newInstace(ArrayList<SubEvent> list) {
        Bundle args = new Bundle();
        args.putSerializable(ARRAY, list);
        SubEventListFragment fragment = new SubEventListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setTitle(R.string.notes);
        mSubEvents = (ArrayList<SubEvent>) getArguments().getSerializable(ARRAY);
        SubEventAdapter adapter = new SubEventAdapter(mSubEvents);
        setListAdapter(adapter);
    }

    public void updateUI() {
        ((SubEventAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class SubEventAdapter extends ArrayAdapter<SubEvent> {
        public SubEventAdapter(ArrayList<SubEvent> events) {
            super(getActivity(), 0, events);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_subevent, null);
            }
            SubEvent s = getItem(position);
            RoundedImageView image = (RoundedImageView) convertView.findViewById(R.id.round_imageView);
            image.setImageURI(Uri.parse(s.getProfileUrl()));


            TextView action =
                    (TextView) convertView.findViewById(R.id.action);
            action.setText(s.getAction() + " " + s.getDisplayName());

            TextView amount =
                    (TextView) convertView.findViewById(R.id.amount);
            String symbol = s.getRealAmount() >= 0? "+": "";
            amount.setText(symbol + s.getAmount());
            amount.setTextColor(s.getRealAmount() >= 0? Color.GREEN: Color.RED);

            CheckBox solvedCheckBox =
                    (CheckBox) convertView.findViewById(R.id.solvedCheckBox);
            solvedCheckBox.setChecked(s.isSettled());
            return convertView;
        }
    }
}
