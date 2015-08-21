package com.example.lulu.venmonotes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;

/**
 * Created by lulu on 8/18/2015.
 */
public class NotesFragment extends ListFragment {
    private ArrayList<Event> mEvents;
    private boolean showPhoto;
    public String TAG = "note";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
//        View v = super.onCreateView(inflater, parent, savedInstanceState);
        View v = inflater.inflate(R.layout.listview_swipe_list_view, parent, false);
        SwipeMenuListView mListView = (SwipeMenuListView) v.findViewById(android.R.id.list);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                openItem.setWidth(220);
                openItem.setTitle("OPEN");
                openItem.setTitleSize(15);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(220);
                deleteItem.setIcon(R.mipmap.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Event event = mEvents.get(position);
                switch (index) {
                    case 0:
                        openEvent(event);
                        break;
                    case 1:
                        mEvents.remove(position);
                        updateUI();
                        break;
                }
                return false;
            }
        });
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
        MenuItem item = menu.findItem(R.id.menu_item_show_photo);
        setMenuTitle(item);
    }

    public void setMenuTitle(MenuItem item) {
        if (showPhoto) {
            item.setTitle(R.string.hide_photo);
        } else {
            item.setTitle(R.string.show_photo);
        }
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
                showPhoto = !showPhoto;
                setMenuTitle(item);
                NotesFragment.this.setListAdapter(new EventAdapter(mEvents));
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
                tmp += subEvents.get(i).getDisplayName();
                if (i != subEvents.size() - 1) tmp += ", ";
            }
            TextView peopleTextView =
                    (TextView) convertView.findViewById(R.id.people);
            peopleTextView.setText(subEvents.size() == 0? "no friends added": tmp);

            TextView titleTextView =
                    (TextView) convertView.findViewById(R.id.title);
            String s = e.getTitle();
            String title = s == null || s.equals("") ? "Unnamed": s;
            titleTextView.setText(title);

            TextView dateTextView =
                    (TextView) convertView.findViewById(R.id.date);
            dateTextView.setText(e.getDateString());
            CheckBox solvedCheckBox =
                    (CheckBox) convertView.findViewById(R.id.solvedCheckBox);
            solvedCheckBox.setChecked(e.isSettled());

            ImageView imageView = (ImageView) convertView.findViewById(R.id.event_imageView);
            if (showPhoto) {

            } else {
                imageView.setVisibility(View.GONE);
            }

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
        Event event = ((EventAdapter) getListAdapter()).getItem(position);
        openEvent(event);
    }

    public void openEvent(Event event ) {
        Intent i = new Intent(getActivity(), EventPagerActivity.class);
        i.putExtra(HomePageActivity.FRIENDS, HomePageActivity.getFriendList());
        i.putExtra(EventFragment.EXTRA_EVENT, event.getDate());
        startActivity(i);
    }

}
