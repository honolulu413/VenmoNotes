package com.example.lulu.venmonotes;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

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
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        ListView listView = (ListView) v.findViewById(android.R.id.list);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            registerForContextMenu(listView);
        } else {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                      long id, boolean checked) {
                }

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.subevent_list_item_context, menu);
                    return true;
                }

                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_subevent:
                            SubEventAdapter adapter = (SubEventAdapter) getListAdapter();
                            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    mSubEvents.remove(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            ((EventFragment)getParentFragment()).updateUI();
                            return true;
                        default:
                            return false;
                    }
                }

                public void onDestroyActionMode(ActionMode mode) {
                }
            });
        }
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
        setHasOptionsMenu(true);
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
        public SubEvent mSubEvent;
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_subevent, null);
            }
            SubEvent s = getItem(position);
            mSubEvent = s;
            RoundedImageView image = (RoundedImageView) convertView.findViewById(R.id.round_imageView);
            Picasso.with(getActivity()).load(s.getProfileUrl()).fit().into(image);

            TextView action =
                    (TextView) convertView.findViewById(R.id.action);
            action.setText(s.getAction() + "  " + s.getDisplayName());

            TextView amount =
                    (TextView) convertView.findViewById(R.id.amount);
            String symbol = s.getRealAmount() >= 0? "+": "";
            amount.setText(symbol + "$" + s.getRealAmount());
            amount.setTextColor(s.getRealAmount() >= 0 ? Color.rgb(34, 139, 34) : Color.RED);

            CheckBox solvedCheckBox =
                    (CheckBox) convertView.findViewById(R.id.solvedCheckBox);
            solvedCheckBox.setChecked(s.isSettled());
            solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // update your model (or other business logic) based on isChecked
                    if (isChecked)
                        mSubEvent.setIsSettled(true);
                    else
                        mSubEvent.setIsSettled(false);
                }
            });
            return convertView;
        }
    }
}
