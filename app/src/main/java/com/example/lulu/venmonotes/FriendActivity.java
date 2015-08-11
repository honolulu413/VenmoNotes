package com.example.lulu.venmonotes;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
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
        new FetchFriends().execute(token);

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
            mListViewFriend.setAdapter(new FriendAdapter(FriendActivity.this, -1, mFriendsList));

        }

    }
}
