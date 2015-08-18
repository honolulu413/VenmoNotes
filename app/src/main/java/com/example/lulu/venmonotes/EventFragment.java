package com.example.lulu.venmonotes;

import android.app.usage.UsageEvents;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.Date;
import java.util.UUID;

/**
 * Created by lulu on 8/18/2015.
 */
public class EventFragment extends Fragment {
    public static final String EXTRA_EVENT_DATE = "com.eventDate";
    private Event mEvent;


    public static EventFragment newInstance(Date eventDate) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_EVENT_DATE, eventDate);
        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Date eventDate = (Date) getArguments().getSerializable(EXTRA_EVENT_DATE);
        mEvent = EventLab.get(getActivity()).getEvent(eventDate);
    }

}
