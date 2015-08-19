package com.example.lulu.venmonotes;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lulu on 8/17/2015.
 */
public class EventLab {
    private static final String TAG = "EventLab";
    private static final String FILENAME = "Event.json";
    private ArrayList<Event> mEvents;
    private EventJSONSerializer mSerializer;
    private static EventLab sEventLab;
    private Context mAppContext;

    private EventLab(Context appContext) {
        mAppContext = appContext;
        mSerializer = new EventJSONSerializer(mAppContext, FILENAME);
        try {
            mEvents = mSerializer.loadEvent();
        } catch (Exception e) {
            mEvents = new ArrayList<Event>();
            Log.e(TAG, "Error loading events: ", e);
        }
    }

    public boolean saveEvents() {
        try {
            mSerializer.saveEvents(mEvents);
            Log.d(TAG, "Events saved to file");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error saving events: ", e);
            return false;
        }
    }

    public static EventLab get(Context c) {
        if (sEventLab == null) {
            sEventLab = new EventLab(c.getApplicationContext());
        }
        return sEventLab;
    }

    public ArrayList<Event> getEvents() {
        return mEvents;
    }

    public Event getEvent(Date date) {
        for (Event e : mEvents) {
            if (e.getDate().equals(date))
                return e;
        }
        return null;
    }

    public void addEvent(Event e) {
        mEvents.add(e);
    }

    public void removeEvent(Event e) {mEvents.remove(e);}
}
