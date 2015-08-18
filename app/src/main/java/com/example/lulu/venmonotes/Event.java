package com.example.lulu.venmonotes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by lulu on 8/17/2015.
 */
public class Event {
    private boolean settled;
    private String title;
    private Date date;
    private ArrayList<SubEvent> subEvents;
    private Photo photo;
    private String note;

    private static final String JSON_SETTLED = "settled";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_PHOTO = "photo";
    private static final String JSON_SUBEVENTS = "subevents";
    private static final String JSON_NOTE = "note";

    public Event() {
        date = new Date();
        subEvents = new ArrayList<>();
    }

    public Event(JSONObject json) throws JSONException {
        settled = json.getBoolean(JSON_SETTLED);
        title = json.getString(JSON_TITLE);
        date = new Date(json.getLong(JSON_DATE));
        JSONArray array = json.getJSONArray(JSON_SUBEVENTS);
        subEvents = new ArrayList<SubEvent>();
        for (int i = 0; i < array.length(); i++) {
            subEvents.add(new SubEvent((JSONObject) array.get(i)));
        }
        if (json.has(JSON_PHOTO)) {
            photo = new Photo(json.getJSONObject(JSON_PHOTO));
        }
        note = json.getString(JSON_NOTE);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_SETTLED, settled);
        json.put(JSON_TITLE, title);
        json.put(JSON_DATE, date.getTime());
        json.put(JSON_NOTE, note);
        JSONArray array = new JSONArray();
        for (SubEvent subEvent: subEvents) {
            array.put(subEvent.toJSON());
        }
        json.put(JSON_SUBEVENTS, array);
        json.put(JSON_PHOTO, photo.toJSON());
        return json;
    }

    public String getDateString() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        return df.format(date);
    }

    @Override
    public String toString() {
        return title;
    }

    public boolean isSettled() {
        return settled;
    }

    public void setSettled(boolean settled) {
        this.settled = settled;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<SubEvent> getSubEvents() {
        return subEvents;
    }

    public void setSubEvents(ArrayList<SubEvent> subEvents) {
        this.subEvents = subEvents;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void addSubEvent(SubEvent e) {
        subEvents.add(e);
    }

    public void removeSubEvent(SubEvent e){
        subEvents.remove(e);
    }
}
