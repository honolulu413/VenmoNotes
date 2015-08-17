package com.example.lulu.venmonotes;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;

/**
 * Created by lulu on 8/17/2015.
 */
public class FriendPageActivity extends ActionBarActivity {
    public static final String USER = "com.example.lulu.FriendPageActivity.user";
    public static final String TRAN = "com.example.lulu.FriendPageActivity.transactions";
    User mUser;
    ArrayList<Transaction> mTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        mUser = (User) getIntent().getSerializableExtra(USER);
        mTransactions = (ArrayList<Transaction>) getIntent().getSerializableExtra(TRAN);

    }

}
