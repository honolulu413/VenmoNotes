package com.example.lulu.venmonotes;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Joseph on 2015/8/11.
 */
public class FriendActivity extends ActionBarActivity {
    private String token;
    private EditText searchBox;
    private ArrayList<User> mFriendsList;
    private String currentUser;
    private FriendAdapter mAdapter;
    private ListView mListViewFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;

        super.onCreate(savedInstanceState);
        currentUser = getIntent().getStringExtra(HomePageActivity.CURRENT_USER);


        token = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(HomePageActivity.ACCESS_TOKEN, null);
        setContentView(R.layout.activity_friendlist);

        searchBox = (EditText) findViewById(R.id.searchBox);

        mListViewFriend = (ListView) findViewById(R.id.friendList);
        mListViewFriend.setClickable(true);
        mListViewFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            }
        });


        new FetchFriends().execute(token);

        searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                mAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

    }


    private class FetchFriends extends AsyncTask<String, Void, ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(String... params) {
            String token = params[0];
            return new FriendFetcher(token, currentUser).getFriend();
        }

        @Override
        protected void onPostExecute(ArrayList<User> result) {
            mFriendsList = result;
            mAdapter = new FriendAdapter(FriendActivity.this, -1, mFriendsList);
            mListViewFriend.setAdapter(mAdapter);
        }

    }
}
