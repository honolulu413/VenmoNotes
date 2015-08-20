package com.example.lulu.venmonotes;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

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
        View v = inflater.inflate(R.layout.listview_swipe_list_view, parent, false);
        SwipeMenuListView mListView = (SwipeMenuListView) v.findViewById(android.R.id.list);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(200);
                deleteItem.setIcon(R.mipmap.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        mSubEvents.remove(position);
                        updateUI();
                        break;
                }
                return false;
            }
        });
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
            Picasso.with(getActivity()).load(s.getProfileUrl()).fit().into(image);

            TextView action =
                    (TextView) convertView.findViewById(R.id.action);
            action.setText(s.getAction() + "  " + s.getDisplayName());

            TextView amount =
                    (TextView) convertView.findViewById(R.id.amount);
            String symbol = s.getRealAmount() >= 0? "+": "";
            amount.setText(symbol + s.getRealAmount());
            amount.setTextColor(s.getRealAmount() >= 0? Color.GREEN: Color.RED);

            CheckBox solvedCheckBox =
                    (CheckBox) convertView.findViewById(R.id.solvedCheckBox);
            solvedCheckBox.setChecked(s.isSettled());
            return convertView;
        }
    }
}
