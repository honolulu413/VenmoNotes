package com.example.lulu.venmonotes;

import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lulu on 8/18/2015.
 */
public class NotesFragment extends ListFragment {
    private ArrayList<Event> mEvents;
    public String TAG = "note";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.notes);
        mEvents = EventLab.get(getActivity()).getEvents();
        EventAdapter adapter = new EventAdapter(mEvents);
        setListAdapter(adapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_event_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_event:
                Event event = new Event();
                EventLab.get(getActivity()).addEvent(event);
                ((EventAdapter)getListAdapter()).notifyDataSetChanged();
                Intent i = new Intent(getActivity(), EventPagerActivity.class);
                i.putExtra(HomePageActivity.FRIENDS, HomePageActivity.getFriendList());
                i.putExtra(EventFragment.EXTRA_EVENT, event.getDate());
                startActivity(i);
                return true;
            case R.id.menu_item_show_photo:


                
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI() {
        ((EventAdapter)getListAdapter()).notifyDataSetChanged();
    }

    private class EventAdapter extends ArrayAdapter<Event> {
        public EventAdapter(ArrayList<Event> events) {
            super(getActivity(), 0, events);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_event, null);
            }
            Event e = getItem(position);


            ArrayList<SubEvent> subEvents = e.getSubEvents();
            String tmp = "";
            for (int i = 0; i < subEvents.size(); i++) {
                if (i == 3) {
                    tmp += "...";
                    break;
                }
                tmp += subEvents.get(i).getDisplayName() + ",";
            }
            TextView peopleTextView =
                    (TextView) convertView.findViewById(R.id.people);
            peopleTextView.setText(tmp);

            TextView titleTextView =
                    (TextView) convertView.findViewById(R.id.title);
            titleTextView.setText(e.getTitle());

            TextView dateTextView =
                    (TextView) convertView.findViewById(R.id.date);
            dateTextView.setText(e.getDateString());
            CheckBox solvedCheckBox =
                    (CheckBox) convertView.findViewById(R.id.solvedCheckBox);
            solvedCheckBox.setChecked(e.isSettled());
            return convertView;
        }
    }

    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventLab.get(getActivity()).saveEvents();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        Crime c = (Crime) (getListAdapter()).getItem(position);
        Event event = ((EventAdapter) getListAdapter()).getItem(position);
        //Log.d(TAG, position + " is clicked");
        // Start CrimeActivity
        Intent i = new Intent(getActivity(), EventPagerActivity.class);
        i.putExtra(HomePageActivity.FRIENDS, HomePageActivity.getFriendList());
        i.putExtra(EventFragment.EXTRA_EVENT, event.getDate());
        startActivity(i);
    }

}
