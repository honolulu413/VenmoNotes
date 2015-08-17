package com.example.lulu.venmonotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;

/**
 * Created by Joseph on 2015/8/11.
 */
public class FriendSearchAdapter extends ArrayAdapter<User> {
    public FriendSearchAdapter(Context context, int resource, ArrayList<User> items) {
        super(context, resource, items);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_friend_search, null);
        }

        User friend = getItem(position);

        TextView textViewName = (TextView) convertView.findViewById(R.id.friendName);
        textViewName.setText(friend.getDisplay_name());

        SmartImageView mImageView = (SmartImageView) convertView.findViewById(R.id.profileImg2);
        mImageView.setImageUrl(friend.getProfileUrl());

        

        return convertView;
    }

}
