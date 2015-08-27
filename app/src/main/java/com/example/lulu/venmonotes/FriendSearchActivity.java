package com.example.lulu.venmonotes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Joseph on 2015/8/11.
 */
public class FriendSearchActivity extends ActionBarActivity {
    private String TAG = "SEARCH";
    private String token;
    private EditText searchBox;
    private ArrayList<User> mFriendsList;
    private User currentUser;
    private FriendSearchAdapter mAdapter;
    private ListView mListViewFriend;
    private HashMap<User, ArrayList<Transaction>> mFriendTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;

        super.onCreate(savedInstanceState);
        currentUser = (User) getIntent().getSerializableExtra(HomePageActivity.CURRENT_USER);
        mFriendTransactions = (HashMap<User, ArrayList<Transaction>>) getIntent().getSerializableExtra(HomePageActivity.TRAN);

        mFriendsList = (ArrayList<User>) getIntent().getSerializableExtra(HomePageActivity.FRIENDS);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(59, 89, 152)));


        token = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(HomePageActivity.ACCESS_TOKEN, null);
        setContentView(R.layout.activity_friend_list);

        searchBox = (EditText) findViewById(R.id.searchBox);

        mListViewFriend = (ListView) findViewById(R.id.friendList);
        mAdapter = new FriendSearchAdapter(FriendSearchActivity.this, -1, mFriendsList);
        mListViewFriend.setAdapter(mAdapter);

        mListViewFriend.setClickable(true);
        mListViewFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                User friend = (User) arg0.getAdapter().getItem(position);
                Intent i = new Intent(FriendSearchActivity.this, FriendPageActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(FriendPageActivity.USER, friend);

                i.putExtras(mBundle);
                i.putExtra(FriendPageActivity.TRAN, mFriendTransactions.get(friend));
                startActivity(i);


            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Log.d(TAG, "" + (mAdapter == null));
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


}
